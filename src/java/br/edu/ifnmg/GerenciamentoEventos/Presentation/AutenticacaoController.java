/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Enumeration;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

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

    private String login, senha, senhaconferencia;
    Pessoa usuario;

    public String validar() {
        usuario = dao.Abrir(login);

        if (usuario == null) {
            Mensagem("Falha", "Login ou senha não correspondem");
            return "login.xhtml";
        } else {
            if (hash.getMD5(senha).equals(usuario.getSenha())) {

                HttpSession session;

                FacesContext ctx = FacesContext.getCurrentInstance();
                session = (HttpSession) ctx.getExternalContext().getSession(false);
                session.setAttribute("usuarioAutenticado", usuario);

                AppendLog("Login");

                return usuario.getPerfil().getHome().getUri();

            } else {
                Mensagem("Falha", "Login ou senha não correspondem");
                return "login.xhtml";
            }
        }

    }

    public String logout() {
        
        AppendLog("Logout");
        
        HttpSession session;

        FacesContext ctx = FacesContext.getCurrentInstance();
        session = (HttpSession) ctx.getExternalContext().getSession(false);
        session.setAttribute("usuarioAutenticado", null);

        Enumeration<String> vals = session.getAttributeNames();

        while (vals.hasMoreElements()) {
            session.removeAttribute(vals.nextElement());
        }

        return "login.xhtml";

    }

    public String novo() {
        usuario = new Pessoa();
        return "cadastrar.xhtml";
    }

    public void salvar() {
        if (senha.equals(senhaconferencia)) {
            usuario.setSenha(hash.getMD5(senha));
            usuario.setPerfil(perfilDAO.getPadrao());
            dao.Salvar(usuario);
            AppendLog("Cadastro do usuário "+usuario.getEmail());
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
}
