/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.DomainModel;

/**
 *
 * @author petronio
 */
public enum PronomeTratamento {
    Senhor("Sr."),
    Senhora("Sra."),
    Senhorita("Sra."),
    Professor("Prof."),
    Especialista("Prof. Esp."),
    Mestre("Prof. Ms."),
    Doutor("Prof. Dr.");
    
    private String descricao;

    private PronomeTratamento(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
    
}
