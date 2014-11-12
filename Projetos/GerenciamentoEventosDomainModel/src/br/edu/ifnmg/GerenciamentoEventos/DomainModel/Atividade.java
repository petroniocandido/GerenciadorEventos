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
import br.edu.ifnmg.DomainModel.Arquivo;
import br.edu.ifnmg.DomainModel.Entidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Cacheable(true)
@Entity
@Table(name = "atividades")
@NamedQueries({
    @NamedQuery(name = "atividades.porTipoEvento", query = "SELECT a FROM Atividade a where a.tipo = :tipo and a.evento = :evento and (a.status = :pendente or a.status = :emexecucao) order by a.inicio, a.termino"),
    @NamedQuery(name = "atividades.ativasUsuario", query = "SELECT a FROM Atividade a join a.responsaveis r where r.id = :idUsuario and a.termino <= :termino and a.status <> :cancelado and a.status <> :concluido"),
    @NamedQuery(name = "atividades.responsavel", query = "SELECT a FROM Atividade a join a.responsaveis r where a.evento = :evento and ( r.id = :idUsuario or a.responsavelPrincipal = :idUsuario )")
    })
public class Atividade implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "atividade")
    private Controle controle;
    
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
    
    private boolean geraCertificado;
        
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal valorInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date inicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date termino;
    
    private Pessoa responsavelPrincipal;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "atividadesresponsaveis")
    private List<Pessoa> responsaveis;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "atividadesarquivos")
    private List<Arquivo> arquivos;
    
    @ManyToOne
    Questionario questionario;
    
    @ManyToOne
    Questionario avaliacao;
    
    @ManyToOne
    private Recurso local;
    
    @Column(nullable = true)
    private int numeroVagas;
    
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal valorOrcado;
    
    @Column(nullable = true, precision = 10, scale = 2)
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
    
    private int cargaHoraria;
    
    @ElementCollection
    @CollectionTable(name = "atividades_inscricoesPorCategoria",
            joinColumns = @JoinColumn(name = "atividade"))
    @MapKeyJoinColumn(name = "eventoInscricaoCategoria", referencedColumnName = "id")
    @Column(name = "quantidadeInscricoes")
    private Map<EventoInscricaoCategoria, Integer> inscricoesPorCategoria;
    
    public Atividade() {
        recursos = new ArrayList<>();
        responsaveis = new ArrayList<>();
        precursores = new ArrayList<>();
        dependentes= new ArrayList<>();
        arquivos = new ArrayList<>();
        status = Status.Pendente;
        valorOrcado = new BigDecimal("0.00");
        valorExecutado = new BigDecimal("0.00");
        valorInscricao = new BigDecimal("0.00");        
        controle = new Controle(this, 0, 0);
        cargaHoraria = 0;
    }
    
    public boolean podeEditar(Pessoa obj) {
        return id == 0 ||  criador.equals(obj) || responsaveis.contains(obj);
    }
    
    public void cancelar() {
        for(Alocacao a : recursos){
            a.setStatus(AlocacaoStatus.Cancelado);
        }
    }
    
    public boolean isAtivo() {
        if(status == Status.Cancelado && status == Status.Concluido)
            return false;
        Date hoje = new Date();
        return hoje.compareTo(inicio) >= 0 && hoje.compareTo(termino) <= 0;
    }
    
    public boolean isPeriodoInscricaoAindaNaoAberto() {
        if(!necessitaInscricao)
            return false;
        if(status == Status.Cancelado && status == Status.Concluido)
            return false;
        Date hoje = new Date();
        return hoje.compareTo(inicioInscricao) <= 0 && hoje.compareTo(terminoInscricao) <= 0;
    }

    public boolean isPeriodoInscricaoAberto() {
        if(!necessitaInscricao)
            return false;
        if(status == Status.Cancelado && status == Status.Concluido)
            return false;
        Date hoje = new Date();
        return hoje.compareTo(inicioInscricao) >= 0 && hoje.compareTo(terminoInscricao) <= 0;
    }
    
    public boolean isVagasAberto() {
        if(getNumeroVagas() == 0)
            return true;
        
        return getControle().getQuantidadeGeral() < getNumeroVagas();
    }
    
    public boolean isListaEsperaAberto() {
        if(getNumeroVagas() == 0)
            return true;
        
        return !isVagasAberto() &&
                getControle().getQuantidadeListaEspera()< (getNumeroVagas()*0.1);
    }
    
    public boolean isInscricaoAberto() {
        return  necessitaInscricao && isPeriodoInscricaoAberto() && (isVagasAberto() || isListaEsperaAberto());
    }
    
    public void add(Pessoa responsavel){
        if(!responsaveis.contains(responsavel))
            responsaveis.add(responsavel);
    }
    public void remove(Pessoa responsavel){
        if(responsaveis.contains(responsavel))
            responsaveis.remove(responsavel);
    }
    
    public void add(Arquivo arquivo){
        if(!arquivos.contains(arquivo))
            arquivos.add(arquivo);
    }
    public void remove(Arquivo arquivo){
        if(arquivos.contains(arquivo))
            arquivos.remove(arquivo);
    }
    
    public void add(Alocacao recurso){
        recurso.setAtividade(this);
        recurso.setEvento(this.getEvento());
        if(!recursos.contains(recurso)){            
            recursos.add(recurso);
        }
    }
    public void remove(Alocacao recurso){
        if(recursos.contains(recurso)){
            recursos.remove(recurso);
            recurso.setStatus(AlocacaoStatus.Cancelado);
        }
    }
    
    public int getLimiteInscricoes(EventoInscricaoCategoria a){
        if(inscricoesPorCategoria.containsKey(a)){
            return inscricoesPorCategoria.get(a).intValue();
        } else {
            return 0;
        }
    }
    
    public void addLimite(EventoInscricaoCategoria a, int l){
        if(inscricoesPorCategoria.containsKey(a)){
            inscricoesPorCategoria.remove(a);
        }
        inscricoesPorCategoria.put(a, l);
    }
    
    public void removeLimite(EventoInscricaoCategoria a){
        inscricoesPorCategoria.remove(a);        
    }

    public Pessoa getResponsavelPrincipal() {
        return responsavelPrincipal;
    }

    public void setResponsavelPrincipal(Pessoa responsavelPrincipal) {
        this.responsavelPrincipal = responsavelPrincipal;
    }
    
    

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Controle getControle() {
        return controle;
    }

    public void setControle(Controle controle) {
        this.controle = controle;
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

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
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
        
        if(this.local != null){
            Alocacao a = new Alocacao();
            a.setEvento(evento);
            a.setAtividade(this);
            a.setRecurso(local);
            a.setInicio(inicio);
            a.setTermino(termino);
            a.setResponsavel(criador);
            add(a);
        }
    }

    public int getNumeroVagas() {
        return numeroVagas;
    }

    public void setNumeroVagas(int numeroVagas) {
        this.numeroVagas = numeroVagas;
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
        if(status == Status.Cancelado && this.status != null && this.status != Status.Cancelado)
            cancelar();
        
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

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public boolean isGeraCertificado() {
        return geraCertificado;
    }

    public void setGeraCertificado(boolean geraCertificado) {
        this.geraCertificado = geraCertificado;
    }

    public Questionario getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Questionario avaliacao) {
        this.avaliacao = avaliacao;
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
