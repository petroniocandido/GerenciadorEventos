/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.ApplicationCredentials;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.edu.ifnmg.DomainModel.Services.LogService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class PagSeguroService {

    @EJB
    InscricaoRepositorio inscDAO;

    @EJB
    LancamentoRepositorio lancamentoDAO;

    @EJB
    LogService log;
   

    public String Enviar(Inscricao i) throws PagSeguroServiceException, Exception {
        if(i.getEvento().getPagSeguroPerfil() == null)
            throw new Exception("Evento sem perfil de PagSeguro configurado!");
        
              
        AccountCredentials cred = new AccountCredentials(
                i.getEvento().getPagSeguroPerfil().getEmail(),
                i.getEvento().getPagSeguroPerfil().getToken(),
                i.getEvento().getPagSeguroPerfil().getTokenSandbox());
        
        /*ApplicationCredentials cred = new ApplicationCredentials(null, null, 
                i.getEvento().getPagSeguroPerfil().getAppID(), 
                i.getEvento().getPagSeguroPerfil().getAppID());
        */
        
        if(i.getLancamento() == null){
            lancamentoDAO.Salvar(i.criarLancamento(i.getPessoa()));            
        } else if(i.getLancamento().getId() == null) {
            lancamentoDAO.Salvar(i.getLancamento());
        }
        
        Lancamento lanc = i.getLancamento();
        
        Checkout checkout = new Checkout();

        checkout.addItem(i.getId().toString(), i.getEvento().getNome(), 1, i.getEvento().getValorInscricao(),
                0L, new BigDecimal("0.00"));

        for (InscricaoItem it : i.getItens()) {
            checkout.addItem(it.getId().toString(), it.getAtividade().getNome(), 1, it.getAtividade().getValorInscricao(),
                    0L, new BigDecimal("0.00"));
        }

        checkout.setSender(
                i.getPessoa().getNome(),
                i.getPessoa().getEmail(),
                null, //DDD
                null, //Telefone
                DocumentType.CPF,
                i.getPessoa().getCpf()
        );

        checkout.setCurrency(Currency.BRL);
        
        checkout.setReference(lanc.getId().toString());

        checkout.setRedirectURL("http://sge.ifnmg.edu.br/GerenciamentoEventos/publico/PagSeguroRetorno.xhtml");

        checkout.setNotificationURL("http://sge.ifnmg.edu.br/GerenciamentoEventos/PagSeguroRetorno");
        
        checkout.setMaxAge(BigInteger.valueOf(172800l)); // 2 dias 

        boolean onlyCheckoutCode = false;
        
        String response = checkout.register(cred, onlyCheckoutCode);
        
        lanc.setTransacaoPagSeguro(checkout.getReference());
        lanc.setStatus(LancamentoStatus.AguardandoConfirmacao);
        lancamentoDAO.Salvar(lanc);
        
        return response;

    }

    private boolean Receber(String l) {
        return false;
    }
    
    
}
