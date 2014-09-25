/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciadorMensagens.Application;

import br.edu.ifnmg.DomainModel.Services.MailService;
import br.edu.ifnmg.GerenciadorMensagens.DataAccess.MensagemDAO;
import br.edu.ifnmg.GerenciadorMensagens.DomainModel.Mensagem;
import br.edu.ifnmg.GerenciadorMensagens.DomainModel.MensagemRepositorio;

/**
 *
 * @author petronio
 */
public class GerenciadorMensagens {

    public static void main(String[] args) {
        MensagemRepositorio mDAO = new MensagemDAO();
        MailService mail = new MailServiceImpl();
        br.edu.ifnmg.DomainModel.Services.LogService log = new LogServiceImpl();
        try {
            for (Mensagem m : mDAO.Buscar()) {
                try {
                    if (mail.enviar(m.getDestinatario(), m.getAssunto(), m.getCorpo())) {
                        mDAO.Apagar(m);
                    } else {
                        if (m.getNumeroTentativas() <= 5) {
                            m.setNumeroTentativas(m.getNumeroTentativas() + 1);
                        } else {
                            mDAO.Apagar(m);
                            log.Append("A mensagem \'" + m.getAssunto() + "\" para o destinatário \"" + m.getDestinatario()
                                    + " está sendo removida após 5 tentativas de envio sem êxito!");
                        }
                    }
                } catch (Exception ex) {
                    log.Append("Erro ao processar a mensagem : " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            log.Append("Erro no Gerenciador de Mensagens: " + ex.getMessage());
        }
    }

}
