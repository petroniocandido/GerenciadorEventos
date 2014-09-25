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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "permissaoController")
@RequestScoped
public class PermissaoController
        extends ControllerBaseEntidade<Permissao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PermissaoController() {
    }

    @EJB
    PermissaoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaListagem("listagemPermissoes.xtml");
        setPaginaEdicao("editarPermissao.xhtml");
    }

    @Override
    public Permissao getFiltro() {
        if (filtro == null) {
            filtro = new Permissao();
            filtro.setUri(getSessao("filtro_uri"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Permissao filtro) {
        this.filtro = filtro;
        if(filtro != null)
            setSessao("filtro_uri", filtro.getUri());
    }

    @Override
    public void limpar() {
        setEntidade(new Permissao());
    }

    @Override
    public List<Permissao> getListagemGeral() {
        return dao.Buscar(new Permissao());
    }

}
