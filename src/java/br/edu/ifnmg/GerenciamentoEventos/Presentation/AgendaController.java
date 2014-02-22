/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author petronio
 */
@Named(value = "agendaController")
@SessionScoped
public class AgendaController
        extends ControllerBaseEntidade<Evento>
        implements Serializable {

    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AgendaController() {

    }

    @EJB
    EventoRepositorio dao;

    @EJB
    AtividadeRepositorio daoA;

    Evento padrao;

    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            padrao = dao.Abrir(Long.parseLong(evt));
        }
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    private ScheduleEvent add(Evento e) {
        return new DefaultScheduleEvent(e.getNome(), e.getInicio(), e.getTermino(), true);
    }

    private ScheduleEvent addInscricao(Evento e) {
        return new DefaultScheduleEvent("Inscrição " + e.getNome(), e.getInicioInscricao(), e.getInicioInscricao(), true);
    }

    private ScheduleEvent add(Atividade a) {
        return new DefaultScheduleEvent(a.getNome(), a.getInicio(), a.getTermino());
    }

    private ScheduleEvent addInscricao(Atividade a) {
        return new DefaultScheduleEvent("Inscrição " + a.getNome(), a.getInicioInscricao(), a.getInicioInscricao());
    }

    public ScheduleModel getEventModel() {
        if (eventModel == null) {
            checaEventoPadrao();
            eventModel = new DefaultScheduleModel();
            if (padrao != null) {
                eventModel.addEvent(add(padrao));
                if (padrao.isNecessitaInscricao()) {
                    eventModel.addEvent(addInscricao(padrao));
                }

                Atividade filtro = new Atividade();
                filtro.setEvento(padrao);
                List<Atividade> atividades = daoA.Buscar(filtro);
                for (Atividade atividade : atividades) {
                    eventModel.addEvent(add(atividade));
                    if (atividade.isNecessitaInscricao()) {
                        eventModel.addEvent(addInscricao(atividade));
                    }
                }
            }
        }
        return eventModel;
    }

    @Override
    public void filtrar() {
        listagem = dao.Buscar(filtro);
    }

    @Override
    public void salvar() {
    }

    @Override
    public String apagar() {
        return "";
    }

    @Override
    public String abrir() {
        return "";
    }

    @Override
    public String cancelar() {
        return "";
    }

    @Override
    public void limpar() {
        setEntidade(new Evento());
    }

    @Override
    public String novo() {
        limpar();
        return "";
    }

    @Override
    public List<Evento> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Evento> listagem) {
        this.listagem = listagem;
    }

}
