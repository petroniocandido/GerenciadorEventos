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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
@Cacheable
@Entity
@Table(name = "atividades")
public class Atividade implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Evento evento;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private AtividadeTipo tipo;

    @Column(nullable = false)
    private String nome;
    
    @Lob
    private String descricao;
    
    @Column(nullable = true)
    private boolean necessitaInscricao;
    
    @Column(nullable = true)
    private boolean inscricaoColetiva;
    
    @Column(nullable = true)
    private BigDecimal valorInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date inicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date termino;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "atividadesresponsaveis")
    private List<Pessoa> responsaveis;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "atividadesarquivos")
    private List<Arquivo> arquivos;
    
    @ManyToOne
    private Recurso local;
    
    @Column(nullable = true)
    private int numeroVagas;
    
    @Column(nullable = true)
    private String auxiliar1;
    
    @Column(nullable = true)
    private String auxiliar2;
    
    @Column(nullable = true)
    private String auxiliar3;
    
    @Column(nullable = true)
    private String auxiliar4;
    
    @Column(nullable = true)
    private BigDecimal valorOrcado;
    
    @Column(nullable = true)
    private BigDecimal valorExecutado;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date inicioInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date terminoInscricao;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToMany
    private List<Atividade> precursores;
    
    @ManyToMany
    private List<Atividade> dependentes;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "atividade")
    private List<Alocacao> recursos;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public AtividadeTipo getTipo() {
        return tipo;
    }

    public void setTipo(AtividadeTipo tipo) {
        this.tipo = tipo;
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

    public boolean isInscricaoColetiva() {
        return inscricaoColetiva;
    }

    public void setInscricaoColetiva(boolean inscricaoColetiva) {
        this.inscricaoColetiva = inscricaoColetiva;
    }

    public BigDecimal getValorInscricao() {
        return valorInscricao;
    }

    public void setValorInscricao(BigDecimal valorInscricao) {
        this.valorInscricao = valorInscricao;
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

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    public List<Pessoa> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<Pessoa> responsaveis) {
        this.responsaveis = responsaveis;
    }

    

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public Recurso getLocal() {
        return local;
    }

    public void setLocal(Recurso local) {
        this.local = local;
    }

    public int getNumeroVagas() {
        return numeroVagas;
    }

    public void setNumeroVagas(int numeroVagas) {
        this.numeroVagas = numeroVagas;
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

    public List<Alocacao> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Alocacao> recursos) {
        this.recursos = recursos;
    }

    public BigDecimal getValorOrcado() {
        return valorOrcado;
    }

    public void setValorOrcado(BigDecimal valorOrcado) {
        this.valorOrcado = valorOrcado;
    }

    public BigDecimal getValorExecutado() {
        return valorExecutado;
    }

    public void setValorExecutado(BigDecimal valorExecutado) {
        this.valorExecutado = valorExecutado;
    }

    public Date getInicioInscricao() {
        return inicioInscricao;
    }

    public void setInicioInscricao(Date inicioInscricao) {
        this.inicioInscricao = inicioInscricao;
    }

    public Date getTerminoInscricao() {
        return terminoInscricao;
    }

    public void setTerminoInscricao(Date terminoInscricao) {
        this.terminoInscricao = terminoInscricao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Atividade> getPrecursores() {
        return precursores;
    }

    public void setPrecursores(List<Atividade> precursores) {
        this.precursores = precursores;
    }

    public List<Atividade> getDependentes() {
        return dependentes;
    }

    public void setDependentes(List<Atividade> dependentes) {
        this.dependentes = dependentes;
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
        return nome + " (" + evento + ")";
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
