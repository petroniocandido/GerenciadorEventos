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
public class LimiteInscricoesExcedidoException extends Exception {
    private int limite;
    private AtividadeTipo tipo;

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public AtividadeTipo getTipo() {
        return tipo;
    }

    public void setTipo(AtividadeTipo tipo) {
        this.tipo = tipo;
    }

    public LimiteInscricoesExcedidoException(int limite, AtividadeTipo tipo) {
        this.limite = limite;
        this.tipo = tipo;
    }
    
}
