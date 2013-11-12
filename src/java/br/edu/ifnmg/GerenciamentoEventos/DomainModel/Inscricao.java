/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "inscricoes")
public class Inscricao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Pessoa pessoa;
    
    @ManyToOne
    private Atividade atividade;
    
    private String titulo;
    
    private String observacoes;
    
    private String auxiliar1;
    
    private String auxiliar2;
    
    private String auxiliar3;
    
    private String auxiliar4;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;
    
    private boolean pago;
    
    private boolean compareceu;
    
    @ManyToOne
    private Lancamento lancamento;
    
    @ManyToMany
    private List<Arquivo> arquivos;

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
        if (!(object instanceof Inscricao)) {
            return false;
        }
        Inscricao other = (Inscricao) object;
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
