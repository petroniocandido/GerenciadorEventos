/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

/**
 *
 * @author petronio
 */
public enum LancamentoTipo {
    Credito(1,"Crédito a receber"),
    Debito(2, "Débito a pagar"),
    Estorno(3,"Estorno");
    
    private final int id;  
    private final String descricao;  
  
    private LancamentoTipo(int id, String descricao) {  
        this.id = id;  
        this.descricao = descricao;  
    }  
  
    public String getDescricao() {  
        return descricao;  
    }  
  
    public int getId() {  
        return id;  
    }  
      
    public static LancamentoTipo parse(int id) {  
        LancamentoTipo enumtype = null;   
        for (LancamentoTipo item : LancamentoTipo.values()) {  
            if (item.getId() == id) {  
                enumtype = item;  
                break;  
            }  
        }  
        return enumtype;  
    }  
  
    @Override  
    public String toString() {  
        return this.descricao;  
    }  
    
}
