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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "questionarioController")
@RequestScoped
public class QuestionarioController
        extends ControllerBaseEntidade<Questionario>
        implements Serializable {

    /**
     * Creates a new instance of ClienteBean
     */
    public QuestionarioController() {
        
    }
    
    @EJB
    QuestionarioRepositorio dao;
    
    QuestionarioSecao secao;
    
    Questao questao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarQuestionario.xhtml");
        setPaginaListagem("listagemQuestionarios.xtml");        
        questao = new Questao();
        secao = new QuestionarioSecao();
    }
    
    @Override
    public Questionario getFiltro() {
        if (filtro == null) {
            filtro = new Questionario();
            filtro.setTitulo(getSessao("qstctrl_titulo"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Questionario filtro) {
        this.filtro = filtro;
        if(filtro != null)
            setSessao("qstctrl_titulo", filtro.getTitulo());
    }

    @Override
    public void limpar() {
        setEntidade(new Questionario());
        setQuestao(new Questao());
        setSecao(new QuestionarioSecao());
    }


    public void addSecao() {
        Refresh();
        getEntidade().add(secao);
        SalvarAgregado(secao);
        novaSecao();
    }
    
    private void novaSecao() {
        setSecao(new QuestionarioSecao());
        if(entidade != null)
            getSecao().setOrdem(entidade.getSecoes().size()+1);
    }

    public void removeSecao() {
        Refresh();
        getEntidade().remove(secao);
        RemoverAgregado(secao);
        novaSecao();
    }

    public void addQuestao() {
        Refresh();
        getEntidade().add(questao);
        SalvarAgregado(questao);
        novaQuestao();
    }
    
    private void novaQuestao() {
        setQuestao(new Questao());
        if(entidade != null)
            getQuestao().setOrdem(entidade.getQuestoes().size()+1);
    }
    
    public void editarQuestao() {
        Refresh();
        getEntidade().remove(questao);
        dao.Salvar(entidade);        
    }

    public void removeQuestao() {
        Refresh();
        getEntidade().remove(questao);
        RemoverAgregado(questao);
        novaQuestao();
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
        //setSecao(questao.getSecao());
        //questao.get
    }

    public QuestionarioSecao getSecao() {
        return secao;
    }

    public void setSecao(QuestionarioSecao secao) {
        this.secao = secao;
    }

    public QuestaoTipo[] getTipos() {
        return QuestaoTipo.values();
    }
}
