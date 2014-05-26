/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class InscricaoConfirmacaoService {
    
    @EJB
    InscricaoRepositorio daoInsc;
    
    @EJB
    LancamentoRepositorio daoLanc;
    
    public boolean confirmar(Inscricao i, Pessoa p){
        i.setCompareceu(true);
        i.setDataPagamento(new Date());
        i.setPago(true);
        i.setStatus(InscricaoStatus.Confirmada);
        if(i.getLancamento() == null){
            Lancamento l = i.criarLancamento(p);
            i.setLancamento(l);
            l.baixar(p);
            daoLanc.Salvar(l);
        }
        
        return daoInsc.Salvar(i);
    }
}
