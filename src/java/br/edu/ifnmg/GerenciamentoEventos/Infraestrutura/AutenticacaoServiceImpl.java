/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.MailService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import java.util.Enumeration;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Stateless
public class AutenticacaoServiceImpl implements AutenticacaoService {

    @EJB
    PessoaRepositorio dao;
    @EJB
    HashService hash;
    @EJB
    MailService mail;
    @EJB
    ConfiguracaoRepositorio configuracao;
    
    Pessoa usuario;
    
    @Override
    public boolean login(String email, String senha) {
         usuario = dao.Abrir(email);

        if (usuario == null) {
            return false;
        } else {
            if (hash.getMD5(senha).equals(usuario.getSenha())) {

                HttpSession session;

                FacesContext ctx = FacesContext.getCurrentInstance();
                session = (HttpSession) ctx.getExternalContext().getSession(false);
                session.setAttribute("usuarioAutenticado", usuario);

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean logout() {
        HttpSession session;

        FacesContext ctx = FacesContext.getCurrentInstance();
        session = (HttpSession) ctx.getExternalContext().getSession(false);
        session.setAttribute("usuarioAutenticado", null);

        Enumeration<String> vals = session.getAttributeNames();

        while (vals.hasMoreElements()) {
            session.removeAttribute(vals.nextElement());
        }
        
        return true;
    }

    @Override
    public boolean redefinirSenha(String email) {
        usuario = dao.Abrir(email);
        if (usuario == null) {
            return false;
        } else {
            String msg = configuracao.Abrir("EMAIL_RECUPERARSENHA").getValor();
            String senhaantiga = usuario.getSenha();
            String tmpsenha = gerarSenha();
            usuario.setSenha(hash.getMD5(tmpsenha));
            msg = msg.replace("###SENHA###", tmpsenha);
            if(mail.enviar(usuario.getEmail(), "Nova Senha", msg)){
                dao.Salvar(usuario);
                return true;
            } else {
                usuario.setSenha(senhaantiga);
                return false;
            }
        }        
    }

    @Override
    public Pessoa getUsuarioCorrente() {
        if (usuario == null) {
            HttpSession session;
            FacesContext ctx = FacesContext.getCurrentInstance();
            session = (HttpSession) ctx.getExternalContext().getSession(false);
            usuario = (Pessoa) session.getAttribute("usuarioAutenticado");
        }
        return usuario;
    }
    
    private String gerarSenha() {
        String alfabeto = "abcdefghijklmnopqrstuvxz0123456789!@#$%&*()-+;.:ABCDEFGHIJKLMNOPQRSTUVXZ";
        Random rnd = new Random();
        StringBuilder tmp = new StringBuilder();
        while(tmp.length() < 8){
           tmp.append( alfabeto.charAt(rnd.nextInt(alfabeto.length())) );
        }
        
        return tmp.toString();
    }
    
}
