/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Perfil;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ValidadorCPF;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.AutorizacaoService;
import br.edu.ifnmg.DomainModel.Services.HashService;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

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
    PessoaRepositorioLocal dao;
    @EJB
    HashService hash;
    @EJB
    PerfilRepositorio perfilDAO;
    @Inject
    AutorizacaoService autorizacao;
    
    AreaConhecimento areaConhecimento;

    private String login, senha, senhaconferencia;
    Pessoa usuario;

    public void validar() {
        if (autenticacao.login(login, senha)) {
            AppendLog("Login");
            Redirect(autenticacao.getUsuarioCorrente().getPerfil().getHome().getUri());
        } else {
            MensagemErro("Falha", "Login ou senha não correspondem");
        }

    }

    public void logout() throws IOException {

        AppendLog("Logout");

        setUsuario(null);

        autenticacao.logout();

        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("../login.xhtml");

    }

    public String novo() {
        setUsuario(null);
        return "cadastrar.xhtml";
    }

    public void cadastrar() {
        getUsuario().setPerfil(perfilDAO.getPadrao());
        usuario.setSenha(java.util.UUID.randomUUID().toString());
        if (dao.Salvar(usuario)) {
            autenticacao.redefinirSenha(usuario.getEmail());
            AppendLog("Cadastro do usuário " + usuario.getEmail());
        } else {
            AppendLog("Erro ao cadastrar usuário: " + dao.getErro().toString());
        }
    }
    
    public void cadastrarAvaliador() {
        
        Pessoa tmp = dao.Abrir(getUsuario().getEmail());
        
        if(tmp != null && autenticacao.getUsuarioCorrente() == null){
            MensagemErro("Falha!", "O e-mail informado já está cadastrado no sistema! Efetue login e volte à página de cadastro de avaliador!");
            return;
        }
        
        if(tmp != null && autenticacao.getUsuarioCorrente() != null && !autenticacao.getUsuarioCorrente().getEmail().equals(tmp.getEmail())){
            MensagemErro("Falha!", "O e-mail informado é diferente do usuário logado no sistema!");
            return;
        }
        
        if(getUsuario().getAreasConhecimento().isEmpty()){
            MensagemErro("Falha!", "Você precisa cadastrar pelo menos uma área de conhecimento!");
            return;
        }
        
        Perfil avaliador = perfilDAO.Abrir( Long.parseLong( getConfiguracao("PERFILSELECAOAVALIADOR") ) );
        
        getUsuario().setPerfil(avaliador);
        
        if(tmp == null) usuario.setSenha(java.util.UUID.randomUUID().toString());
        if (dao.Salvar(usuario)) {
            if(tmp == null) {
                autenticacao.redefinirSenha(usuario.getEmail());            
                AppendLog("Cadastro do usuário " + usuario.getEmail());
            } else {
                AppendLog("Cadastro do usuário " + usuario.getEmail() + "como avaliador");
            }
            
            Mensagem("Sucesso!","O seu cadastro será submetido a uma avaliação de perfil. Em breve entraremos em contato!");
            
        } else {
            AppendLog("Erro ao cadastrar usuário: " + dao.getErro().toString());
            MensagemErro("Falha!","Erro ao cadastrar usuário! Entre em contato com o administrador.");
        }
    }

    public void salvar() {
        if (senha != null && !senha.isEmpty()) {
            if (senha.equals(senhaconferencia)) {
                getUsuario().setSenha(hash.getMD5(senha));
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
        if (usuario == null) {
            usuario = (Pessoa) getSessao("usuarioAutenticado", dao);
            if (usuario == null) {
                usuario = new Pessoa();
            }
        }
        return usuario;
    }

    public void setUsuario(Pessoa usuario) {
        this.usuario = usuario;
        setSessao("usuarioAutenticado", usuario);
    }

    public boolean autorizacao(String url) {
        return autorizacao.possuiPermissao(url);
    }

    public void novasenha() {
        if (autenticacao.redefinirSenha(login)) {
            Mensagem("Sucesso!", "Em alguns minutos será enviada uma nova senha para o e-mail informado! ");
        } else {
            MensagemErro("Falha!", "Houve um problema ao enviar o e-mail com a nova senha! "
                    + "Consulte o administrador do sistema ou tente novamente em alguns instantes.");
        }
    }

    public void idleListener() {
        autenticacao.logout();
    }

    public void validaCPF(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (getUsuarioCorrente() != null) {
            return;
        }

        Pessoa tmp = dao.AbrirPorCPF(value.toString());

        if (tmp != null) {
            FacesMessage msg
                    = new FacesMessage("CPF já cadastrado!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

        if (!ValidadorCPF.validaCPF(value.toString())) {
            FacesMessage msg
                    = new FacesMessage("CPF Inválido!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public void validaEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (getUsuarioCorrente() != null) {
            return;
        }

        Pessoa tmp = dao.Abrir(value.toString());

        if (tmp != null) {
            FacesMessage msg
                    = new FacesMessage("E-mail já cadastrado!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
    
    
    
    public void addAreaConhecimento() {
        getUsuario().add(areaConhecimento);
        dao.Salvar(usuario);
        areaConhecimento = null; 
    }

    public void removeAreaConhecimento() {
        getUsuario().remove(areaConhecimento);
        dao.Salvar(usuario);
        areaConhecimento = null; 
    }

}
