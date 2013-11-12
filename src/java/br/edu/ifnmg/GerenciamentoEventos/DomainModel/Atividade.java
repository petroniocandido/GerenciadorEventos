/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author petronio
 */
@Entity
@Table(name = "atividades")
public class Atividade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Evento evento;
    
    @ManyToOne
    private AtividadeTipo tipo;

    private String nome;
    
    private String descricao;
    
    private boolean publica;
    
    private boolean necessitaInscricao;
    
    private boolean inscricaoColetiva;
    
    private BigDecimal valorInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date termino;
    
    @ManyToOne
    private Pessoa responsavel;
    
    @ManyToMany
    private List<Arquivo> arquivos;
    
    @ManyToOne
    private Local local;
    
    private int numeroVagas;
    
    private String auxiliar1;
    
    private String auxiliar2;
    
    private String auxiliar3;
    
    private String auxiliar4;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recursosalocacao", 
            joinColumns = {@JoinColumn(name = "AtividadeID")}, 
            inverseJoinColumns = {@JoinColumn(name = "RecursoID")})
    private List<Recurso> recursos;
    
    private BigDecimal valorOrcado;
    
    private BigDecimal valorExecutado;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date terminoInscricao;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToMany
    private List<Atividade> precursores;
    
    @ManyToMany
    private List<Atividade> dependentes;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Atividade)) {
            return false;
        }
        Atividade other = (Atividade) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade[ id=" + id + " ]";
    }
    
}
