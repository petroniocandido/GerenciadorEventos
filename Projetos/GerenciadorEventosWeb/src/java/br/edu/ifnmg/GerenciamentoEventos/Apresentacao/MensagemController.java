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

import br.edu.ifnmg.DomainModel.Mensagem;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.DomainModel.Services.MensagemRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "mensagemController")
@RequestScoped
public class MensagemController
        extends ControllerBaseEntidade<Mensagem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public MensagemController() {
    }

    @EJB
    MensagemRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarMensagem.xhtml");
        setPaginaListagem("listagemMensagens.xtml");
    }

    @Override
    public Mensagem getFiltro() {
        if (filtro == null) {
            filtro = new Mensagem();
            filtro.setAssunto(getSessao("menctrl_assunto"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Mensagem filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("menctrl_assunto", filtro.getAssunto());
        }
    }

    @Override
    public void limpar() {
        setEntidade(new Mensagem());
    }

    public void apagaFiltro() {
        dao.Apaga();
    }

    public void apagaTodos() {
        dao.Apaga();
    }

}
