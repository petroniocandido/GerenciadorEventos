/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.RetornoPagSeguro.Application;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.RetornoPagSeguro.DataAccess.InscricaoDAO;
import br.edu.ifnmg.RetornoPagSeguro.DataAccess.LancamentoDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petronio
 */
public class RetornoPagSeguro {

    /**
     * @param args the command line arguments
     */
    protected static Date HojeMenos5() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -5);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    public static void main(String[] args) {
        br.edu.ifnmg.DomainModel.Services.LogService log = new LogServiceImpl();
        LancamentoRepositorio lancDAO = new LancamentoDAO();
        InscricaoRepositorio inscDAO = new InscricaoDAO();
        PagSeguroService pagseguro = new PagSeguroService();
        while (true) {            
            int total = 0, totalConfirmado = 0, totalCancelado = 0, totalErro = 0;
            try {
                Date d = HojeMenos5();
                List<Lancamento> lancamentos = lancDAO
                        .MaiorOuIgualA("criacao", d)
                        .IgualA("status", LancamentoStatus.AguardandoConfirmacao)
                        .NaoENulo("transacaoPagSeguro")
                        .Buscar();

                for (Lancamento l : lancamentos) {
                    total++;
                    try {
                        LancamentoStatus status = pagseguro.receber(l);
                        switch (status) {
                            case AguardandoConfirmacao:
                                break;
                            case Baixado:
                                totalConfirmado++;
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
                            case Cancelado:
                                totalCancelado++;
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

                        }
                    } catch (Exception ex) {
                        totalErro++;
                        String msg = ex.getMessage();
                        if(msg == null)
                            msg = "";
                        log.Append("Erro no retorno do PagSeguro do lançamento " + l.getId().toString() + ":" + msg);
                    }
                }

            } catch (Exception ex) {
                String msg = ex.getMessage();
                        if(msg == null)
                            msg = "";
                log.Append("Erro no retorno do PagSeguro do lançamento:" + msg);
            }
            log.Append("Término da execução do Retorno do PagSeguro: " + total + " transações, "
                        + totalConfirmado + " confirmadas, " + totalCancelado + " canceladas, "
                        + totalErro + " erros.");
                System.out.println("Término da execução do Retorno do PagSeguro: " + total + " transações, "
                        + totalConfirmado + " confirmadas, " + totalCancelado + " canceladas, "
                        + totalErro + " erros.");
            int minutos = 60;
            try {
                Thread.sleep(1000 * 60 * minutos);
            } catch (InterruptedException ex) {
                Logger.getLogger(RetornoPagSeguro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
