/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.RetornoPagSeguro.Application;

import br.edu.ifnmg.DomainModel.Configuracao;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoRepositorio;
import br.edu.ifnmg.RetornoPagSeguro.DataAccess.ConfiguracaoDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    protected static Date CalculaData(int qtdDias) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -qtdDias);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }
    
    protected static Date HojeMenos5Horas() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -5);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    public static void main(String[] args) {
        PagSeguroService pagseguro = new PagSeguroService();
        ConfiguracaoRepositorio conf = new ConfiguracaoDAO();
        while (true) {            
            int total = 0, totalConfirmado = 0, totalCancelado = 0, totalErro = 0;
            try {
                
                Configuracao dias = conf.Abrir("PagSeguroQuantidadeDias");
                
        /*        Date d = CalculaData();
                List<Lancamento> lancamentos = lancDAO
                        .MaiorOuIgualA("criacao", d)
                        .IgualA("status", LancamentoStatus.AguardandoConfirmacao)
                        .NaoENulo("transacaoPagSeguro")
                        .Buscar();

                for (Lancamento l : lancamentos) {
                    total++;
                    try {
                        LancamentoStatus status = pagseguro.receber(l);
                        
                    } catch (Exception ex) {
                        totalErro++;
                        String msg = ex.getMessage();
                        if(msg == null)
                            msg = "";
                        log.Append("Erro no retorno do PagSeguro do lançamento " + l.getId().toString() + ":" + msg);
                    }
                }
            */
                pagseguro.Sincronizar(new Date(), CalculaData( Integer.parseInt(dias.getValor())  ));

            } catch (Exception ex) {
                String msg = ex.getMessage();
                        if(msg == null)
                            msg = "";
                System.out.println(msg);
            }
            int minutos = 15;
            try {
                Thread.sleep(1000 * 60 * minutos);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(RetornoPagSeguro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
