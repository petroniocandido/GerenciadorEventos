/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.RetornoPagSeguro.Application;

import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.NotificationService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;

/**
 *
 * @author petronio
 */
public class PagSeguroService {

    public LancamentoStatus receber(Lancamento l) throws Exception {
        Transaction transaction = null;
        if (l.getInscricoes().isEmpty()) {
            throw new Exception("Sem inscrições associadas!");
        }
        Inscricao i = l.getInscricoes().get(0);
        
        AccountCredentials cred = new AccountCredentials(
                i.getEvento().getPagSeguroPerfil().getEmail(),
                i.getEvento().getPagSeguroPerfil().getToken(),
                i.getEvento().getPagSeguroPerfil().getTokenSandbox());

       
       transaction = NotificationService.checkTransaction(cred, l.getTransacaoPagSeguro());
       
       switch(transaction.getStatus()){
           case CANCELLED:
           case CONTESTATION:
           case IN_DISPUTE:
               return LancamentoStatus.Cancelado;
               
           case PAID:
           case AVAILABLE:
               return LancamentoStatus.Baixado;
               
           case INITIATED:
           case IN_ANALYSIS:               
           case WAITING_PAYMENT:
           default:
               return LancamentoStatus.AguardandoConfirmacao;
       }
    }
}
