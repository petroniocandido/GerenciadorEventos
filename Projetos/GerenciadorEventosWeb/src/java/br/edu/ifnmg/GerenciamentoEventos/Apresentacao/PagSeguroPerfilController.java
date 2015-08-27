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

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.PagSeguroPerfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PagSeguroPerfilRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "pagSeguroPerfilController")
@RequestScoped
public class PagSeguroPerfilController
        extends ControllerBaseEntidade<PagSeguroPerfil>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PagSeguroPerfilController() {
    }

    @EJB
    PagSeguroPerfilRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarPagSeguroPerfil.xhtml");
        setPaginaListagem("listagemPagSeguroPerfis.xtml");
    }
        

    @Override
    public PagSeguroPerfil getFiltro() {
        if (filtro == null) {
            filtro = new PagSeguroPerfil();
            filtro.setEmail(getSessao("psctrl_email"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(PagSeguroPerfil filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("psctrl_email", filtro.getEmail());
        }
    }
    
    @Override
    public void filtrar() {
        setFiltro(filtro);
    }

    @Override
    public void limpar() {
        setEntidade(new PagSeguroPerfil());
    }

}
