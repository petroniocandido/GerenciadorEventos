/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface QuestionarioRepositorio extends Repositorio<Questionario> {
    void Salvar(QuestionarioSecao obj);
    QuestionarioSecao AbrirSecao(Long id);
    List<QuestionarioSecao> Buscar(QuestionarioSecao obj);
    void Salvar(Questao obj);
    Questao AbrirQuestao(Long id);
    List<Questao> Buscar(Questao obj);
}
