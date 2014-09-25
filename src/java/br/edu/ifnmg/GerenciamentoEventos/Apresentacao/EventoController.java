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



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author petronio
 */
@Named(value = "eventoController")
@RequestScoped
public class EventoController
        extends ControllerBaseEntidade<Evento>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public EventoController() {
    }
    
    @EJB
    EventoRepositorio dao;
    
    UploadedFile arquivo;
    
    AtividadeTipo atividadeTipo;
    
    Integer limite;

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }
        
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarEvento.xhtml");
        setPaginaListagem("listagemEventos.xtml");
    }
    
    @Override
    public Evento getFiltro() {
        if (filtro == null) {
            filtro = new Evento();
            filtro.setNome(getSessao("evtctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Evento filtro) {
        this.filtro = filtro;
        if(filtro != null)
            setSessao("evtctrl_nome", filtro.getNome());
    }

    @Override
    public void limpar() {
        setEntidade(new Evento());
    }

    public void configurarEventoGlobal() {
        setConfiguracao("EVENTO_PADRAO", entidade.getId().toString());
        Mensagem("Sucesso", "Configuração global alterada com êxito!");
    }
    
    public void configurarEventoUsuario() {
        setConfiguracao(getUsuarioCorrente(), "EVENTO_PADRAO", entidade.getId().toString());
        Mensagem("Sucesso", "Configuração do usuário alterada com êxito!");
    }
    
    public void fileUploadListener(FileUploadEvent event) {  
        entidade = dao.Refresh(getEntidade());
        Arquivo tmp = criaArquivo(event.getFile());
        String arq = (String)event.getComponent().getAttributes().get("arquivo");
        switch(arq){
            case "banner":
                entidade.setBanner(tmp);
                break;
            case "logomarca":
                entidade.setLogo(tmp);
                break;
            case "Imagem de Fundo":
                entidade.setCertificadoFundo(tmp);
                break;
            case "Assinatura 1":
                entidade.setCertificadoAssinatura1(tmp);
                break;
            case "Assinatura 2":
                entidade.setCertificadoAssinatura2(tmp);
                break;
        }
        
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + entidade.getLogo() + " ao evento " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + entidade.getLogo() + " ao evento " + entidade + ":" + dao.getErro());
        }        
    }
    
     public Status[] getStatus() {
        return Status.values();
    }
     
     Pessoa responsavel;
     
     public void addResponsavel() {
        entidade = dao.Refresh(getEntidade());
        entidade.add(responsavel);
        SalvarAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void removeResponsavel() {
        entidade = dao.Refresh(getEntidade());
        entidade.remove(responsavel);
        RemoverAgregado(responsavel);
        responsavel = new Pessoa();
    }
    
    public void addLimite() {
        entidade = dao.Refresh(getEntidade());
        entidade.addLimite(atividadeTipo, limite);
        SalvarAgregado(atividadeTipo);
        atividadeTipo = new AtividadeTipo();
    }

    public void removeLimite() {
        entidade = dao.Refresh(getEntidade());
        entidade.removeLimite(atividadeTipo);
        RemoverAgregado(atividadeTipo);
        atividadeTipo = new AtividadeTipo();
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public AtividadeTipo getAtividadeTipo() {
        return atividadeTipo;
    }

    public void setAtividadeTipo(AtividadeTipo atividadeTipo) {
        this.atividadeTipo = atividadeTipo;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }
    
    public List<Entry<AtividadeTipo, Integer>> getLimitesAtividades() {
        return new ArrayList<Entry<AtividadeTipo, Integer>>(entidade.getInscricoesPorAtividade().entrySet());
    }
    
    
}
