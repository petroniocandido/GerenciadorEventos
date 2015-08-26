/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.PagSeguroPerfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PagSeguroPerfilRepositorio;
import javax.ejb.Singleton;

/**
 *
 * @author petronio
 */
@Singleton
public class PagSeguroPerfilDAO extends DAO<PagSeguroPerfil> implements PagSeguroPerfilRepositorio {

    public PagSeguroPerfilDAO() {
        super(PagSeguroPerfil.class);
    }      
       
}
