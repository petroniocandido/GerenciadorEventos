/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciadorMensagens.DataAccess;

import br.edu.ifnmg.DomainModel.MensagemPerfil;
import br.edu.ifnmg.DomainModel.Services.MensagemPerfilRepositorio;

/**
 *
 * @author petronio
 */
public class MensagemPerfilDAO extends DAO<MensagemPerfil> implements MensagemPerfilRepositorio {

    public MensagemPerfilDAO() {
        super(MensagemPerfil.class);
    }
       
}
