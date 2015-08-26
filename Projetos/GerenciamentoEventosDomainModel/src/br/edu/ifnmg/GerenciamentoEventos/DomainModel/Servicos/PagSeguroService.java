/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.DomainModel.Services.LogService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.PagSeguroPerfil;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class PagSeguroService {
    @EJB
    PagSeguroPerfilRepositorio perfilDAO;
    
    @EJB
    LancamentoRepositorio lancamentoDAO;
    
    @EJB
    LogService log;
    
    PagSeguroPerfil perfil;
    
    private boolean Conectar(){
        return false;
    }
    
    private boolean Enviar(Lancamento l){
        return false;
    }
    
    private boolean Receber(String l){
        return false;
    }
    
    private boolean Desconectar(){
        return false;
    }
}
