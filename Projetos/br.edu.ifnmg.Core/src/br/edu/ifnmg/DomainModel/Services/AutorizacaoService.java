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

package br.edu.ifnmg.DomainModel.Services;

import br.edu.ifnmg.DomainModel.Permissao;
import java.io.Serializable;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author petronio
 */
@Named
@SessionScoped
public class AutorizacaoService implements Serializable {
    
    @Inject
    AutenticacaoService autenticacao;
    
    @EJB
    PermissaoRepositorio permissaoDAO;
    
    HashMap<String,Permissao> permissoes;
    
    public AutorizacaoService() {
        permissoes = new HashMap<>();
    }
    
    public boolean possuiPermissao(Permissao pf){
        return autenticacao.getUsuarioCorrente().getPerfil().getPermissoes().contains(pf);            
    }
    
    public boolean possuiPermissao(String url){
        Permissao pf;
        if(permissoes.containsKey(url)){
            pf = permissoes.get(url);
        } else {
            pf = permissaoDAO.Abrir(url);
            permissoes.put(url, pf);
        }
        boolean acesso = autenticacao.getUsuarioCorrente().getPerfil().getPermissoes().contains(pf);
        return acesso;
    }
    
}
