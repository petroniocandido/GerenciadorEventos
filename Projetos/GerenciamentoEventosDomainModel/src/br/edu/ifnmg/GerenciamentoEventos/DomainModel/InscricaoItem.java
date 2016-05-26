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

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author petronio
 */
@Entity
@Table(name = "inscricoesitens")
@Cacheable(true)
public class InscricaoItem extends Inscricao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Inscricao inscricao;
    
    @ManyToOne
    private Atividade atividade;
    
    public InscricaoItem(){
         tipo = InscricaoTipo.InscricaoItem;
    }
    
    public InscricaoItem(Inscricao i, Atividade a){
        this.tipo = InscricaoTipo.InscricaoItem;
        this.inscricao = i;
        this.atividade = a;
        setEvento(a.getEvento());
        setPessoa(i.getPessoa());
    }
       
    @Override
    public boolean isProntoParaCertificado() {
        if(prontoParaCertificado == null){
            prontoParaCertificado = getEvento().requerPagamento() ?  isPago() : true;
            
            prontoParaCertificado = prontoParaCertificado 
                    && atividade.isGeraCertificado() 
                    && atividade.getStatus() == Status.Concluido 
                    && isCompareceu();
        }
        return prontoParaCertificado;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.inscricao);
        hash = 83 * hash + Objects.hashCode(this.atividade);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InscricaoItem other = (InscricaoItem) obj;
        if (!Objects.equals(this.inscricao, other.inscricao)) {
            return false;
        }
        if (!Objects.equals(this.atividade, other.atividade)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        if(id == null)
            return Integer.toString(hashCode());
        return id + "";
    }
    
}
