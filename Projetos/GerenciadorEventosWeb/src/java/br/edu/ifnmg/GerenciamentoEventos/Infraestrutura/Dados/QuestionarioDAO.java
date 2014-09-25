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
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DataAccess.DAOGenerico;
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
        extends DAO<Questionario>
        implements QuestionarioRepositorio {
    
    DAO<Questao> daoQuestao;
    DAO<QuestionarioSecao> daoSecao;
    
    public QuestionarioDAO() {
        super(Questionario.class);
        daoQuestao = new DAO<>(Questao.class);
        daoSecao = new DAO<>(QuestionarioSecao.class);
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
