/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class RecursoDAO 
    extends DAOGenerico<Recurso> 
    implements RecursoRepositorio {

    public RecursoDAO(){
        super(Recurso.class);
    }
    
    @Override
    public List<Recurso> Buscar(Recurso filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .Like("descricao", filtro.getDescricao())
                .IgualA("tipo", filtro.getTipo())
                .Buscar();

    }
    
}
