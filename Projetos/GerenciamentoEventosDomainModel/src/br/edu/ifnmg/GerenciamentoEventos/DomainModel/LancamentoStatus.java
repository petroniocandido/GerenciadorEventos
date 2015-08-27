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
public enum LancamentoStatus {
    Aberto("Pagamento pendente"),             // Ainda não foi gerada orde de pagamento
    Baixado("Pagamento confirmado"),            // Pagamento confirmado
    Cancelado("Pagamento cancelado"),          
    AguardandoConfirmacao("Aguardando confirmação do pagamento");   // Gerada ordem de pagamento, mas o pagamento ainda não foi confirmado
    
    private String descricao;
    
    private LancamentoStatus(String d){
        this.descricao = d;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
