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
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoRepositorio;
import br.edu.ifnmg.DomainModel.Services.HashService;
import br.edu.ifnmg.DomainModel.Services.MailService;
import br.edu.ifnmg.DomainModel.Services.PessoaRepositorio;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@SessionScoped
public class AutenticacaoService implements br.edu.ifnmg.DomainModel.Services.AutenticacaoService, Serializable {

    @EJB
    PessoaRepositorio dao;
    @EJB
    HashService hash;
    @EJB
    MailService mail;
    @EJB
    ConfiguracaoRepositorio configuracao;
    @Inject
    SessaoService sessao;

    Pessoa usuario;

    @Override
    public boolean login(String email, String senha) {
        usuario = dao.Abrir(email);

        if (usuario == null) {
            return false;
        } else {
            if (hash.getMD5(senha).equals(usuario.getSenha())) {

                sessao.put("usuarioAutenticado", usuario.getId().toString());

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
        
        session.invalidate();

        sessao.delete("usuarioAutenticado");
        
        sessao.limpar();

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
            if (mail.enviar(usuario.getEmail(), "Nova Senha", msg)) {
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
           String id = sessao.get("usuarioAutenticado");
           if(id != null) usuario = dao.Abrir(Long.parseLong(id));             
        }
        return usuario;
    }

    private String gerarSenha() {
        String alfabeto = "abcdefghijklmnopqrstuvxz0123456789!@#$%&*()-+;.:ABCDEFGHIJKLMNOPQRSTUVXZ";
        Random rnd = new Random();
        StringBuilder tmp = new StringBuilder();
        while (tmp.length() < 8) {
            tmp.append(alfabeto.charAt(rnd.nextInt(alfabeto.length())));
        }

        return tmp.toString();
    }
}
