/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericConverter;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author petronio
 */
@Named(value = "questionarioConverter")
@Singleton
public class QuestionarioConverter  extends GenericConverter<Questionario, QuestionarioRepositorio>
        implements Serializable {

    @EJB
    QuestionarioRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
    public List<Questionario> autoCompleteQuestionario(String query) {
        Questionario i = new Questionario();
        i.setTitulo(query);
        return dao.Buscar(i);
    }

   
}
