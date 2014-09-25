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

import br.edu.ifnmg.DomainModel.Log;
import br.edu.ifnmg.DomainModel.Services.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.PessoaRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "logController")
@RequestScoped
public class LogController
        extends ControllerBaseEntidade<Log>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LogController() {
    }

    @EJB
    LogRepositorio dao;

    @EJB
    PessoaRepositorio pessoaDAO;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarLog.xhtml");
        setPaginaListagem("listagemLogs.xtml");
    }

    @Override
    public Log getFiltro() {
        if (filtro == null) {
            filtro = new Log();
            filtro.setUsuario((Pessoa) getSessao("logctrl_usuario", pessoaDAO));
            filtro.setDataEvento(getSessaoData("logctrl_data"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Log filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("logctrl_usuario", filtro.getUsuario());
            setSessao("logctrl_data", filtro.getDataEvento());
        }
    }

    @Override
    public void limpar() {
        setEntidade(new Log());
    }

    public void apagaFiltro() {
        dao.Apaga();
    }

    public void apagaTodos() {
        dao.Apaga();
    }

}
