/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.RetornoPagSeguro.Application;

import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionSearchResult;
import br.com.uol.pagseguro.domain.TransactionSummary;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.*;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.PagSeguroPerfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PagSeguroPerfilRepositorio;
import br.edu.ifnmg.RetornoPagSeguro.DataAccess.InscricaoDAO;
import br.edu.ifnmg.RetornoPagSeguro.DataAccess.LancamentoDAO;
import br.edu.ifnmg.RetornoPagSeguro.DataAccess.PagSeguroPerfilDAO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author petronio
 */
public class PagSeguroService {

    LancamentoRepositorio lancDAO = new LancamentoDAO();
    InscricaoRepositorio inscDAO = new InscricaoDAO();
    LogServiceImpl log = new LogServiceImpl();
    

    public void Sincronizar(Date dataInicio, Date dataFim) throws PagSeguroServiceException {
        PagSeguroPerfilRepositorio perfDAO = new PagSeguroPerfilDAO();

        List<PagSeguroPerfil> perfis = perfDAO.Buscar();

        int sucesso = 0;
        int erro = 0;
        
        for (PagSeguroPerfil perfil : perfis) {
            AccountCredentials cred = new AccountCredentials(
                    perfil.getEmail(),
                    perfil.getToken(),
                    perfil.getTokenSandbox());

            TransactionSearchResult res = TransactionSearchService.searchByDate(cred, dataFim, dataInicio, 1, 5000);
            for (TransactionSummary t : res.getTransactionSummaries()) {
                try {
                    Long id = Long.parseLong(t.getReference());
                    Lancamento l = lancDAO.Abrir(id);
                    if (l != null) {
                        if (l.getStatus() == LancamentoStatus.AguardandoConfirmacao || l.getStatus() == LancamentoStatus.Aberto) {
                            l.setTransacaoPagSeguro(t.getCode());
                            LancamentoStatus s = ConverteStatus(t.getStatus());
                            l = AtualizaLancamento(l, s);
                            if (lancDAO.Salvar(l)) {
                                sucesso++;
                                //log.Append("Atualizando lançamento " + l.getId().toString() + " no PagSeguro. Situacção: " + t.getStatus());
                            } else {
                                erro++;
                                log.Append("Falha ao atualizar lançamento " + l.getId().toString() + " no PagSeguro. Situacção: " + t.getStatus());
                            }
                        }
                    }
                } catch(Exception e){
                    erro++;
                    log.Append("Falha ao atualizar lançamento " + t.getReference() + ", " + t.getPaymentLink());
                }
            }
        }
        log.Append("Final da atualização de lançamentos no PagSeguro: Sucessos: " + sucesso + " . Falhas: " + erro);
    }

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

        transaction = TransactionSearchService.searchByCode(cred, l.getTransacaoPagSeguro());
        return ConverteStatus(transaction.getStatus());
    }

    protected LancamentoStatus ConverteStatus(TransactionStatus s) {
        switch (s) {
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

    protected Lancamento AtualizaLancamento(Lancamento l, LancamentoStatus s) {
        switch (s) {
            default:
            case AguardandoConfirmacao:
                return l;
            case Baixado:
                l.baixar(l.getCliente());
                for (Inscricao i : l.getInscricoes()) {
                    i.setStatus(InscricaoStatus.Confirmada);
                    i.setPago(true);
                    for (InscricaoItem it : i.getItens()) {
                        i.setStatus(InscricaoStatus.Confirmada);
                        i.setPago(true);
                    }
                    inscDAO.Salvar(i);
                }
                return l;
            case Cancelado:
                l.cancelar(l.getCliente());
                for (Inscricao i : l.getInscricoes()) {
                    i.setStatus(InscricaoStatus.Criada);
                    i.setPago(false);
                    for (InscricaoItem it : i.getItens()) {
                        i.setStatus(InscricaoStatus.Criada);
                        i.setPago(false);
                    }
                    inscDAO.Salvar(i);
                }
                return l;
        }
    }
}
