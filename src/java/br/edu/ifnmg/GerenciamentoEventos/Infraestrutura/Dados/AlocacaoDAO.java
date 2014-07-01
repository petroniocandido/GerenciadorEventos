/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
                + "and (a.inicio < :termino\n" +
"               or   a.termino > :inicio\n" +
"               or   (\n" +
"                            a.inicio < :inicio\n" +
"                        and  a.termino > :termino\n" +
"                    )\n" +
"               or   (\n" +
"                            a.inicio > :inicio\n" +
"                        and  a.termino < :termino\n" +
"                    ))";
       Query query = getManager().createQuery(sql);
       query.setParameter("recurso", filtro.getRecurso())
               .setParameter("concluido", AlocacaoStatus.Concluido)
               .setParameter("cancelado", AlocacaoStatus.Cancelado)
               .setParameter("inicio", filtro.getInicio())
               .setParameter("termino", filtro.getTermino());
       return query.getResultList();
    }
    
}
