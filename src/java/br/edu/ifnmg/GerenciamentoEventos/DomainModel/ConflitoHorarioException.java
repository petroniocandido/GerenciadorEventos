/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

/**
 *
 * @author petronio
 */
public class ConflitoHorarioException extends Exception {
    Entidade conflitante;

    public Entidade getConflitante() {
        return conflitante;
    }

    public ConflitoHorarioException(Entidade conflitante) {
        this.conflitante = conflitante;
    }
    
    
    
}
