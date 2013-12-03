/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public abstract class AtividadeDAO 
    extends DAOGenerico<Atividade> 
    implements AtividadeRepositorio {
    
}
