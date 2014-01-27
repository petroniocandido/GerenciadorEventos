/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface HashService {
    public String getMD5(String msg);
}
