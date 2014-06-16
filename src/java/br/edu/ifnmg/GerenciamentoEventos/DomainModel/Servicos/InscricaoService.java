/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Controle;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
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
    
    public InscricaoItem inscrever(Inscricao i, Atividade e, Pessoa p){
    
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
        
        Controle c = controleDAO.Abrir(e);
        
        if(e.getNumeroVagas() > 0){           
            
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
