/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Entity
@Table(name = "questoesrespostas")
public class QuestaoResposta implements Serializable, Entidade {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private QuestionarioResposta resposta;
    
    @ManyToOne
    private Questao questao;
    
    @ManyToOne
    private Arquivo arquivo;
    
    @Lob
    @Column(name = "valor")
    private String valor;

    public QuestaoResposta() {
        this.id = 0L;
        this.valor = "";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public QuestionarioResposta getResposta() {
        return resposta;
    }

    public void setResposta(QuestionarioResposta avaliacao) {
        this.resposta = avaliacao;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getInteiro() {
        if (valor != null && valor.length() > 0) {
            return Integer.parseInt(valor);
        } else {
            return 0;
        }
    }

    public void setInteiro(int valor) {
        this.valor = Integer.toString(valor);
    }
    
    public Date getData() throws ParseException {
        if (valor != null && valor.length() > 0) {
            DateFormat df = DateFormat.getDateInstance();
            return df.parse(valor);
        } else {
            return new Date();
        }
    }

    public void setData(Date valor) {
        DateFormat df = DateFormat.getDateInstance();
        this.valor = df.format(valor);
    }

    public Long getLongo() {
        if (valor != null && valor.length() > 0) {
            return Long.parseLong(valor);
        } else {
            return 0L;
        }
    }

    public void setLongo(Long valor) {
        this.valor = Long.toString(valor);
    }

    public boolean getBooleano() {
        if (valor != null && valor.length() > 0) {
            return Boolean.parseBoolean(valor);
        } else {
            return false;
        }
    }

    public void setBooleano(boolean valor) {
        this.valor = Boolean.toString(valor);
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? this.getResposta().hashCode() * this.getQuestao().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestaoResposta)) {
            return false;
        }
        QuestaoResposta other = (QuestaoResposta) object;
        if (//(this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)) || 
                this.getResposta() != other.getResposta() || this.getQuestao() != other.getQuestao()) {
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
