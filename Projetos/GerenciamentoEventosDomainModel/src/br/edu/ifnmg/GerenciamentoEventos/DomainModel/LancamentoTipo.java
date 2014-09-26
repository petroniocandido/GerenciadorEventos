/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
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
