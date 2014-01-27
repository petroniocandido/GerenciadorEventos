/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface LogRepositorio extends Repositorio<Log> {
    
}
