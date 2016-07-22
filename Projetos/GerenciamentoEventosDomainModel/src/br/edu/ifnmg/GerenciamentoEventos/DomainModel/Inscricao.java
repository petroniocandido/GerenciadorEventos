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
import java.util.Objects;
import javax.persistence.Cacheable;
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
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "inscricoes")
@Cacheable(false)
public class Inscricao implements Entidade, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private EventoInscricaoCategoria eventoInscricaoCategoria;

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

    @ManyToOne(cascade = CascadeType.ALL)
    private Lancamento lancamento;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "inscricoesarquivos")
    private List<Arquivo> arquivos;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submissao> submissoes;

    public Inscricao() {
        submissoes = new ArrayList<>();
        arquivos = new ArrayList<>();
        itens = new ArrayList<>();
        tipo = InscricaoTipo.Inscricao;
        dataInscricao = new Date();
        categoria = InscricaoCategoria.Normal;
        status = InscricaoStatus.Criada;
        this.pago = false;
    }

    public boolean isPendente() {
        return (getEvento().requerPagamento() && status == InscricaoStatus.Criada) || status == InscricaoStatus.Aceita;
    }
    
    public boolean isNaoPago() {
        return isPendente() 
                && getEvento().requerPagamento() && !pago 
                && (getLancamento() == null 
                    || getLancamento().getStatus() != LancamentoStatus.Baixado);
    }

    public boolean isCancelada() {
        return status == InscricaoStatus.Cancelada || status == InscricaoStatus.Recusada;
    }

    public void cancelar() {

        for (InscricaoItem i : itens) {
            i.setStatus(InscricaoStatus.Cancelada);
        }

        if (lancamento != null) {
            lancamento.cancelar(pessoa);
        }
    }

    public void recusar() {

        for (InscricaoItem i : itens) {
            i.setStatus(InscricaoStatus.Recusada);
        }

        if (lancamento != null) {
            lancamento.cancelar(pessoa);
        }
    }

    public Lancamento pagar(Pessoa operador) {
        if (pago) {
            return getLancamento();
        }

        setDataPagamento(new Date());
        setPago(true);
        setStatus(InscricaoStatus.Confirmada);

        Lancamento l = getLancamento();

        if (l == null) {
            l = criarLancamento(operador);
            setLancamento(l);
        }
        
        l.baixar(operador);

        for (InscricaoItem i : itens) {
            i.setPago(true);
            if (i.getStatus() == InscricaoStatus.Criada) {
                i.setStatus(InscricaoStatus.Confirmada);
            }
        }
        if (status == InscricaoStatus.Criada) {
            setStatus(InscricaoStatus.Confirmada);
        }

        return l;
    }

    public void pagar(Lancamento l, Pessoa operador) {
        setLancamento(l);
        setDataPagamento(new Date());
        setPago(true);
        setStatus(InscricaoStatus.Confirmada);

        for (InscricaoItem i : itens) {
            i.setPago(true);
            if (i.getStatus() == InscricaoStatus.Criada) {
                i.setStatus(InscricaoStatus.Confirmada);
            }
        }
        if (status == InscricaoStatus.Criada) {
            setStatus(InscricaoStatus.Confirmada);
        }

    }

    public boolean add(InscricaoItem item) {
        item.setInscricao(this);
        if (!itens.contains(item)) {
            return itens.add(item);
        } else {
            return false;
        }
    }

    public boolean remove(InscricaoItem item) {
        if (itens.contains(item)) {
            itens.remove(item);
            item.setInscricao(null);
            return true;
        } else {
            return false;
        }
    }

    public void add(Arquivo arquivo) {
        if (!arquivos.contains(arquivo)) {
            arquivos.add(arquivo);
        }
    }

    public void remove(Arquivo arquivo) {
        if (arquivos.contains(arquivo)) {
            arquivos.remove(arquivo);
        }
    }
    
    public boolean add(Submissao item) {
        if (!submissoes.contains(item)) {
            return submissoes.add(item);
        } else {
            return false;
        }
    }

    public boolean remove(Submissao item) {
        if (submissoes.contains(item)) {
            submissoes.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public InscricaoItem getItem(Atividade a) {
        for (InscricaoItem i : getItens()) {
            if (i.getAtividade().equals(a)) {
                return i;
            }
        }
        return null;
    }

    public BigDecimal getValorTotal() {
        BigDecimal valor = new BigDecimal("0.00");
        
        if(getEventoInscricaoCategoria() != null)
            valor = valor.add(this.getEventoInscricaoCategoria().getValorInscricao());
        else
            valor = valor.add(this.getEvento().getValorInscricao());
        
        for (InscricaoItem i : getItens()) {
            if (i.getStatus() != InscricaoStatus.Cancelada && i.getStatus() != InscricaoStatus.Recusada
                    && i.getCategoria() == InscricaoCategoria.Normal) {
                valor = valor.add(i.getAtividade().getValorInscricao());
            }
        }
        return valor;
    }

    public Lancamento criarLancamento(Pessoa p) {
        Lancamento l = new Lancamento();
        l.add(this);
        l.setEvento(evento);
        l.setTipo(LancamentoTipo.Credito);
        String tmp = "";
        for (InscricaoItem i : getItens()) {
            tmp += (tmp.length() > 0) ? "," : "";
            tmp += i.getAtividade().getNome();
        }
        l.setCliente(this.getPessoa());
        l.setCriacao(new Date());
        l.setCriador(p);
        l.setValorOriginal(getValorTotal());
        l.setValorTotal(getValorTotal());
        String texto = "pagamento da inscrição " + id.toString();
        if (tmp.length() > 0) {
            texto = texto + " e inscrição nas atividades " + tmp;
        }
        l.setDescricao(texto);
        setLancamento(l);
        setPago(false);
        return l;
    }
    
    public Submissao criarSubmissao() {
        Submissao submissao = new Submissao();
        submissao.setInscricao(this);
        submissao.setResposta(resposta);
        this.add(submissao);
        return submissao;
    }

    @Transient
    protected Boolean prontoParaCertificado = null;

    public boolean isProntoParaCertificado() {
        if (prontoParaCertificado == null) {
            
            prontoParaCertificado = getEvento().requerPagamento() ?  isPago() : true;
            
            prontoParaCertificado = prontoParaCertificado && getEvento().getStatus() == Status.Concluido && isCompareceu();
        }

        return prontoParaCertificado;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<Submissao> getSubmissoes() {
        return submissoes;
    }

    public void setSubmissoes(List<Submissao> submissoes) {
        this.submissoes = submissoes;
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
        if (status == InscricaoStatus.Cancelada && this.status != null && this.status != InscricaoStatus.Cancelada) {
            cancelar();
        }

        if (status == InscricaoStatus.Recusada && this.status != null && this.status != InscricaoStatus.Recusada) {
            recusar();
        }

        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public EventoInscricaoCategoria getEventoInscricaoCategoria() {
        return eventoInscricaoCategoria;
    }

    public void setEventoInscricaoCategoria(EventoInscricaoCategoria eventoInscricaoCategoria) {
        this.eventoInscricaoCategoria = eventoInscricaoCategoria;
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
