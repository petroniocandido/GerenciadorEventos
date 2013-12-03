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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author petronio
 */
@Entity
@Table(name = "lancamentos")
public class Lancamento extends Entidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date criacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date baixa;
    
    @ManyToOne
    private Pessoa usuarioCriacao;
    
    @ManyToOne
    private Pessoa usuarioBaixa;
    
    @ManyToOne
    private Pessoa cliente;
    
    private BigDecimal valorOriginal;
    
    private BigDecimal valorDescontos;
    
    private BigDecimal valorAcrescimos;
    
    private BigDecimal valorTotal;
    
    private String descricao;
    
    List<ItemFinanceiro> itens;
    
    private LancamentoStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCriacao() {
        return criacao;
    }

    public void setCriacao(Date criacao) {
        this.criacao = criacao;
    }

    public Date getBaixa() {
        return baixa;
    }

    public void setBaixa(Date baixa) {
        this.baixa = baixa;
    }

    public Pessoa getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Pessoa usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Pessoa getUsuarioBaixa() {
        return usuarioBaixa;
    }

    public void setUsuarioBaixa(Pessoa usuarioBaixa) {
        this.usuarioBaixa = usuarioBaixa;
    }

    public Pessoa getCliente() {
        return cliente;
    }

    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorDescontos() {
        return valorDescontos;
    }

    public void setValorDescontos(BigDecimal valorDescontos) {
        this.valorDescontos = valorDescontos;
    }

    public BigDecimal getValorAcrescimos() {
        return valorAcrescimos;
    }

    public void setValorAcrescimos(BigDecimal valorAcrescimos) {
        this.valorAcrescimos = valorAcrescimos;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ItemFinanceiro> getItens() {
        return itens;
    }

    public void setItens(List<ItemFinanceiro> itens) {
        this.itens = itens;
    }

    public LancamentoStatus getStatus() {
        return status;
    }

    public void setStatus(LancamentoStatus status) {
        this.status = status;
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
        if (!(object instanceof Lancamento)) {
            return false;
        }
        Lancamento other = (Lancamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento[ id=" + id + " ]";
    }
    
}
