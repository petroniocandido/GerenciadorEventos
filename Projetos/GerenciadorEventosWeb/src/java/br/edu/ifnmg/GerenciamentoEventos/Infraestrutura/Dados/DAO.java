/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DataAccess.DAOGenerico;
import br.edu.ifnmg.DomainModel.Entidade;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author petronio
 */
public class DAO<T extends Entidade> extends DAOGenerico<T> {

    public DAO(Class t) {
        super(t);
    }
    
    @PersistenceContext(name = "GerenciamentoEventosPU")
    private EntityManager manager;
    
    @Override
    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }
    
    
    
}
