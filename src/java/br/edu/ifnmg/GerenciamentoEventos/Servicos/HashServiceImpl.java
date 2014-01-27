/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class HashServiceImpl implements HashService {

    @Override
    public String getMD5(String msg) {
        try {
            byte[] bytesOfMessage = msg.getBytes("ASCII");
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            String decoded = new String(thedigest, "ASCII"); 
            return decoded;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HashServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}
