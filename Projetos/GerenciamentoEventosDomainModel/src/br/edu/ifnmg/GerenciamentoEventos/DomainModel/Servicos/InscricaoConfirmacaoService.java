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

import br.edu.ifnmg.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import java.util.Date;
import java.util.List;
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

    @EJB
    LogRepositorio daoLog;

    public boolean confirmarPresenca(Inscricao i) {
        try {
            i.setCompareceu(true);
            return daoInsc.Salvar(i);
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean confirmar(Inscricao i, Pessoa p) {
        try {
            Lancamento l = i.pagar(p);
            l.setCategoria(daoLanc.CategoriaPadrao());
            return daoInsc.Salvar(i);
        } catch (Exception ex) {
            Log l = new Log();
            l.setCriador(p);
            l.setDescricao(ex.getMessage());
            l.setUsuario(p);
            daoLog.Salvar(l);
            return false;
        }
    }

    public boolean confirmar(Evento e, List<Inscricao> insc, Pessoa p) {
        try {
            Lancamento l = new Lancamento();
            l.setCliente(p);
            l.setEvento(e);
            l.setCriador(p);
            l.setDataCriacao(new Date());
            l.setCategoria(daoLanc.CategoriaPadrao());
            StringBuilder sb = new StringBuilder("Confirmação das inscrições ");
            for (Inscricao i : insc) {
                sb.append(i.getId().toString()).append(",");
                l.setValorOriginal(l.getValorOriginal().add(i.getValorTotal()));
            }
            l.setDescricao(sb.toString());
            l.baixar(p);
            if (daoLanc.Salvar(l)) {
                for (Inscricao i : insc) {
                    i.pagar(l, p);
                    daoInsc.Salvar(i);
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            Log l = new Log();
            l.setCriador(p);
            l.setDescricao(ex.getMessage());
            l.setUsuario(p);
            daoLog.Salvar(l);
            return false;
        }
    }
}
