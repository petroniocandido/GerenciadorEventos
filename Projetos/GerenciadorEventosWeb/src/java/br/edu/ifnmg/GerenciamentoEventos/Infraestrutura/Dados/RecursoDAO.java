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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author petronio
 */
@Singleton
public class RecursoDAO
        extends DAO<Recurso>
        implements RecursoRepositorio {

    public RecursoDAO() {
        super(Recurso.class);
    }

    @Override
    public List<Recurso> Buscar(Recurso filtro) {
        if (filtro != null) {
            IgualA("id", filtro.getId())
                    .Like("nome", filtro.getNome())
                    .Like("descricao", filtro.getDescricao())
                    .IgualA("tipo", filtro.getTipo());
        }
        return Ordenar("nome", "asc"). Buscar();
    }

}
