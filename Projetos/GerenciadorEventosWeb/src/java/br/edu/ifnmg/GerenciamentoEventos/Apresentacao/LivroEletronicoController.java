/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.SubmissaoStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author petronio
 */
@Named(value = "livroEletronicoController")
@RequestScoped
public class LivroEletronicoController extends ControllerBase {

    @EJB
    EventoRepositorio daoEvt;

    @EJB
    AtividadeRepositorio daoAtiv;
    
    @EJB
    SubmissaoRepositorio daoSub;

    Evento evento;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        String evtid = params.get("evento");
        if(evtid == null || evtid.isEmpty()){
            
        } else {
            setEvento(daoEvt.Abrir(Long.parseLong(evtid)));
        }
    }

    public List<Atividade> getAtividades() {
        return daoAtiv
                .IgualA("evento", getEvento())
                .DiferenteDe("status", br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status.Cancelado)
                .IgualA("aceitaSubmissoes", true)
                .Ordenar("nome", "ASC")
                .Buscar();
    }
    
     public List<AreaConhecimento> buscarAreasConhecimento(Atividade a) {
        List<AreaConhecimento> tmp = new ArrayList<>();
        for(AreaConhecimento ac : daoSub.AreasPorAtividade(a, SubmissaoStatus.Aprovado))
            if(!tmp.contains(ac))
                tmp.add(ac);
        return tmp;
    }

    public List<Submissao> buscarSubmissoes(Atividade a, AreaConhecimento ac) {
        return daoSub.Buscar(SubmissaoStatus.Aprovado, evento, a, ac);
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}
