/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciadorMensagens.Application;

import br.edu.ifnmg.DomainModel.Services.MailService;
import br.edu.ifnmg.GerenciadorMensagens.DataAccess.MensagemDAO;
import br.edu.ifnmg.DomainModel.Mensagem;
import br.edu.ifnmg.DomainModel.Services.MensagemRepositorio;
import java.util.List;

/**
 *
 * @author petronio
 */
public class GerenciadorMensagens {

    public static void main(String[] args) {
        MensagemRepositorio mDAO = new MensagemDAO();
        MailService mail = new MailServiceImpl();
        br.edu.ifnmg.DomainModel.Services.LogService log = new LogServiceImpl();
        int total=0, totalEnviado=0, totalNaoEnviado=0, totalErro=0;
        try {
            List<Mensagem> mensagens = mDAO.Buscar();
            total = mensagens.size();
            for (Mensagem m : mensagens) {
                try {
                    if (mail.enviar(m.getDestinatario(), m.getAssunto(), m.getCorpo())) {
                        totalEnviado++;
                        mDAO.Apagar(m);
                    } else {
                        totalNaoEnviado++;
                        if (m.getNumeroTentativas() <= 5) {
                            m.setNumeroTentativas(m.getNumeroTentativas() + 1);
                        } else {
                            mDAO.Apagar(m);
                            log.Append("A mensagem \'" + m.getAssunto() + "\" para o destinatário \"" + m.getDestinatario()
                                    + " está sendo removida após 5 tentativas de envio sem êxito!");
                        }
                    }
                } catch (Exception ex) {
                    totalErro++;
                    log.Append("Erro ao processar a mensagem : " + ex.getMessage());
                    System.out.println("Erro ao processar a mensagem : " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            totalErro++;
            log.Append("Erro no Gerenciador de Mensagens: " + ex.getMessage());
            System.out.println("Erro no Gerenciador de Mensagens: " + ex.getMessage());
        }
        log.Append("Término da execução do Gerenciador de Mensagens: " + total + " mensagens, " 
                + totalEnviado + " enviadas, "+ totalNaoEnviado + " não enviadas, "
                + totalErro + " erros.");
        System.out.println("Término da execução do Gerenciador de Mensagens: " + total + " mensagens, " 
                + totalEnviado + " enviadas, "+ totalNaoEnviado + " não enviadas, "
                + totalErro + " erros.");
    }

}
