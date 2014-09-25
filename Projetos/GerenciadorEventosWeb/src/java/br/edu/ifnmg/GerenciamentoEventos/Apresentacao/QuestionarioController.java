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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "questionarioController")
@SessionScoped
public class QuestionarioController
        extends ControllerBaseEntidade<Questionario>
        implements Serializable {

    /**
     * Creates a new instance of ClienteBean
     */
    public QuestionarioController() {
        id = 0L;
        setEntidade(new Questionario());
        setFiltro(new Questionario());
        novaQuestao();
        novaSecao();
    }
    @EJB
    QuestionarioRepositorio dao;
    QuestionarioSecao secao;
    Questao questao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    

    @Override
    public void salvar() {
        SalvarEntidade();

        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        ApagarEntidade();
        filtrar();
        return "listagemQuestionarios.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarQuestionario.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemQuestionarios.xhtml";
    }

    @Override
    public void limpar() {
        setId(0L);
        setEntidade(new Questionario());
        setQuestao(new Questao());
        setSecao(new QuestionarioSecao());
    }

    @Override
    public String novo() {
        limpar();
        return "editarQuestionario.xhtml";
    }

    public void addSecao() {
        Refresh();
        entidade.add(secao);
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
        entidade.remove(secao);
        RemoverAgregado(secao);
        novaSecao();
    }

    public void addQuestao() {
        Refresh();
        entidade.add(questao);
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
        entidade.remove(questao);
        dao.Salvar(entidade);        
    }

    public void removeQuestao() {
        Refresh();
        entidade.remove(questao);
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
