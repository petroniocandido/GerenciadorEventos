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
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoConfirmacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "credenciamentoBlocoController")
@SessionScoped
public class CredenciamentoBlocoController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public CredenciamentoBlocoController() {
        inscricoes = new ArrayList<>();
        valor = new BigDecimal("0.00");
    }

    @EJB
    InscricaoRepositorio dao;
    
    @EJB
    EventoRepositorio daoEvt;
    
    @EJB
    InscricaoConfirmacaoService serv;
    
    Long id;
    
    Inscricao inscricao;
    
    BigDecimal valor;
    
    List<Inscricao> inscricoes;
    
    Evento evento;
    
    public void buscar(){
        inscricao = dao.Abrir(getId());
        add();
    }
    
     public List<Inscricao> autoCompletar(String query) {
        return dao.Buscar(getEvento(), query);
    }
    
    public void credenciar(){        
        if(serv.confirmar(getEvento(), inscricoes, getUsuarioCorrente())){
            Mensagem("Sucesso!", "Inscrição confirmada com êxito!");
            limpar();
        }
    }

    public void limpar() {
        inscricoes = new ArrayList<>();
        valor = new BigDecimal("0.00");
    }
    
     public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            evento = daoEvt.Abrir(Long.parseLong(evt));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;        
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;        
    }

    public Evento getEvento() {
        if(evento == null){
                checaEventoPadrao();
        }
        
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    public void cancelar(InscricaoItem i){
        i.setStatus(InscricaoStatus.Cancelada);
        dao.Salvar(i.getInscricao());
    }
    
    public void add() {
        inscricoes.add(inscricao);
        BigDecimal tmp = inscricao.getValorTotal();
        valor = valor.add(tmp);
    }
    
    public void remove() {
        inscricoes.remove(getInscricao());
        BigDecimal tmp = inscricao.getValorTotal();
        valor = valor.subtract(tmp);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }
    
    

}
