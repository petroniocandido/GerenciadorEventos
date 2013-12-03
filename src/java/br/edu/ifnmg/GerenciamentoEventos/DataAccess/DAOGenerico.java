/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Repositorio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author petronio
 * @param <T>
 */
public abstract class DAOGenerico<T> implements Repositorio<T> {

    @PersistenceContext(name = "")
    private EntityManager manager;
    
    /**
     * Cria um filtro para consulta de igualdade  
     * @param campo Atributo da classe que se deseja filtrar 
     * @param valor Valor do filtro
     * @author petronio
     * @return 
     */
    protected DAOGenerico<T> IgualA(String campo, Object valor) {
        return this;
    }

    protected DAOGenerico<T> DiferenteDe(String campo, Object valor) {
        return this;
    }

    protected DAOGenerico<T> MaiorQue(String campo, Object valor) {
        return this;
    }

    protected DAOGenerico<T> MaiorOuIgualA(String campo, Object valor) {
        return this;
    }

    protected DAOGenerico<T> MenorQue(String campo, Object valor) {
        return this;
    }

    protected DAOGenerico<T> MenorOuIgualA(String campo, Object valor) {
        return this;
    }

    protected DAOGenerico<T> Like(String campo, String valor) {
        return this;
    }

    protected DAOGenerico<T> Setar(String campo, String valor) {
        return this;
    }

    protected boolean Atualiza() {
        return false;
    }

    protected boolean Apaga() {
        return false;
    }

    @Override
    public boolean Salvar(T obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean Apagar(T obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T Abrir(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public abstract List<T> Buscar(T filtro);
}
