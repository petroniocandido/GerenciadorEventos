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
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.DomainModel.Services.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author petronio
 */
@Named(value = "lancamentoController")
@RequestScoped
public class LancamentoController
        extends ControllerBaseEntidade<Lancamento>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LancamentoController() {
    }
    
    @EJB
    LancamentoRepositorio dao;

    @EJB
    InscricaoRepositorio daoInsc;

    @EJB
    PessoaRepositorioLocal daoPessoa;
    
    @EJB
    EventoRepositorio daoEvt;

    Inscricao inscricao;
    
    Date inicio;
    
    Date termino;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarLancamento.xhtml");
        setPaginaListagem("listagemLancamentos.xtml");
        checaEventoPadrao();
        //inicio = new Date();
        //termino = new Date();
    }
    
     public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null ) {
            Evento padrao = daoEvt.Abrir(Long.parseLong(evt));
            getFiltro().setEvento(padrao);
        }
    }

    @Override
    public Lancamento getFiltro() {
        if (filtro == null) {
            filtro = new Lancamento();
            filtro.setCliente((Pessoa) getSessao("lcctrl_cliente", daoPessoa));
            filtro.setBaixa(getSessaoData("lcctrl_baixa"));
            String tmp = getSessao("lcctrl_tipo");
            filtro.setStatus((tmp != null) ? LancamentoStatus.valueOf(getSessao("lcctrl_tipo")) : null);
        }
        return filtro;
    }

    @Override
    public void setFiltro(Lancamento filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("lcctrl_cliente", filtro.getCliente());
            setSessao("lcctrl_baixa", filtro.getBaixa());
            setSessao("lcctrl_tipo", filtro.getStatus() != null ? filtro.getStatus().name() : null);
        }
    }
    
    @Override
    public void filtrar() {
        checaEventoPadrao();
        setFiltro(filtro);
    }

    @Override
    public void limpar() {
        setEntidade(new Lancamento());
    }

    public LancamentoStatus[] getStatus() {
        return LancamentoStatus.values();
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;        
    }

    public void addInscricao() {
        entidade = dao.Refresh(getEntidade());
        entidade.add(getInscricao());
        if(dao.Salvar(entidade)){
            Mensagem("", "");
        } else {
            Mensagem("", "");
        }
        setInscricao(new Inscricao());
    }

    public void removeInscricao() {
        entidade = dao.Refresh(getEntidade());
        entidade.remove(getInscricao());
        dao.Salvar(entidade);
        setInscricao(new Inscricao());
    }

    public void baixarLancamento() {
        entidade = dao.Refresh(getEntidade());
        Rastrear(entidade);
        entidade.baixar(getUsuarioCorrente());
        if (dao.Salvar(entidade)) {
            Mensagem("Sucesso", "Lançamento baixado com sucesso!");
        } else {
            MensagemErro("Falha", "Lançamento não foi baixado! Consulte o administrador");
        }
    }

    public void cancelarLancamento() {
        entidade = dao.Refresh(getEntidade());
        Rastrear(entidade);
        entidade.cancelar(getUsuarioCorrente());
        if (dao.Salvar(entidade)) {
            Mensagem("Sucesso", "Lançamento cancelado com sucesso!");
        } else {
            MensagemErro("Falha", "Lançamento não foi cancelado! Consulte o administrador");
        }
    }

    public boolean isEditavel() {
        return getEntidade().editavel();
    }

    public String getCorStatus(LancamentoStatus s) {
        if (s == null) {
            return "white";
        }

        switch (s) {
            case Aberto:
                return "white";
            case Baixado:
                return "green";
            case Cancelado:
                return "gray";
            default:
                return "white";
        }
    }

    public List<LancamentoCategoria> getCategorias() {
        return dao.BuscarCategorias(null);
    }

    public LancamentoTipo[] getTipos() {
        return LancamentoTipo.values();
    }
    
    @Override
    public List<Lancamento> getListagem() {
        return dao
                .IgualA("evento", filtro.getEvento())
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("status", filtro.getStatus())
                .IgualA("tipo", filtro.getTipo())
                .IgualA("categoria", filtro.getCategoria())
                .IgualA("cliente", filtro.getCliente())
                .IgualA("usuarioBaixa", filtro.getUsuarioBaixa())
                .MaiorOuIgualA("criacao", inicio)
                .MenorOuIgualA("criacao", termino)
                .Buscar();
    }
    
     public void validaValorOriginal(FacesContext context, UIComponent component, Object value) throws ValidatorException {

         BigDecimal valor = (BigDecimal)value;
        
        if (valor.compareTo(new BigDecimal("0.00")) == 0 ) {
            FacesMessage msg
                    = new FacesMessage("O valor inicial deve ser maior do que R$ 0,00!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public Date getInicio() {
        inicio = getSessaoData("lcctrl_inicio");
        if(inicio == null)
            inicio = new Date();            
        
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
        setSessao("lcctrl_inicio", inicio);
    }

    public Date getTermino() {
        termino = getSessaoData("lcctrl_termino");
        if(termino == null)
            termino = new Date();                    
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
        setSessao("lcctrl_termino", termino);
    }
     
     

}
