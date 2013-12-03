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
public class PessoaDAO 
    extends DAOGenerico<Pessoa> 
    implements PessoaRepositorio {

    public PessoaDAO(){
        super(Pessoa.class);
    }
    
    @Override
    public List<Pessoa> Buscar(Pessoa filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Arquivo Abrir(String login) {
       Pessoa tmp = new Pessoa();
       tmp.setEmail(login);
       return null;
    }

    @Override
    public Arquivo AbrirPorCPF(String cpf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
