/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import java.util.List;

/**
 *
 * @author petronio
 * @param <T>
 */
public interface Repositorio<T> {
    public boolean Salvar(T obj);
    public boolean Apagar(T obj);
    public T Abrir(Long id);
    public List<T> Buscar(T filtro);
    public Exception getErro();
    public T Refresh(T obj);
    public boolean Atualiza();
    public boolean Apaga();
    public T Abrir();
    public List<T> Buscar();
    
    public Repositorio<T> Join(String campo, String alias);
    public Repositorio<T> IgualA(String campo, Object valor);
    public Repositorio<T> DiferenteDe(String campo, Object valor);
    public Repositorio<T> MaiorQue(String campo, Object valor);
    public Repositorio<T> MaiorOuIgualA(String campo, Object valor);
    public Repositorio<T> MenorQue(String campo, Object valor);
    public Repositorio<T> MenorOuIgualA(String campo, Object valor);
    public Repositorio<T> Like(String campo, String valor);
    public Repositorio<T> ENulo(String campo);
    public Repositorio<T> NaoENulo(String campo);
    public Repositorio<T> Ordenar(String campo, String sentido);
    public Repositorio<T> Setar(String campo, Object valor);
    
}
