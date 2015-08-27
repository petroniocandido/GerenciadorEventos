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

import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Entidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Entity
@Table(name = "lancamentos")
@Cacheable(false)
public class Lancamento implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date criacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date baixa;
    
    @ManyToOne
    private Evento evento;
    
    @ManyToOne
    private Atividade atividade;
    
    @ManyToOne
    private Pessoa usuarioBaixa;
    
    @ManyToOne
    private Pessoa cliente;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorOriginal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal valorDescontos;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal valorAcrescimos;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Lob
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    private LancamentoStatus status;
    
    @Enumerated(EnumType.ORDINAL)
    private LancamentoTipo tipo;
    
    @ManyToOne
    private LancamentoCategoria categoria;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lancamento")
    private List<Inscricao> inscricoes;
    
    private String transacaoPagSeguro;

    public Lancamento() {
        valorOriginal = new BigDecimal("0.00");
        valorAcrescimos = new BigDecimal("0.00");
        valorDescontos = new BigDecimal("0.00");
        valorTotal = new BigDecimal("0.00");
        status= LancamentoStatus.Aberto;
        tipo = LancamentoTipo.Credito;
        inscricoes = new ArrayList<>();
    }
    
    public boolean editavel(){
        return status == LancamentoStatus.Aberto;
    }

    public void add(Inscricao i){
        if(!editavel())
            return;
        
        if(!inscricoes.contains(i)){
            inscricoes.add(i);
            i.setLancamento(this);
            i.setPago(true);
            valorOriginal = valorOriginal.add(i.getValorTotal());
        }
        recalcularValorTotal();
    }
    
    public void remove(Inscricao i){
        if(!editavel())
            return;
        
        if(inscricoes.contains(i)){
            inscricoes.remove(i);
            i.setLancamento(null);
            valorOriginal = valorOriginal.subtract(i.getValorTotal());
        }
        recalcularValorTotal();
    }
    
    public void recalcularValorTotal() {
        if(!editavel())
            return;
        
        valorTotal = valorOriginal.add(valorAcrescimos).subtract(valorDescontos);
    }
    
    public void baixar(Pessoa p){
        if(!editavel())
            return;
        
        setStatus(LancamentoStatus.Baixado);
        setUsuarioBaixa(p);
        setUltimoAlterador(p);
        setDataUltimaAlteracao(new Date());
        setBaixa(new Date());
    }
    
    public void cancelar(Pessoa p){
        if(!editavel())
            return;
        
        setStatus(LancamentoStatus.Cancelado);
        setUsuarioBaixa(p);
        setUltimoAlterador(p);
        setDataUltimaAlteracao(new Date());
        setBaixa(new Date());
    }
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
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
        recalcularValorTotal();
    }

    public BigDecimal getValorDescontos() {
        return valorDescontos;
    }

    public void setValorDescontos(BigDecimal valorDescontos) {
        this.valorDescontos = valorDescontos;
        recalcularValorTotal();
    }

    public BigDecimal getValorAcrescimos() {
        return valorAcrescimos;
    }

    public void setValorAcrescimos(BigDecimal valorAcrescimos) {
        this.valorAcrescimos = valorAcrescimos;
        recalcularValorTotal();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public BigDecimal getValorTotalComSinal() {
        return (tipo == LancamentoTipo.Credito ? valorTotal : valorTotal.negate() );
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


    public LancamentoStatus getStatus() {
        return status;
    }

    public void setStatus(LancamentoStatus status) {
        this.status = status;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    public LancamentoTipo getTipo() {
        return tipo;
    }

    public void setTipo(LancamentoTipo tipo) {
        this.tipo = tipo;
    }

    public LancamentoCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(LancamentoCategoria categoria) {
        this.categoria = categoria;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getTransacaoPagSeguro() {
        return transacaoPagSeguro;
    }

    public void setTransacaoPagSeguro(String transacaoPagSeguro) {
        this.transacaoPagSeguro = transacaoPagSeguro;
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa criador;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa ultimoAlterador;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @Version
    private Long versao;
    

    @Override
    public Pessoa getCriador() {
        return criador;
    }

    @Override
    public void setCriador(Pessoa criador) {
        this.criador = criador;
    }

    @Override
    public Date getDataCriacao() {
        return dataCriacao;
    }

    @Override
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
        this.criacao = dataCriacao;
    }

    @Override
    public Pessoa getUltimoAlterador() {
        return ultimoAlterador;
    }

    @Override
    public void setUltimoAlterador(Pessoa ultimoAlterador) {
        this.ultimoAlterador = ultimoAlterador;
    }

    @Override
    public Date getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    @Override
    public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    @Override
    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

}
