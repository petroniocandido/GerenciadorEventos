/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoTipo;
import static br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoTipo.Constante;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRespostaRepositorio;
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
@Named(value = "respostaController")
@SessionScoped
public class RespostaController
        extends ControllerBaseEntidade<QuestionarioResposta>
        implements Serializable {

    /**
     * Creates a new instance of ClienteBean
     */
    public RespostaController() {
        id = 0L;
        setEntidade(new QuestionarioResposta());
        setFiltro(new QuestionarioResposta());
        questao = new Questao();
        resposta = new QuestaoResposta();
    }
    @EJB
    QuestionarioRespostaRepositorio dao;
    boolean simNao;
    Questao questao;
    QuestaoResposta resposta;
    
    @PostConstruct
   public void init() {
      setRepositorio(dao);
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

        return "listagemRespostas.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));

        return "editarResposta.xhtml";
    }

    @Override
    public String cancelar() {
        return "listarRespostas.xhtml";
    }

    @Override
    public void limpar() {
        setId(0L);
        setEntidade(new QuestionarioResposta());
        setFiltro(new QuestionarioResposta());
        setResposta(new QuestaoResposta());
        setQuestao(new Questao());
    }

    @Override
    public String novo() {
        limpar();
        return "editarResposta.xhtml";
    }

    public void responder() {
        AppendLog("Respondendo a questão " + questao.getId() + " da avaliação " + entidade.getQuestionario().getId());
        resposta.setQuestao(questao);
        Rastrear(resposta);
        entidade.add(resposta);
        setResposta(new QuestaoResposta());
        Rastrear(entidade);
        dao.Salvar(entidade);
        Mensagem("Sucesso", "Item respondido com sucesso!");
    }


    public boolean isSimNao() {
        return simNao;
    }

    public void setSimNao(boolean simNao) {
        this.simNao = simNao;
    }


    public QuestaoResposta getResposta() {
        return resposta;
    }

    public void setResposta(QuestaoResposta resposta) {
        this.resposta = resposta;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

        
    public List<QuestionarioResposta> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    
}
