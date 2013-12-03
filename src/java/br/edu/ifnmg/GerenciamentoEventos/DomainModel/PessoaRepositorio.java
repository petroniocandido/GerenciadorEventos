/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface PessoaRepositorio extends Repositorio<Pessoa> {
    public Arquivo Abrir(String login);
    public Arquivo AbrirPorCPF(String cpf);
}
