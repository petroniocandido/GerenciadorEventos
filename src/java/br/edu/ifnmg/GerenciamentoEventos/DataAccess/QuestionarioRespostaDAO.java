/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DataAccess;


import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRespostaRepositorio;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless(name = "QuestionarioRespostaRepositorio")
public class QuestionarioRespostaDAO
        extends DAOGenerico<QuestionarioResposta>
        implements QuestionarioRespostaRepositorio {

    public QuestionarioRespostaDAO() {
        super(QuestionarioResposta.class);
    }

    @Override
    public List<QuestionarioResposta> Buscar(QuestionarioResposta obj) {
                IgualA("id", obj.getId())
                .IgualA("pessoa", obj.getPessoa())
                .IgualA("questionario", obj.getQuestionario());
        return Buscar();
    }
}