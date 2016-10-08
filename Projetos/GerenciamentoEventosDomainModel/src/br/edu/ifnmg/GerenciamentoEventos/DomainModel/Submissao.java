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
import javax.persistence.Transient;
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
    
    @Lob()
    private String resumo;
    
    @ElementCollection
    @CollectionTable(name = "submissoes_palavraschave",
            joinColumns = @JoinColumn(name = "submissao"))
    @Column(name = "palavrachave")
    private List<String> palavraschave;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissaoAvaliacao> avaliacoes;
    
    @Column(nullable = false)
    private String autor1;
    
    @Column(nullable = true)
    private String autor2;
    
    @Column(nullable = true)
    private String autor3;
    
    @Column(nullable = true)
    private String autor4;
    
    @Column(nullable = true)
    private String autor5;
    
    private double total;
    
    @Enumerated()
    private SubmissaoStatus status;
    
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = AreaConhecimento.class)
    private List<AreaConhecimento> areasConhecimento;
    
    @ManyToMany(targetEntity = Pessoa.class)
    private List<Pessoa> avaliadores;

    public Submissao() {
        this.palavraschave = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.areasConhecimento = new ArrayList<>();
        this.avaliadores = new ArrayList<>();
        this.status = SubmissaoStatus.EmEdicao;
        this.autor3 = "";
        this.autor4 = "";
        this.autor5 = "";
    }
    
    public void add(Pessoa avaliador) {
        if(avaliador == null) return;
        if (!avaliadores.contains(avaliador)) {
            avaliadores.add(avaliador);
        }
    }

    public void remove(Pessoa avaliador) {
        if(avaliador == null) return;
        if (avaliadores.contains(avaliador)) {
            avaliadores.remove(avaliador);
        }
    }
    
    public void add(SubmissaoAvaliacao avaliacao) {
        if(getStatus() != SubmissaoStatus.Atribuido && getStatus() != SubmissaoStatus.Avaliado)
            return;
        
        if (!avaliacoes.contains(avaliacao)) {
            avaliacao.setSubmissao(this);
            avaliacoes.add(avaliacao);
            if(getStatus() == SubmissaoStatus.Atribuido)
                setStatus(SubmissaoStatus.Avaliado);                        
        }
    }

    public void remove(SubmissaoAvaliacao avaliacao) {
        if(getStatus() != SubmissaoStatus.Avaliado)
            return;
        
        if (avaliacoes.contains(avaliacao)) {
            avaliacoes.remove(avaliacao);
        }
        
        if(avaliacoes.isEmpty())
            setStatus(SubmissaoStatus.Pendente);
        
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
    
    public void add(String a) {
        if(a == null) return;
        if (!palavraschave.contains(a)) {
            palavraschave.add(a);
        }
    }

    public void remove(String a) {
        if(a == null) return;
        if (palavraschave.contains(a)) {
            palavraschave.remove(a);
        }
    }
    
    public boolean hasPalavrasChave() {
        return ! this.palavraschave.isEmpty();
    }
    
    public boolean hasAreaConhecimento() {
        return ! this.areasConhecimento.isEmpty();
    }
    
    public boolean hasMinimoDeAutores() {
        return countAutores() >=  ((InscricaoItem)getInscricao()).getAtividade().getQuantidadeAutores();
    }
    
    public boolean hasMinimoDeArquivos() {
        return countArquivos()>=  ((InscricaoItem)getInscricao()).getAtividade().getQuantidadeArquivos();
    }
    
    public boolean pendente() {
        if(getStatus() == SubmissaoStatus.EmEdicao && hasPalavrasChave() && hasAreaConhecimento() && hasMinimoDeAutores() && hasMinimoDeArquivos()){
            setStatus(SubmissaoStatus.Pendente);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isEmEdicao() {
        return getStatus() == SubmissaoStatus.EmEdicao;
    }
    
    public boolean isEmAvaliacao() {
        return getStatus() == SubmissaoStatus.Atribuido;
    }
    
    public boolean isProntoParaCertificado() {
        return getStatus() == SubmissaoStatus.Aprovado;
    }
    
    public int countAutores() {
        int tmp = 0;
        if(autor1 != null && !autor1.isEmpty()) tmp++;
        if(autor2 != null && !autor2.isEmpty()) tmp++;
        if(autor3 != null && !autor3.isEmpty()) tmp++;
        if(autor4 != null && !autor4.isEmpty()) tmp++;
        if(autor5 != null && !autor5.isEmpty()) tmp++;
        return tmp;
    }
    
    public int countArquivos() {
        int tmp = 0;
        if(arquivo1 != null ) tmp++;
        if(arquivo2 != null ) tmp++;
        if(arquivo3 != null ) tmp++;
        if(arquivo4 != null ) tmp++;
        return tmp;
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

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String descricao) {
        this.resumo = descricao;
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

    public List<Pessoa> getAvaliadores() {
        return avaliadores;
    }

    public void setAvaliadores(List<Pessoa> avaliadores) {
        this.avaliadores = avaliadores;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public void verificarStatus(){
        switch(getStatus()){
            case EmEdicao:
                if(!((InscricaoItem)getInscricao()).getAtividade().isInscricaoAberto())
                      setStatus(SubmissaoStatus.Cancelado);
                break;
                
            case Avaliado:
                concluirAvaliacao();
                break;
                
        }
    }
    
    private void concluirAvaliacao() {
        boolean aprovado = false;
        boolean reprovado = false;
        boolean desclassificado = false;
        
        int valores = 0;
        int count = 0;
        
        for(SubmissaoAvaliacao aval : getAvaliacoes()){
            if(aval.getStatus() == SubmissaoStatus.Aprovado){
                aprovado = true;
            }
            else if(aval.getStatus() == SubmissaoStatus.Reprovado)
                reprovado = true;
            else if(aval.getStatus() == SubmissaoStatus.Desclassificado)
                desclassificado = true;
            if(getResposta() != null){
                count = count + 1;
                valores = valores + getResposta().getTotal();
            }
        }
        if(aprovado && !reprovado && !desclassificado){
            setStatus(SubmissaoStatus.Aprovado);
            getInscricao().setStatus(InscricaoStatus.Confirmada);
        } else if(!aprovado && reprovado && !desclassificado){
            setStatus(SubmissaoStatus.Reprovado);
            getInscricao().setStatus(InscricaoStatus.Recusada);
        } else if(!aprovado && !reprovado && desclassificado){
            setStatus(SubmissaoStatus.Desclassificado);
            getInscricao().setStatus(InscricaoStatus.Cancelada);
        } else {
            setStatus(SubmissaoStatus.Divergencia);
        }
        
        if(count>0)
            setTotal(valores/count);
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
    
    @Transient
    String pl = null;
    public String palavrasChave(){
        if(pl == null){
            pl = "";
            for(String p : getPalavraschave()){
                if(!pl.isEmpty()) pl = pl + ",";
                pl = pl + p;
            }
        }
        return pl;
    }
    
    @Transient
    String ac = null;
    public String areasConhecimento(){
        if(ac == null){
            ac = "";
            for(AreaConhecimento a : getAreasConhecimento()){
                if(!ac.isEmpty()) ac = ac + ",";
                ac = ac + a.toString();
            }
        }
        return ac;
    }
    
    @Transient
    String au = null;
    public String autores(){
        if(au == null){
            au = "";
            if(this.autor1 != null && !this.autor1.isEmpty())
                au = this.autor1;
            if(this.autor2 != null && !this.autor2.isEmpty())
                au = au + "," + this.autor2;
            if(this.autor3 != null && !this.autor3.isEmpty())
                au = au + "," + this.autor3;
            if(this.autor4 != null && !this.autor4.isEmpty())
                au = au + "," + this.autor4;
            if(this.autor5 != null && !this.autor4.isEmpty())
                au = au + "," + this.autor5;
        }
        return au;
    }
    
    public String getAutores() {
        return this.autores();
    }

    @Override
    public String toString() {
        return (titulo != null ? titulo + "(": "(") + id.toString() + ")";
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
