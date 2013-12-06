/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LocalRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class LocalDAO 
    extends DAOGenerico<Local> 
    implements LocalRepositorio {

    public LocalDAO(){
        super(Local.class);
    }
    
    @Override
    public List<Local> Buscar(Local filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .IgualA("capacidade", filtro.getCapacidade())
                .Buscar();
    }
    
}
