/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciadorMensagens.DataAccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author petronio
 */
public class ConnectionFactory {
    private  EntityManager manager;
    private ConnectionFactory() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("GerenciadorMensagensPU");
        manager = factory.createEntityManager();
    }
    
    private static ConnectionFactory instance;
    public static ConnectionFactory getInstance() {
        if(instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public EntityManager getManager() {
        return manager;
    }
    
    
}
