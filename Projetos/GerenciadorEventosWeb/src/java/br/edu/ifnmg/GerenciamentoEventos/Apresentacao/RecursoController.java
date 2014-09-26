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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.RecursoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "recursoController")
@RequestScoped
public class RecursoController
        extends ControllerBaseEntidade<Recurso>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public RecursoController() {
    }

    @EJB
    RecursoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaListagem("listagemRecursos.xtml");
        setPaginaEdicao("editarRecurso.xhtml");
    }

    @Override
    public Recurso getFiltro() {
        if (filtro == null) {
            filtro = new Recurso();
            filtro.setNome(getSessao("rcsctrl_nome"));
            String tmp = getSessao("rcsctrl_tipo");
            filtro.setTipo((tmp != null) ? RecursoTipo.valueOf(getSessao("rcsctrl_tipo")) : null);
        }
        return filtro;
    }

    @Override
    public void setFiltro(Recurso filtro) {
        this.filtro = filtro;
        if(filtro != null){
            setSessao("rcsctrl_nome", filtro.getNome());
            setSessao("rcsctrl_tipo", filtro.getTipo() != null ? filtro.getTipo().name() : null);
        }

    }

    @Override
    public void limpar() {

        setEntidade(new Recurso());
    }

    public RecursoTipo[] getTipos() {
        return RecursoTipo.values();
    }

    public AlocacaoStatus[] getStatus() {
        return AlocacaoStatus.values();
    }

}
