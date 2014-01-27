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
@Cacheable
@Entity
@Table(name = "eventos")
public class Evento implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(length = 500, nullable = false, unique = true)
    private String nome;
    
    @Lob
    private String descricao;
    
    private boolean necessitaInscricao;
    
    private BigDecimal valorInscricao;
    
    @ManyToOne
    private Recurso local;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date termino;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date terminoInscricao;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToOne
    private Arquivo logo;
    
    @ManyToOne
    private Arquivo banner;
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "evento")
    private List<Alocacao> recursos;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isNecessitaInscricao() {
        return necessitaInscricao;
    }

    public void setNecessitaInscricao(boolean necessitaInscricao) {
        this.necessitaInscricao = necessitaInscricao;
    }

    public BigDecimal getValorInscricao() {
        return valorInscricao;
    }

    public void setValorInscricao(BigDecimal valorInscricao) {
        this.valorInscricao = valorInscricao;
    }

    public Recurso getLocal() {
        return local;
    }

    public void setLocal(Recurso local) {
        this.local = local;
    }

    
    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) throws ValidacaoException {    
        /*if(termino == null)
            return;
        if(termino.before(inicio))
            throw new ValidacaoException("A data de término do evento não pode ser menor do que a data de início!");
                */
        this.termino = termino;
    }

    public Date getInicioInscricao() {
        return inicioInscricao;
    }

    public void setInicioInscricao(Date inicioInscricao) throws ValidacaoException {
       /* if(inicioInscricao == null)
            return;
        if(inicioInscricao.after(termino))
            throw new ValidacaoException("A data de início de inscrições do evento não pode ser maior do que a data de término do evento!");
               */
        this.inicioInscricao = inicioInscricao;
    }

    public Date getTerminoInscricao() {
        return terminoInscricao;
    }

    public void setTerminoInscricao(Date terminoInscricao) throws ValidacaoException {
        /*if(terminoInscricao == null)
            return;
        if(terminoInscricao.before(inicioInscricao))
            throw new ValidacaoException("A data de término de inscrições do evento não pode ser menor do que a data de início do evento!");
                */
        this.terminoInscricao = terminoInscricao;
    }

    public List<Alocacao> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Alocacao> recursos) {
        this.recursos = recursos;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Arquivo getLogo() {
        return logo;
    }

    public void setLogo(Arquivo logo) {
        this.logo = logo;
    }

    public Arquivo getBanner() {
        return banner;
    }

    public void setBanner(Arquivo banner) {
        this.banner = banner;
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
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
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
