/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface QuestionarioRespostaRepositorio extends Repositorio<QuestionarioResposta> {
     public QuestionarioResposta Abrir(Pessoa p, Questionario q);
     public QuestionarioResposta Abrir(Inscricao i);
}
