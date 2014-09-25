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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AlocacaoRepositorio;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class AlocacaoDAO 
    extends DAOGenerico<Alocacao> 
    implements AlocacaoRepositorio {

    public AlocacaoDAO(){
        super(Alocacao.class);
    }
    
    @Override
    public List<Alocacao> Buscar(Alocacao filtro) {
       return IgualA("evento", filtro.getEvento())
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("recurso", filtro.getRecurso())    
                .MaiorOuIgualA("inicio", filtro.getInicio())
                .Ordenar("inicio", "asc")
                .Buscar();
               
    }
    
    @Override
    public List<Alocacao> conflitos(Alocacao filtro) {
        String sql = "select a from Alocacao a where a.recurso = :recurso and a.status <> :concluido and a.status <> :cancelado "
                + "and not ("
                + "   (  a.inicio < :inicio\n" +           // antes
"                        and   a.termino < :inicio )\n" +
"               or   (\n  a.inicio > :termino\n" +           // depois
"                        and   a.termino > :termino ))";
       Query query = getManager().createQuery(sql);
       query.setParameter("recurso", filtro.getRecurso())
               .setParameter("concluido", AlocacaoStatus.Concluido)
               .setParameter("cancelado", AlocacaoStatus.Cancelado)
               .setParameter("inicio", filtro.getInicio())
               .setParameter("termino", filtro.getTermino());
       return query.getResultList();
    }
    
}
