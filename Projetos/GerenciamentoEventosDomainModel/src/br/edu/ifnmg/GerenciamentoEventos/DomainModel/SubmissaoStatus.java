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
public enum SubmissaoStatus  {
    EmEdicao(false,"Em Edição"),
    Pendente(false,"Avaliação Pendente"),
    Atribuido(false,"Distribuído para avaliação"),
    Avaliado(false,"Já Avaliado"),
    Divergencia(false,"Divergência entre as avaliações"),
    Aprovado(true,"Aprovado"),
    Reprovado(true,"Reprovado"),
    Cancelado(true,"Cancelado"),
    Desclassificado(true,"Desclassificado");
    
    private final boolean isfinal;
    private final String descricao;
    
    private SubmissaoStatus(boolean isfinal, String descricao){
        this.isfinal = isfinal;
        this.descricao = descricao;
    }

    public boolean isFinal() {
        return isfinal;
    }

    public String getDescricao() {
        return descricao;
    }
    
    
}
