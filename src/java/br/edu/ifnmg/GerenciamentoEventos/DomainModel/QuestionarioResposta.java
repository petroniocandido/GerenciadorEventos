/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Entity
@Table(name = "questionariosrespostas")
@Cacheable(false)
public class QuestionarioResposta implements Serializable, Entidade {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa pessoa;
   
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Questionario questionario;
    
    @OneToMany(cascade= CascadeType.ALL, mappedBy="resposta")
    private List<QuestaoResposta> respostas;
    
    @Transient
    private Map<Questao,QuestaoResposta> maps;

    public QuestionarioResposta(){
        this.id = 0L;
        this.respostas = new LinkedList<>();
        this.maps = new HashMap<>();
    }
    
    public QuestionarioResposta(Pessoa p, Questionario q){
        this.id = 0L;
        this.respostas = new LinkedList<>();
        this.maps = new HashMap<>();
        this.pessoa = p;
        this.questionario = q;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    public void add(QuestaoResposta m){
        m.setResposta(this);
        if(!respostas.contains(m)){
            respostas.add(m);
        }
    }
    
    public void remove(QuestaoResposta m){
        if(respostas.contains(m)){
            respostas.remove(m);
            m.setResposta(null);
        }
    }
    
    public QuestaoResposta RespostaDeQuestao(Questao q){  
        if(!maps.containsKey(q))
            criaMapaQuestoes();
        
        return maps.get(q);
    }
    
    private void criaMapaQuestoes() {
        for(QuestaoResposta r : respostas){
            maps.put(r.getQuestao(), r);
        }
    }
   
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public List<QuestaoResposta> getRespostas() {
        return respostas;
    }
    
    public void setRespostas(List<QuestaoResposta> respostas) {
        this.respostas = respostas;
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
        if (!(object instanceof QuestionarioResposta)) {
            return false;
        }
        QuestionarioResposta other = (QuestionarioResposta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  id.toString() ;
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
