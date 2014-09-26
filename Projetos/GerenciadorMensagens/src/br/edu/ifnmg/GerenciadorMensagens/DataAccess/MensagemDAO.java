/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciadorMensagens.DataAccess;

import br.edu.ifnmg.DomainModel.Mensagem;
import br.edu.ifnmg.DomainModel.Services.MensagemRepositorio;

/**
 *
 * @author petronio
 */
public class MensagemDAO extends DAO<Mensagem> implements MensagemRepositorio {

    public MensagemDAO() {
        super(Mensagem.class);
    }
       
}
