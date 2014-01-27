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
public enum RecursoTipo {
    Imovel("Imóvel"),
    Movel("Móvel"),
    Equipamento("Equipamento"),
    MaterialConsumivel("Material Consumível"),
    ServicoInterno("Serviço Institucional"),
    ServicoExterno("Serviço Externo");
    
    private final String nome;
    
    private RecursoTipo(String nom){
        this.nome = nom;
    }
    
    @Override
    public String toString(){
        return nome;
    }
}
