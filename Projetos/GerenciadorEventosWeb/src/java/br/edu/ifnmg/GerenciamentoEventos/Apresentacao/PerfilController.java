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

import br.edu.ifnmg.DomainModel.Perfil;
import br.edu.ifnmg.DomainModel.Permissao;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.PessoaRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author petronio
 */
@Named(value = "perfilController")
@RequestScoped
public class PerfilController
        extends ControllerBaseEntidade<Perfil>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PerfilController() {
    }

    @EJB
    PerfilRepositorio dao;

    @EJB
    PessoaRepositorio daoP;

    Permissao permissao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarPerfil.xhtml");
        setPaginaListagem("listagemPerfis.xtml");
    }

    @Override
    public Perfil getFiltro() {
        if (filtro == null) {
            filtro = new Perfil();
            filtro.setNome(getSessao("pflctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Perfil filtro) {
        this.filtro = filtro;
        if(filtro != null)
            setSessao("pflctrl_nome", filtro.getNome());

    }

    @Override
    public void limpar() {
        setEntidade(new Perfil());
    }

    public void valueChangeListener(ValueChangeEvent evt) {
        setEntidade(dao.Refresh(getEntidade()));
        if ((boolean) evt.getNewValue()) {
            entidade.add(permissao);
        } else {
            entidade.remove(permissao);
        }
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public List<Pessoa> getPessoas() {
        return daoP.IgualA("perfil", entidade).Buscar();
    }

}
