/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class LogDAO 
    extends DAOGenerico<Log> 
    implements LogRepositorio {

    public LogDAO(){
        super(Log.class);
    }
    
    @Override
    public List<Log> Buscar(Log filtro) {
       return IgualA("id", filtro.getId())
                .IgualA("data", filtro.getData())
                .IgualA("usuario", filtro.getUsuario())
                .IgualA("permissao", filtro.getPermissao())               
                .Buscar();
    }
    
}
