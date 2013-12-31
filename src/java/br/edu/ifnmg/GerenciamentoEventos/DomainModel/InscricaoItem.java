/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Entity
@Table(name = "inscricoesitens")
public class InscricaoItem extends Inscricao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Inscricao inscricao;
    
    @ManyToOne
    private Atividade atividade;
    
    private String titulo;
    
    private String observacoes;
    
    private String auxiliar1;
    
    private String auxiliar2;
    
    private String auxiliar3;
    
    private String auxiliar4;
    
    @ManyToMany
    @JoinTable(name = "inscricoescoletivas")
    private List<Pessoa> pessoas;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getAuxiliar1() {
        return auxiliar1;
    }

    public void setAuxiliar1(String auxiliar1) {
        this.auxiliar1 = auxiliar1;
    }

    public String getAuxiliar2() {
        return auxiliar2;
    }

    public void setAuxiliar2(String auxiliar2) {
        this.auxiliar2 = auxiliar2;
    }

    public String getAuxiliar3() {
        return auxiliar3;
    }

    public void setAuxiliar3(String auxiliar3) {
        this.auxiliar3 = auxiliar3;
    }

    public String getAuxiliar4() {
        return auxiliar4;
    }

    public void setAuxiliar4(String auxiliar4) {
        this.auxiliar4 = auxiliar4;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InscricaoItem)) {
            return false;
        }
        InscricaoItem other = (InscricaoItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao[ id=" + id + " ]";
    }
    
}
