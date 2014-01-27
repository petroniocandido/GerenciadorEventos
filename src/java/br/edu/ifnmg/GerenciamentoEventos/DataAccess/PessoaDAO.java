/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
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
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .Like("cpf", filtro.getCpf())
                .Like("email", filtro.getEmail())
                .Buscar();
    }

    @Override
    public Pessoa Abrir(String login) {
       return IgualA("email", login).Abrir();
    }

    @Override
    public Pessoa AbrirPorCPF(String cpf) {
        return IgualA("cpf", cpf).Abrir();
    }
    
}
