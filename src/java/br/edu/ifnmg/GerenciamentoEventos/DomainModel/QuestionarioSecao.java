/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Entity
@Table(name="questionariossecoes")
@Cacheable(true)
public class QuestionarioSecao implements Serializable,Entidade {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch= FetchType.LAZY, targetEntity=Questionario.class)
    private Questionario questionario;
    
    @OneToMany(mappedBy = "secao", fetch = FetchType.LAZY)
    @OrderBy(value="Ordem")
    private List<Questao> questoes;
    
    private String nome;
    
    private int ordem;
    
    @ManyToOne(fetch= FetchType.LAZY, targetEntity=QuestionarioSecao.class)
    private QuestionarioSecao pai;
    
    @OneToMany(mappedBy = "pai", fetch = FetchType.LAZY)
    @OrderBy(value="Ordem")
    private List<QuestionarioSecao> filhos;

    public QuestionarioSecao(){
        this.nome = "";
        this.id = 0L;
        this.ordem = 0;
        this.pai = null;
        this.questionario = null;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    public void add(QuestionarioSecao m){
        if(filhos == null){
            filhos = new LinkedList<>();
        }
        m.setPai(this);
        if(!filhos.contains(m)){
            filhos.add(m);
        }
    }
    
    public void remove(QuestionarioSecao m){
        if(filhos == null){
            filhos = new LinkedList<>();
        }
        m.setPai(null);
        if(filhos.contains(m)){
            filhos.remove(m);
        }
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public QuestionarioSecao getPai() {
        return pai;
    }

    public void setPai(QuestionarioSecao pai) {
        this.pai = pai;
    }

    public List<QuestionarioSecao> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<QuestionarioSecao> filhos) {
        this.filhos = filhos;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() * nome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionarioSecao)) {
            return false;
        }
        QuestionarioSecao other = (QuestionarioSecao) object;
        if (((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
                || this.getPai() != other.getPai() || !this.getNome().equals(other.getNome())) {
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
