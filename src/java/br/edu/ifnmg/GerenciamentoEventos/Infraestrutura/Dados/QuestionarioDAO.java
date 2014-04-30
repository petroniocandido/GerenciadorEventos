/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class QuestionarioDAO
        extends DAOGenerico<Questionario>
        implements QuestionarioRepositorio {
    
    DAOGenerico<Questao> daoQuestao;
    DAOGenerico<QuestionarioSecao> daoSecao;
    
    public QuestionarioDAO() {
        super(Questionario.class);
        daoQuestao = new DAOGenerico<>(Questao.class);
        daoSecao = new DAOGenerico<>(QuestionarioSecao.class);
    }
    
    @PostConstruct
    public void inicializar() {
        daoQuestao.setManager(getManager());
        daoSecao.setManager(getManager());
    }
    
    @Override
    public List<Questionario> Buscar(Questionario obj) {
        Ordenar("titulo", "Asc")
                .IgualA("id", obj.getId())
                .Like("titulo", obj.getTitulo());
        return Buscar();
    }    
    
    @Override
    public QuestionarioSecao AbrirSecao(Long id) {        
        return daoSecao.Abrir(id);
    }
    
    @Override
    public Questao AbrirQuestao(Long id) {
        return daoQuestao.Abrir(id);
    }
    
    @Override
    public List<QuestionarioSecao> Buscar(QuestionarioSecao obj) {
        return daoSecao.
                IgualA("id", obj.getId()).
                Like("nome", obj.getNome()).
                IgualA("questionario", obj.getQuestionario()).
                Buscar();
        
    }
    
    @Override
    public List<Questao> Buscar(Questao obj) {
        return daoQuestao.
                IgualA("id", obj.getId()).
                Like("enunciado", obj.getEnunciado()).
                IgualA("questionarioa", obj.getQuestionario()).
                IgualA("tipo", obj.getTipo()).
                IgualA("secao", obj.getSecao()).
                Buscar();
    }
    
    @Override
    public void Salvar(QuestionarioSecao obj) {
        daoSecao.Salvar(obj);
    }
    
    @Override
    public void Salvar(Questao obj) {
        daoQuestao.Salvar(obj);
    }
}
