/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "publicoController")
@RequestScoped
public class PublicoController  {

    Long pessoaID;
    Long eventoID;
    Long inscricaoID;
    
    @EJB
    EventoRepositorio eventoDAO;
    
    /**
     * Creates a new instance of PublicoController
     */
    public PublicoController() {
        
    }
    
    public List<Evento> getEventos() {
        Evento filtro = new Evento();
        filtro.setStatus(Status.EmExecucao);
        return eventoDAO.Buscar(filtro);
    }
    
}
