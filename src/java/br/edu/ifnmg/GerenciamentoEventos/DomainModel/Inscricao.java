/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "inscricoes")
public class Inscricao implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Pessoa pessoa;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Evento evento;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInscricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    QuestionarioResposta resposta;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "DTYPE")
    protected InscricaoTipo tipo;
    
    @Enumerated(EnumType.STRING)
    protected InscricaoCategoria categoria;
    
    @Enumerated(EnumType.STRING)
    protected InscricaoStatus status;
    
    @Lob
    private String observacoes;
    
    private boolean pago;
    
    private boolean compareceu;
    
    private int ordem;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "inscricao")
    private List<InscricaoItem> itens;
    
    @ManyToOne
    private Lancamento lancamento;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "inscricoesarquivos")
    private List<Arquivo> arquivos;
    
    public Inscricao() {
        arquivos = new ArrayList<>();
        itens = new ArrayList<>();
        tipo = InscricaoTipo.Inscricao;
        dataInscricao = new Date();
        categoria = InscricaoCategoria.Normal; 
        status = InscricaoStatus.Criada;
    }
    
    public void cancelar(){
        
        for(InscricaoItem i : itens){
            i.setStatus(InscricaoStatus.Cancelada);
        }
        
        if(lancamento != null)
            lancamento.cancelar(pessoa);
    }
    
    public void recusar(){
        
        for(InscricaoItem i : itens){
            i.setStatus(InscricaoStatus.Recusada);
        }
        
        if(lancamento != null)
            lancamento.cancelar(pessoa);
    }
    
    public void pagar() {
        for(InscricaoItem i : itens){
            i.setPago(true);
            if(i.getStatus() == InscricaoStatus.Criada)
                i.setStatus(InscricaoStatus.Confirmada);
        }
        if(status == InscricaoStatus.Criada)
            setStatus(InscricaoStatus.Confirmada);
    }
    
    
    
    public boolean add(InscricaoItem item){
        item.setInscricao(this);
        if(!itens.contains(item)){
            return itens.add(item);
        } else
            return false;
    }
    
    public boolean remove(InscricaoItem item){
        if(itens.contains(item)){
            itens.remove(item);
            item.setInscricao(null);            
            return true;
        } else
            return false;
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
    
    public InscricaoItem getItem(Atividade a){
        for(InscricaoItem i : getItens()){
            if(i.getAtividade().equals(a))
                return i;
        }
        return null;
    }
    
    public BigDecimal getValorTotal() {
        BigDecimal valor = new BigDecimal("0.00");
        valor = valor.add(this.getEvento().getValorInscricao());
        for(InscricaoItem i : getItens()){
            valor = valor.add(i.getAtividade().getValorInscricao());
        }
        return valor;
    }
    
    public Lancamento criarLancamento(Pessoa p) {
        Lancamento l = new Lancamento();
        l.add(this);
        l.setEvento(evento);
        l.setTipo(LancamentoTipo.Credito);
        String tmp = "";
        for(InscricaoItem i : getItens()){
            tmp += i.getAtividade().getNome() + ",";
        }
        l.setCliente(this.getPessoa());
        l.setCriacao(new Date());
        l.setCriador(p);
        l.setValorOriginal(getValorTotal());
        l.setValorTotal(getValorTotal());
        
        l.setDescricao("Referente pagto inscrição " + id.toString() + " do evento " + this.getEvento().getNome() + " e das atividades " + tmp);
        setLancamento(l);
        return l;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public InscricaoCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(InscricaoCategoria categoria) {
        this.categoria = categoria;
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
        if(pago == true && this.pago == false)
            pagar();
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

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public InscricaoStatus getStatus() {
        return status;
    }

    public void setStatus(InscricaoStatus status) {
        if(status == InscricaoStatus.Cancelada && this.status != null && this.status != InscricaoStatus.Cancelada)
            cancelar();
        
        if(status == InscricaoStatus.Recusada && this.status != null && this.status != InscricaoStatus.Recusada)
            recusar();
        
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.pessoa);
        hash = 23 * hash + Objects.hashCode(this.evento);
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
        final Inscricao other = (Inscricao) obj;
        if (!Objects.equals(this.pessoa, other.pessoa)) {
            return false;
        }
        if (!Objects.equals(this.evento, other.evento)) {
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
