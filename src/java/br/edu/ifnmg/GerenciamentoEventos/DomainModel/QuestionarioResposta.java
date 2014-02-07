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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "questionariosrespostas")
public class QuestionarioResposta implements Serializable, Entidade {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
    @JoinColumn(name = "idPessoa", referencedColumnName = "idPessoa", updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa pessoa;
   
    @JoinColumn(name = "idQuestionario", referencedColumnName = "idQuestionario", updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Questionario questionario;
    
    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy="avaliacao")
    private List<QuestaoResposta> respostas;
    
    @Column(name="identidade")
    private Long chave;

    @Transient
    private Map<Questao,QuestaoResposta> maps;

    public QuestionarioResposta(){
        this.id = 0L;
        this.respostas = new LinkedList<QuestaoResposta>();
        this.maps = new HashMap<Questao, QuestaoResposta>();
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
        if(respostas == null){
            respostas = new LinkedList<QuestaoResposta>();
        }
        m.setAvaliacao(this);
        if(!respostas.contains(m)){
            respostas.add(m);
        }
    }
    
    public void remove(QuestaoResposta m){
        if(respostas == null){
            respostas = new LinkedList<QuestaoResposta>();
        }
        if(respostas.contains(m)){
            respostas.remove(m);
            m.setAvaliacao(null);
        }
    }
    
    public QuestaoResposta RespostaDeQuestao(Questao q){        
        if(maps.containsKey(q)){
            return maps.get(q);
        }
        for(QuestaoResposta r : respostas){
            if(r.getQuestao().equals(q)){
                maps.put(q, r);
                return r;
            }
        }
        return null;
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

    public Long getChave() {
        return chave;
    }

    public void setChave(Long chave) {
        this.chave = chave;
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
        return "br.edu.ifnmg.LevantamentoDados.DomainModel.Avaliacao[ avaliacaoPK=" + id + " ]";
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