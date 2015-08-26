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

import br.edu.ifnmg.DomainModel.MensagemPerfil;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.DomainModel.Services.MensagemPerfilRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "mensagemPerfilController")
@RequestScoped
public class MensagemPerfilController
        extends ControllerBaseEntidade<MensagemPerfil>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public MensagemPerfilController() {
    }

    @EJB
    MensagemPerfilRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarMensagemPerfil.xhtml");
        setPaginaListagem("listagemMensagensPerfis.xtml");
    }
        

    @Override
    public MensagemPerfil getFiltro() {
        if (filtro == null) {
            filtro = new MensagemPerfil();
            filtro.setNome(getSessao("mpctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(MensagemPerfil filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("mpctrl_nome", filtro.getNome());
        }
    }
    
    @Override
    public void filtrar() {
        setFiltro(filtro);
    }

    @Override
    public void limpar() {
        setEntidade(new MensagemPerfil());
    }

}
