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
public enum Status {
    Pendente(10, "Pendente"),
    EmExecucao(20, "Em Execução"),
    Concluido(30, "Concluído"),
    Cancelado(40, "Cancelado");
    
    private final int id;
    private final String desc;
    
    private Status(int id, String d){
        this.id = id;
        this.desc = d;
    }

    @Override
    public String toString() {
        return desc;
    }
    
    

}
