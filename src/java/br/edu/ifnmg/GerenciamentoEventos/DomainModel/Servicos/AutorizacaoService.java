/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 *
 * @author petronio
 */
@Named(value = "autorizacaoService")
@Stateless
public class AutorizacaoService {
    
    @EJB
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
