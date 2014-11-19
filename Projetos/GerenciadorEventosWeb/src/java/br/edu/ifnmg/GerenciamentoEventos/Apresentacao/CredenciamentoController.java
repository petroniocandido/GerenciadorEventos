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

import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.ConflitoHorarioException;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LimiteInscricoesExcedidoException;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoConfirmacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "credenciamentoController")
@RequestScoped
public class CredenciamentoController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public CredenciamentoController() {
        
    }

    @EJB
    InscricaoRepositorio dao;
    
    @EJB
    EventoRepositorio daoEvt;
    
    @EJB
    InscricaoConfirmacaoService serv;
    
    @EJB
    InscricaoService inscServ;
    
    @EJB
    PessoaRepositorioLocal daoPessoa;
    
    @EJB
    PerfilRepositorio daoPerfil;
    
    @EJB
    AtividadeRepositorio daoAtividade;
    
    Long id;
    
    Inscricao inscricao;
    
    Pessoa pessoa;
    
    Evento evento;
    
    Atividade atividade;
    
    public String buscar(){
        inscricao = dao.Abrir(getId());
        return "credenciamentoConfirmacao.xhtml";
    }
    
     public List<Inscricao> autoCompletar(String query) {
        return dao.Buscar(getEvento(), query);
    }
    
    public void credenciar(){
        if(getInscricao() == null){
            MensagemErro("ERRO", "Inscrição não encontrada!");
            return;
        } 
        
        if(serv.confirmar(getInscricao(), getUsuarioCorrente())){
            Mensagem("Sucesso!", "Inscrição confirmada com êxito!");
        }
    }
    
     public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            setSessao("credEvento", evt);
        }
    }

    public Long getId() {
        if(id == null){
            String tmp = getSessao("credInsc");
            if(tmp != null)
                id = Long.parseLong(tmp);
            else
                id = 0L;
        }
        return id;
    }

    public void setId(Long id) {
        if(id != null)
            this.id = id;
        else 
            this.id = 0L;
        setSessao("credInsc", id.toString());
    }

    public Inscricao getInscricao() {
        if(inscricao == null){
            inscricao = (Inscricao)getSessao("credInsc", dao);
        }
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
        if(inscricao != null) {
            setId(inscricao.getId());
            setEvento(inscricao.getEvento());
        }
    }
   
    public void cancelar(InscricaoItem i){
        i.setStatus(InscricaoStatus.Cancelada);
        dao.Salvar(i.getInscricao());
    }
    
    public String cadastrar() {
        pessoa.setPerfil(daoPerfil.getPadrao());
        pessoa.setSenha("123");
        if(daoPessoa.Salvar(pessoa)){
            return "credenciamentoInscricao.xhtml";
        }
        MensagemErro("Erro ao cadastrar! Tente novamente", "Erro");
        return "";
    }
    
    public String inscrever() {
        Inscricao tmp = inscServ.inscrever(getEvento(), getPessoa());
        if(tmp != null){
            setInscricao(tmp);
            return "credenciamentoConfirmacao.xhtml";
        }
        MensagemErro("Erro ao cadastrar! Tente novamente", "Erro");
        return "";
    }
    
    public String inscreverAtividade() {
        try {
            if(inscServ.inscrever(getInscricao(), getAtividade(), getPessoa()) != null){
                return "credenciamentoConfirmacao.xhtml";
            }
            MensagemErro("Erro ao cadastrar! Tente novamente", "Erro");
            
        } catch (ConflitoHorarioException ex) {
            MensagemErro("Erro ao cadastrar! Tente novamente", "O participante já possui outra atividade no horário!");
        } catch (LimiteInscricoesExcedidoException ex) {
            MensagemErro("Erro ao cadastrar! Tente novamente", "O Limite de Inscrições para essa atividade foi atingido!");
        }
        return "";
    }
    
    public String novaPessoa() {
        setPessoa(null);
        return "credenciamentoCadastro.xhtml";        
    }
    
    public Evento getEvento() {
        if(evento == null){
            evento = (Evento)getSessao("credEvento", daoEvt);
            if(evento == null){
                checaEventoPadrao();
                evento = (Evento)getSessao("credEvento", daoEvt);
            }
        }
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
        setSessao("credEvento", evento);
    }

    public Pessoa getPessoa() {
        if(pessoa == null){
            pessoa = (Pessoa)getSessao("credpessoa", daoPessoa);
            if(pessoa == null) pessoa = new Pessoa();
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        if(pessoa != null){
            setSessao("credpessoa", pessoa);
        }
        this.pessoa = pessoa;
    }

    public Atividade getAtividade() {
        if(atividade == null){
            atividade = (Atividade)getSessao("credatividade", daoAtividade);
            if(atividade == null) atividade = new Atividade();
        }
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        if(atividade != null){
            setSessao("credatividade", atividade);
        }
        this.atividade = atividade;
    }
    
}
