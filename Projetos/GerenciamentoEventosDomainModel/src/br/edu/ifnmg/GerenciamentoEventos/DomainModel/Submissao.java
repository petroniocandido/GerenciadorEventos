/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Arquivo;
import br.edu.ifnmg.DomainModel.Entidade;
import br.edu.ifnmg.DomainModel.Pessoa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Cacheable(false)
@Entity
@Table(name="submissoes")
public class Submissao implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Inscricao inscricao;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    QuestionarioResposta resposta;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Arquivo arquivo1;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Arquivo arquivo2;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Arquivo arquivo3;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Arquivo arquivo4;
    
    @Column(nullable = false)
    private String titulo;
    
    @Lob
    private String descricao;
    
    @ElementCollection
    @CollectionTable(name = "submissoespalavraschave",
            joinColumns = @JoinColumn(name = "submissao"))
    @Column(name = "palavrachave")
    private List<String> palavraschave;
    
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SubmissaoAvaliacao> avaliacoes;
    
    @Column(nullable = false)
    private String autor1;
    
    @Column(nullable = false)
    private String autor2;
    
    @Column(nullable = false)
    private String autor3;
    
    @Column(nullable = false)
    private String autor4;
    
    @Column(nullable = false)
    private String autor5;
    
    @Enumerated()
    private SubmissaoStatus status;
    
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = AreaConhecimento.class)
    private List<AreaConhecimento> areasConhecimento;

    public Submissao() {
        this.areasConhecimento = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.status = SubmissaoStatus.Pendente;
    }
     
    public void add(SubmissaoAvaliacao avaliacao) {
        if (!avaliacoes.contains(avaliacao)) {
            avaliacoes.add(avaliacao);
        }
    }

    public void remove(SubmissaoAvaliacao avaliacao) {
        if (avaliacoes.contains(avaliacao)) {
            avaliacoes.remove(avaliacao);
        }
    }
    
    public void add(AreaConhecimento a) {
        if(a == null) return;
        if (!areasConhecimento.contains(a)) {
            areasConhecimento.add(a);
        }
    }

    public void remove(AreaConhecimento a) {
        if(a == null) return;
        if (areasConhecimento.contains(a)) {
            areasConhecimento.remove(a);
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

    public List<SubmissaoAvaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<SubmissaoAvaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public List<AreaConhecimento> getAreasConhecimento() {
        return areasConhecimento;
    }

    public void setAreasConhecimento(List<AreaConhecimento> areasConhecimento) {
        this.areasConhecimento = areasConhecimento;
    }
    
    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public QuestionarioResposta getResposta() {
        return resposta;
    }

    public void setResposta(QuestionarioResposta resposta) {
        this.resposta = resposta;
    }

    public Arquivo getArquivo1() {
        return arquivo1;
    }

    public void setArquivo1(Arquivo arquivo1) {
        this.arquivo1 = arquivo1;
    }

    public Arquivo getArquivo2() {
        return arquivo2;
    }

    public void setArquivo2(Arquivo arquivo2) {
        this.arquivo2 = arquivo2;
    }

    public Arquivo getArquivo3() {
        return arquivo3;
    }

    public void setArquivo3(Arquivo arquivo3) {
        this.arquivo3 = arquivo3;
    }

    public Arquivo getArquivo4() {
        return arquivo4;
    }

    public void setArquivo4(Arquivo arquivo4) {
        this.arquivo4 = arquivo4;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getPalavraschave() {
        return palavraschave;
    }

    public void setPalavraschave(List<String> palavraschave) {
        this.palavraschave = palavraschave;
    }

    public String getAutor1() {
        return autor1;
    }

    public void setAutor1(String autor1) {
        this.autor1 = autor1;
    }

    public String getAutor2() {
        return autor2;
    }

    public void setAutor2(String autor2) {
        this.autor2 = autor2;
    }

    public String getAutor3() {
        return autor3;
    }

    public void setAutor3(String autor3) {
        this.autor3 = autor3;
    }

    public String getAutor4() {
        return autor4;
    }

    public void setAutor4(String autor4) {
        this.autor4 = autor4;
    }

    public String getAutor5() {
        return autor5;
    }

    public void setAutor5(String autor5) {
        this.autor5 = autor5;
    }

    public SubmissaoStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissaoStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.inscricao);
        hash = 73 * hash + Objects.hashCode(this.titulo);
        hash = 73 * hash + Objects.hashCode(this.autor1);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Submissao other = (Submissao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.inscricao, other.inscricao)) {
            return false;
        }
        if (!Objects.equals(this.titulo, other.titulo)) {
            return false;
        }
        if (!Objects.equals(this.autor1, other.autor1)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return id.toString();
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
