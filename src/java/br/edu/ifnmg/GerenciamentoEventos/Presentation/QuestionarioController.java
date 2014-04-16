/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseEntidade;
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
    QuestaoTipo[] tipos;
    Questao questao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Questionario> autoCompleteQuestionario(String query) {
        Questionario i = new Questionario();
        i.setTitulo(query);
        return dao.Buscar(i);
    }

    public List<QuestionarioSecao> autoCompleteQuestionarioSecao(String query) {
        QuestionarioSecao i = new QuestionarioSecao();
        i.setNome(query);
        i.setQuestionario(entidade);
        return dao.Buscar(i);
    }

    @Override
    public void filtrar() {
        listagem = dao.Buscar(filtro);
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
        if (tipos == null) {
            tipos = QuestaoTipo.values();
        }
        return tipos;
    }

    @Override
    public List<Questionario> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

}
