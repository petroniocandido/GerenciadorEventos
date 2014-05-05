/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
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
    
    public boolean possuiPermissao(Permissao pf){
        return autenticacao.getUsuarioCorrente().getPerfil().getPermissoes().contains(pf);            
    }
    
    public boolean possuiPermissao(String url){
        Permissao pf;
        pf = permissaoDAO.Abrir(url);
        boolean acesso = autenticacao.getUsuarioCorrente().getPerfil().getPermissoes().contains(pf);
        return acesso;
    }
    
}
