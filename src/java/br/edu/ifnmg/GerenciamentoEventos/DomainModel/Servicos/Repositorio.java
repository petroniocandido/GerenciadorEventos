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
    
    public Class getTipo();
    
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
