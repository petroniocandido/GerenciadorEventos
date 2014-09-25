/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
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
