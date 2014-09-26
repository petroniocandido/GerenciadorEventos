/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciadorMensagens.DataAccess;

import br.edu.ifnmg.DataAccess.DAOGenerico;
import br.edu.ifnmg.DomainModel.Entidade;
import javax.persistence.EntityManager;

/**
 *
 * @author petronio
 */
public class DAO<T extends Entidade> extends DAOGenerico<T> {

    public DAO(Class t) {
        super(t);
        setJts(false);
    }
           
    @Override
    public EntityManager getManager() {
        return ConnectionFactory.getInstance().getManager();
    }
    
}
