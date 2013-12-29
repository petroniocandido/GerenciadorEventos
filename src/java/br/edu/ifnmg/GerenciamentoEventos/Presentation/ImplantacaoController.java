/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author petronio
 */
@Named(value = "implantacaoController")
@SessionScoped
public class ImplantacaoController implements Serializable {

    @EJB
    PermissaoRepositorio permissaoDAO;

    @EJB
    PerfilRepositorio perfilDAO;

    @EJB
    PessoaRepositorio pessoaDAO;

    @EJB
    ConfiguracaoRepositorio configuracaoDAO;

    @EJB
    HashService hash;

    /**
     * Creates a new instance of ImplantacaoController
     */
    public ImplantacaoController() {
    }

    protected void Mensagem(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "Implantação");
        context.addMessage(null, m);
    }

    public boolean Salvar(List objs, Repositorio dao) {
        for (Object o : objs) {
            if (!dao.Salvar(o)) {
                return false;
            }
        }
        return true;
    }

    public void Rastrear(Entidade e, Pessoa usuario) {
        e.setDataCriacao(new Date());
        e.setDataUltimaAlteracao(new Date());
        e.setCriador(usuario);
        e.setUltimoAlterador(usuario);
    }

    public boolean Salvar(List objs, Repositorio dao, Pessoa usuario) {
        for (Object o : objs) {
            Entidade e = (Entidade) o;
            Rastrear(e, usuario);
            if (!dao.Salvar(e)) {
                return false;
            }
            o = dao.Abrir(e.getId());
        }
        return true;
    }

    public Permissao criarPermissao(String nome, String uri, Pessoa usuario) {
        Permissao tmp = permissaoDAO.Abrir(uri);
        if (tmp == null) {
            tmp = new Permissao(nome, uri);
            Rastrear(tmp, usuario);
            if (!permissaoDAO.Salvar(tmp)) {
                Mensagem(permissaoDAO.getErro().getMessage());
            } else {
                Mensagem("Permissão " + uri + " criada.");
            }
            tmp = permissaoDAO.Abrir(uri);
        }
        return tmp;
    }

    public void gerarBD() {
        try {

            Pessoa usuarioSystem = pessoaDAO.Abrir("system@ifnmg.edu.br");
            if (usuarioSystem == null) {
                usuarioSystem = new Pessoa();
                usuarioSystem.setNome("System");
                usuarioSystem.setCpf("11111111111");
                usuarioSystem.setEmail("system@ifnmg.edu.br");
                usuarioSystem.setSenha(hash.getMD5("admin"));

                if (!pessoaDAO.Salvar(usuarioSystem)) {
                    Mensagem(pessoaDAO.getErro().getMessage());
                } else {
                    Mensagem("Usuário system criado.");
                }

                usuarioSystem = pessoaDAO.Abrir("system@ifnmg.edu.br");
            }

            List<Permissao> permissoesPublicas = new LinkedList<>();

            permissoesPublicas.add(criarPermissao("Login", "index.xhtml", usuarioSystem));
            permissoesPublicas.add(criarPermissao("Cadastro de Novo Usuário", "cadastro.xhtml", usuarioSystem));
            Permissao homePublico = criarPermissao("Home Público", "public/index.xhtml", usuarioSystem);
            permissoesPublicas.add(homePublico);

            List<Permissao> permissoesAdmin = new LinkedList<>();
            Permissao homeAdmin = criarPermissao("Home Administrativo", "admin/index.xhtml", usuarioSystem);
            permissoesAdmin.add(homeAdmin);
            permissoesAdmin.add(criarPermissao("Listagem Usuários", "admin/listagemUsuarios.xhtml", usuarioSystem));
            permissoesAdmin.add(criarPermissao("Edição de Usuários", "admin/editarUsuarios.xhtml", usuarioSystem));

            List<Permissao> permissoesSystem = new LinkedList<>();
            permissoesAdmin.add(criarPermissao("Logs", "admin/listagemLogs.xhtml", usuarioSystem));

            Perfil publico = perfilDAO.Abrir("Público");
            if (publico == null) {
                publico = new Perfil("Público", "Público", homePublico, true);
                Rastrear(publico, usuarioSystem);

                if (!perfilDAO.Salvar(publico)) {
                    Mensagem(perfilDAO.getErro().getMessage());
                } else {
                    Mensagem("Perfil público criado.");
                }

                publico = perfilDAO.Abrir("Público");

            }

            for (Permissao p : permissoesPublicas) {
                publico.add(p);
            }
            if (!perfilDAO.Salvar(publico)) {
                Mensagem(perfilDAO.getErro().getMessage());
            } else {
                Mensagem("Permissões do perfil público criadas.");
            }

            Perfil admin = perfilDAO.Abrir("Administrador");
            if (admin == null) {
                admin = new Perfil("Administrador", "Administrador", homeAdmin, false);
                Rastrear(admin, usuarioSystem);

                if (!perfilDAO.Salvar(admin)) {
                    Mensagem(perfilDAO.getErro().getMessage());
                } else {
                    Mensagem("Perfil administrativo criado.");
                }

                admin = perfilDAO.Abrir("Administrador");
            }
            for (Permissao p : permissoesPublicas) {
                admin.add(p);
            }

            for (Permissao p : permissoesAdmin) {
                admin.add(p);
            }

            if (!perfilDAO.Salvar(admin)) {
                Mensagem(perfilDAO.getErro().getMessage());
            } else {
                Mensagem("Permissões do perfil administrativo criadas.");
            }

            Perfil system = perfilDAO.Abrir("System");
            if (system == null) {
                system = new Perfil("System", "System", homeAdmin, false);
                Rastrear(system, usuarioSystem);

                if (!perfilDAO.Salvar(system)) {
                    Mensagem(perfilDAO.getErro().getMessage());
                } else {
                    Mensagem("Perfil system criado.");
                }
                system = perfilDAO.Abrir("System");
            }

            for (Permissao p : permissoesPublicas) {
                system.add(p);
            }

            for (Permissao p : permissoesAdmin) {
                system.add(p);
            }

            for (Permissao p : permissoesSystem) {
                system.add(p);
            }

            if (!perfilDAO.Salvar(system)) {
                Mensagem(perfilDAO.getErro().getMessage());
            } else {
                Mensagem("Permissões do perfil system criadas.");
            }

            usuarioSystem.setPerfil(system);

            if (!pessoaDAO.Salvar(usuarioSystem)) {
                Mensagem(pessoaDAO.getErro().getMessage());
            } else {
                Mensagem("Perfil do usuário system atualizado.");
            }

            Pessoa usuarioTeste = new Pessoa();

            usuarioTeste.setNome("Teste");
            usuarioTeste.setCpf("22222222222");
            usuarioTeste.setEmail("teste@ifnmg.edu.br");
            usuarioTeste.setSenha(hash.getMD5("123"));
            usuarioTeste.setPerfil(publico);
            Rastrear(usuarioTeste, usuarioSystem);

            if (!pessoaDAO.Salvar(usuarioTeste)) {
                Mensagem(pessoaDAO.getErro().getMessage());
            } else {
                Mensagem("Usuário teste criado.");
            }

            List<Configuracao> configuracoes = new LinkedList<>();

            configuracoes.add(new Configuracao("SMTPServidor", "127.0.0.1"));
            configuracoes.add(new Configuracao("SMTPPorta", ""));
            configuracoes.add(new Configuracao("SMTPUsuario", ""));
            configuracoes.add(new Configuracao("SMTPSenha", "127.0.0.1"));
            configuracoes.add(new Configuracao("SMTPSsl", ""));

            if (Salvar(configuracoes, configuracaoDAO, usuarioSystem)) {
                Mensagem(configuracaoDAO.getErro().getMessage());
            } else {
                Mensagem("Configurações padrão criadas.");
            }

        } catch (Exception ex) {
            Mensagem(ex.getMessage());
        }
    }

}
