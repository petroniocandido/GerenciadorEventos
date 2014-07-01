/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
