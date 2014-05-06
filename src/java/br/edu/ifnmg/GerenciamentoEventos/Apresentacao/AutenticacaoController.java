/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutorizacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;

/**
 *
 * @author petronio
 */
@Named(value = "autenticacaoController")
@SessionScoped
public class AutenticacaoController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of AutenticacaoController
     */
    public AutenticacaoController() {

    }

    @EJB
    PessoaRepositorio dao;
    @EJB
    HashService hash;
    @EJB
    PerfilRepositorio perfilDAO;
    @EJB
    AutorizacaoService autorizacao;
    @EJB
    AutenticacaoService autenticacao;

    private String login, senha, senhaconferencia;
    Pessoa usuario;

    public void validar() {
        if (autenticacao.login(login, senha)) {
            usuario = autenticacao.getUsuarioCorrente();
            AppendLog("Login");
            Redirect(autenticacao.getUsuarioCorrente().getPerfil().getHome().getUri());
        } else {
            Mensagem("Falha", "Login ou senha não correspondem");
        }

    }

    public String logout() {

        AppendLog("Logout");

        autenticacao.logout();

        return "login.xhtml";

    }

    public String novo() {
        usuario = new Pessoa();
        return "cadastrar.xhtml";
    }

    public void cadastrar() {
        if (senha.equals(senhaconferencia)) {
            usuario.setSenha(hash.getMD5(senha));
            usuario.setPerfil(perfilDAO.getPadrao());
            if (dao.Salvar(usuario)) {
                AppendLog("Cadastro do usuário " + usuario.getEmail());
            } else {
                AppendLog("Erro ao cadastrar usuário: " + dao.getErro().toString());
            }
        } else {
            MensagemErro("Senhas", "Senhas não conferem!");
        }
    }

    public void salvar() {
        if (senha != null && !senha.isEmpty()) {
            if (senha.equals(senhaconferencia)) {
                usuario.setSenha(hash.getMD5(senha));
            } else {
                MensagemErro("Senhas", "Senhas não conferem!");
                return;
            }
        }
        if (dao.Salvar(usuario)) {
            AppendLog("Cadastro do usuário " + usuario.getEmail());
        } else {
            AppendLog("Erro ao cadastrar usuário: " + dao.getErro().toString());
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaconferencia() {
        return senhaconferencia;
    }

    public void setSenhaconferencia(String senhaconferencia) {
        this.senhaconferencia = senhaconferencia;
    }

    public Pessoa getUsuario() {
        return usuario;
    }

    public void setUsuario(Pessoa usuario) {
        this.usuario = usuario;
    }

    public boolean autorizacao(String url) {
        return autorizacao.possuiPermissao(url);
    }

    public void novasenha() {
        autenticacao.redefinirSenha(login);
    }
}
