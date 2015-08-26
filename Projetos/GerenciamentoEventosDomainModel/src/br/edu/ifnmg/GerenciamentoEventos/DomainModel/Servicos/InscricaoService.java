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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.ConflitoHorarioException;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Controle;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LimiteInscricoesExcedidoException;
import br.edu.ifnmg.DomainModel.Pessoa;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */

@Stateless
public class InscricaoService {
    @EJB
    ControleRepositorio controleDAO;
    
    @EJB
    InscricaoRepositorio inscricaoDAO;
    
    public Inscricao inscrever(Evento e, Pessoa p){
        
        Inscricao tmp = new Inscricao();
        tmp.setPessoa(p);
        tmp.setEvento(e);
        
        List<Inscricao> list = inscricaoDAO.Buscar(tmp);
        if(list.size() > 0)
            return list.get(0);
        
        if(!e.isPeriodoInscricaoAberto())
            return null;
        
        if(!e.isNecessitaInscricao())
            return null;
        
        Controle c = controleDAO.Abrir(e);
        
        if(e.getNumeroVagas() > 0){
            
            if(c.getQuantidadeGeral() < e.getNumeroVagas()){
                return criaInscricao(c, e, p, InscricaoCategoria.Normal);
            } 
            else  if(c.getQuantidadeListaEspera() < (e.getNumeroVagas()*0.1)){
                return criaInscricao(c, e, p, InscricaoCategoria.ListaEspera);
            }
        } else {
            return criaInscricao(c, e, p, InscricaoCategoria.Normal);
        }
        
        return null;
    }
    
    private Atividade checarConflitos(Inscricao i, Atividade b){
        for(InscricaoItem item : i.getItens()){
            Atividade a = item.getAtividade();
            boolean condA = a.getInicio().after(b.getTermino());
            boolean condB = a.getTermino().before(b.getInicio());
            if(  !( condA  ||   condB) )            
                return a;
        }
        return null;
    }
    
    public InscricaoItem inscrever(Inscricao i, Atividade e, Pessoa p) throws ConflitoHorarioException,LimiteInscricoesExcedidoException{
    
        InscricaoItem tmp = inscricaoDAO.Abrir(i, e);
        
        if(tmp != null)
            return tmp;
        
        tmp = new InscricaoItem(i, e);
        
        List<InscricaoItem> list = inscricaoDAO.Buscar(tmp);
        if(list.size() > 0)
            return list.get(list.size() - 1);
        
        if(!e.isPeriodoInscricaoAberto())
            return null;
        
        if(!e.isNecessitaInscricao())
            return null;
        
        Atividade atv = checarConflitos(i, e);
        
        if(atv != null)
            throw new ConflitoHorarioException(atv);
        
        Controle c = controleDAO.Abrir(e);
        
        if(e.getNumeroVagas() > 0){           
            
            int inscPorTipo = i.getEvento().getLimiteInscricoes(e.getTipo());
            
            if(inscPorTipo > 0){
                Long inscs = inscricaoDAO.QuantidadeInscricoes(i, e);
                if(inscs >= inscPorTipo)
                    throw new LimiteInscricoesExcedidoException(inscPorTipo,e.getTipo());
            }
            
            if(c.getQuantidadeGeral() < e.getNumeroVagas()){
                return criaInscricaoItem(c, i, e, p, InscricaoCategoria.Normal);
            } 
            else  if(c.getQuantidadeListaEspera() < (e.getNumeroVagas()*0.1)){
                return criaInscricaoItem(c, i, e, p, InscricaoCategoria.ListaEspera);
            }
        } else {
            return criaInscricaoItem(c, i, e, p, InscricaoCategoria.Normal);
        }
        
        return null;
    }
    
    public boolean cancelar(Inscricao i){
        if(inscricaoDAO.Apagar(i)){
            Controle c = controleDAO.Abrir(i.getEvento());
            if(i.getCategoria() == InscricaoCategoria.Normal)
                c.setQuantidadeGeral(c.getQuantidadeGeral() - 1);
            else 
                c.setQuantidadeListaEspera(c.getQuantidadeListaEspera()- 1);
            return controleDAO.Alterar(c);
        } else {
            return false;
        }
    }
    
     public boolean cancelar(InscricaoItem i){
        if(inscricaoDAO.Apagar(i)){
            Controle c = controleDAO.Abrir(i.getAtividade());
            if(i.getCategoria() == InscricaoCategoria.Normal)
                c.setQuantidadeGeral(c.getQuantidadeGeral() - 1);
            else 
                c.setQuantidadeListaEspera(c.getQuantidadeListaEspera()- 1);
            return controleDAO.Alterar(c);
        } else {
            return false;
        }
    }
     
     public boolean promoverListaEsperaParaNormal(InscricaoItem i){
        if(i.getCategoria() != InscricaoCategoria.ListaEspera)
            return false;
        
        Controle c = controleDAO.Abrir(i.getAtividade());
        
        if(i.getAtividade().getNumeroVagas() <= c.getQuantidadeGeral())
            return false;
        
        i.setCategoria(InscricaoCategoria.Normal);
        if(inscricaoDAO.Salvar(i)){
            c.setQuantidadeGeral(c.getQuantidadeGeral() + 1);
            c.setQuantidadeListaEspera(c.getQuantidadeListaEspera() - 1);
            return controleDAO.Alterar(c);
        } else {
            return false;
        }
    }

    private Inscricao criaInscricao(Controle ctl, Evento e, Pessoa p, InscricaoCategoria c) {
        Inscricao i = new Inscricao();
        i.setEvento(e);
        i.setPessoa(p);
        i.setCriador(p);
        i.setDataCriacao(new Date());
        i.setDataInscricao(new Date());
        i.setCategoria(c);
        i.setOrdem(c == InscricaoCategoria.Normal ? ctl.getQuantidadeGeral() : ctl.getQuantidadeListaEspera());
        if(inscricaoDAO.Salvar(i)){  
            if(i.getCategoria() == InscricaoCategoria.Normal)
                ctl.setQuantidadeGeral(ctl.getQuantidadeGeral() + 1);
            else 
                ctl.setQuantidadeListaEspera(ctl.getQuantidadeListaEspera() + 1);
            
            if(controleDAO.Alterar(ctl))
                return i;
            else return null;
        }
        else
            return null;
    }
    
    private InscricaoItem criaInscricaoItem(Controle ctl, Inscricao i, Atividade e, Pessoa p, InscricaoCategoria c) {
        InscricaoItem it;
        it = new InscricaoItem(i, e);
        it.setCriador(p);
        it.setDataCriacao(new Date());
        it.setDataInscricao(new Date());
        it.setCategoria(c);
        it.setOrdem(c == InscricaoCategoria.Normal ? ctl.getQuantidadeGeral() : ctl.getQuantidadeListaEspera());
        i.add(it);
        i.setDataUltimaAlteracao(new Date());
        i.setUltimoAlterador(p);
        
        if(inscricaoDAO.Salvar(it)){  
            if(c == InscricaoCategoria.Normal)
                ctl.setQuantidadeGeral(ctl.getQuantidadeGeral() + 1);
            else 
                ctl.setQuantidadeListaEspera(ctl.getQuantidadeListaEspera() + 1);
            
            if(controleDAO.Alterar(ctl))
                return it;
            else return null;
        }
        else
            return null;
    }
    
    
}
