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
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRespostaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.primefaces.event.FileUploadEvent;

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

    
    public void arquivoFileUpload(FileUploadEvent event) {  
        entidade = dao.Refresh(entidade);
        resposta = entidade.RespostaDeQuestao(questao);
        resposta.setArquivo(criaArquivo(event.getFile()));
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + resposta.getArquivo()+ " a resposta " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + resposta.getArquivo() + " a resposta " + entidade + ":" + dao.getErro());
        }
        
    }

    
}
