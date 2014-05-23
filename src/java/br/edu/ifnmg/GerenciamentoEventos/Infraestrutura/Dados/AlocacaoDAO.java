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
       return MaiorOuIgualA("evento", filtro.getEvento())
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("recurso", filtro.getRecurso())    
                .MaiorOuIgualA("inicio", filtro.getInicio())
                .Ordenar("inicio", "asc")
                .Buscar();
               
    }
    
}
