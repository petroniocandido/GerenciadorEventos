/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciadorMensagens.Application;

import br.edu.ifnmg.DomainModel.Log;
import br.edu.ifnmg.DomainModel.Services.LogRepositorio;
import br.edu.ifnmg.DomainModel.Services.LogService;
import br.edu.ifnmg.GerenciadorMensagens.DataAccess.LogDAO;

/**
 *
 * @author petronio
 */
public class LogServiceImpl implements LogService {
    
    private LogRepositorio logDAO = new LogDAO();


    @Override
    public boolean Append(String msg) {
        try {
            Log log = new Log();
            log.setDescricao(msg);
            logDAO.Salvar(log);

        } catch (Exception ex) {
            System.out.println("Erro ao gravar log: " + ex.getMessage());
            return false;
        } 
        return true;
    }
}
