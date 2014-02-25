/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Inheritance(strategy= InheritanceType.JOINED)
@Table(name = "inscricoes")
public class Inscricao implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Evento evento;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;
    
    @ManyToOne
    QuestionarioResposta resposta;
    
    @Enumerated
    @Column(name = "DTYPE")
    protected InscricaoTipo tipo;
    
    private boolean pago;
    
    private boolean compareceu;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "inscricao")
    private List<InscricaoItem> itens;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Lancamento lancamento;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "inscricoesarquivos")
    private List<Arquivo> arquivos;
    
    public Inscricao() {
        arquivos = new ArrayList<>();
        itens = new ArrayList<>();
        tipo = InscricaoTipo.Inscricao;
    }
    
    public void add(InscricaoItem item){
        item.setInscricao(this);
        if(!itens.contains(item)){
            itens.add(item);
        }
    }
    
    public void remove(InscricaoItem item){
        if(itens.contains(item)){
            itens.remove(item);
            item.setInscricao(null);            
        }
    }
    
    public void add(Arquivo arquivo){
        if(!arquivos.contains(arquivo)){
            arquivos.add(arquivo);
        }
    }
    
    public void remove(Arquivo arquivo){
        if(arquivos.contains(arquivo)){
            arquivos.remove(arquivo);
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public boolean isCompareceu() {
        return compareceu;
    }

    public void setCompareceu(boolean compareceu) {
        this.compareceu = compareceu;
    }

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<InscricaoItem> getItens() {
        return itens;
    }

    public void setItens(List<InscricaoItem> itens) {
        this.itens = itens;
    }

    public QuestionarioResposta getResposta() {
        return resposta;
    }

    public void setResposta(QuestionarioResposta resposta) {
        this.resposta = resposta;
    }

    public InscricaoTipo getTipo() {
        return tipo;
    }

    public void setTipo(InscricaoTipo tipo) {
        this.tipo = tipo;
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
