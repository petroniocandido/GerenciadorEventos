/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.RetornoPagSeguro.DataAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("RetornoPagSeguroPU", getProperties());
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
    
    private static Map getProperties() {
        FileInputStream file = null;
        try {
            Map properties = new HashMap<>();
            Properties props = new Properties();
            file = new FileInputStream("./RetornoPagSeguro.properties");
            props.load(file);
            properties.put("javax.persistence.jdbc.url", props.getProperty("javax.persistence.jdbc.url"));
            properties.put("javax.persistence.jdbc.password", props.getProperty("javax.persistence.jdbc.password"));
            properties.put("javax.persistence.jdbc.driver", props.getProperty("javax.persistence.jdbc.driver"));
            properties.put("javax.persistence.jdbc.user", props.getProperty("javax.persistence.jdbc.user"));
            properties.put("javax.persistence.schema-generation.database.action", props.getProperty("javax.persistence.schema-generation.database.action"));
            return properties;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
