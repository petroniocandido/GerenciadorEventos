/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class PermissaoDAO 
    extends DAOGenerico<Permissao> 
    implements PermissaoRepositorio {

    public PermissaoDAO(){
        super(Permissao.class);
    }
    
    @Override
    public List<Permissao> Buscar(Permissao filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Permissao Abrir(String uri) {
        return IgualA("uri", uri).Abrir();
    }
    
}
