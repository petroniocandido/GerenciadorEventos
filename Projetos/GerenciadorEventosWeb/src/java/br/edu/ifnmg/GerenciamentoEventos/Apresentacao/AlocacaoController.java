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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AlocacaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Alocacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "alocacaoController")
@RequestScoped
public class AlocacaoController
        extends ControllerBaseEntidade<Alocacao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AlocacaoController() {
    }

    Evento padrao;

    @EJB
    AlocacaoRepositorio dao;

    @EJB
    EventoRepositorio evtDAO;

    @EJB
    PessoaRepositorioLocal pessoaDAO;

    @EJB
    RecursoRepositorio recursoDAO;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
        setPaginaEdicao("editarAlocacao.xhtml");
        setPaginaListagem("listagemAlocacoes.xtml");
    }

    @Override
    public Alocacao getFiltro() {
        if (filtro == null) {
            filtro = new Alocacao();
            filtro.setResponsavel((Pessoa) getSessao("alcctrl_responsavel", pessoaDAO));
            filtro.setRecurso((Recurso) getSessao("alcctrl_recurso", recursoDAO));
            filtro.setInicio(getSessaoData("alcctrl_data"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Alocacao filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("alcctrl_responsavel", filtro.getResponsavel());
            setSessao("alcctrl_recurso", filtro.getRecurso());
            setSessao("alcctrl_data", filtro.getInicio());
        }
    }

    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null && padrao == null) {
            padrao = evtDAO.Abrir(Long.parseLong(evt));
            if (getEntidade().getEvento() == null) {
                getEntidade().setEvento(padrao);
            }
            if (getFiltro().getEvento() == null) {
                getFiltro().setEvento(padrao);
            }
        }
    }

    @Override
    public void filtrar() {
        checaEventoPadrao();
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new Alocacao());
    }

    public AlocacaoStatus[] getStatus() {
        return AlocacaoStatus.values();
    }

    public void concluirItem() {
        getEntidade().setStatus(AlocacaoStatus.Concluido);
        if (dao.Salvar(entidade)) {
            Mensagem("Confirmação", "Alocação concluída!");
        } else {
            AppendLog("Erro ao concluir alocação: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao concluir alocação! Consulte o administrador do sistema!");
        }
    }

    public void cancelarItem() {
        getEntidade().setStatus(AlocacaoStatus.Cancelado);
        if (dao.Salvar(entidade)) {
            Mensagem("Confirmação", "Alocação cancelada!");
        } else {
            AppendLog("Erro ao cancelar alocação: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao cancelar alocação! Consulte o administrador do sistema!");
        }
    }
}
