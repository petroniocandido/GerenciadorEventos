/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class LogDAO 
    extends DAOGenerico<Log> 
    implements LogRepositorio {

    public LogDAO(){
        super(Log.class);
    }
    
    @Override
    public List<Log> Buscar(Log filtro) {
       return MaiorOuIgualA("dataEvento", filtro.getDataEvento())
                .IgualA("usuario", filtro.getUsuario())
                .IgualA("permissao", filtro.getPermissao())    
                .Ordenar("dataEvento", "desc")
                .Buscar();
               
    }
    
}
