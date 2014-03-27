/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRespostaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBase;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author petronio
 */
@Named(value = "publicoController")
@SessionScoped
public class PublicoController extends ControllerBase implements Serializable {

    @EJB
    EventoRepositorio eventoDAO;

    @EJB
    AtividadeRepositorio atividadeDAO;

    @EJB
    InscricaoRepositorio inscricaoDAO;

    @EJB
    PessoaRepositorio pessoaDAO;

    @EJB
    QuestionarioRepositorio questionarioDAO;

    @EJB
    QuestionarioRespostaRepositorio respostaDAO;

    Evento evento;

    Atividade atividade;

    Inscricao inscricao;

    InscricaoItem inscricaoItem;

    /**
     * Creates a new instance of PublicoController
     */
    public PublicoController() {

    }

    List<Evento> eventos;

    public List<Evento> getEventos() {
        if (eventos == null) {
            Evento filtro = new Evento();
            filtro.setStatus(Status.EmExecucao);
            eventos = eventoDAO.Buscar(filtro);
        }
        return eventos;
    }

    List<AtividadeTipo> tipos = new ArrayList<>();

    public List<AtividadeTipo> getAtividadesTipos() throws IOException {
        if (tipos.isEmpty()) {
            for (Atividade a : getAtividades()) {
                if (!tipos.contains(a.getTipo())) {
                    tipos.add(a.getTipo());
                }
            }
        }
        return tipos;
    }

    List<Atividade> atividades;

    public List<Atividade> getAtividades() throws IOException {
        if (atividades == null) {
            Atividade filtro = new Atividade();
            AtividadeTipo tipo = new AtividadeTipo();
            tipo.setPublico(true);
            filtro.setTipo(tipo);
            filtro.setEvento(getEvento());
            atividades = atividadeDAO.Buscar(filtro);
            return atividades;

        } else {
            return atividades;
        }
    }

    public List<Atividade> getAtividades(AtividadeTipo t) throws IOException {
        List<Atividade> tmp = new ArrayList<>();
        for (Atividade a : getAtividades()) {
            if (a.getTipo().equals(t)) {
                tmp.add(a);
            }
        }
        return tmp;
    }

    public Inscricao getInscricao() throws IOException {
        if (inscricao == null) {
            if (getEvento() != null) {
                inscricao = inscricaoDAO.Abrir(getEvento(), getUsuarioCorrente());
                return inscricao;
            } else {
                return null;
            }
        } else {
            return inscricao;
        }
    }


    public Evento getEvento() throws IOException {
        if (evento == null) {
           Redirect("selecionaEvento.xhtml");
        }
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String selecionaEvento() {
        return "inscricao.xhtml";
    }

    public String selecionaAtividade() {
        return "inscricaoAtividade.xhtml";
    }

    public void inscreverEvento() throws IOException {
        Inscricao i = new Inscricao();
        i.setEvento(getEvento());
        i.setPessoa(getUsuarioCorrente());
        Rastrear(i);
        if (inscricaoDAO.Salvar(i)) {
            inscricao = i;
        }
        
        processaQuestionarioEvento();
    }

    public String cancelarInscricaoEvento() {
        if(inscricaoDAO.Apagar(inscricao)) {
            inscricao = null;
            return "selecionaEvento.xhtml";
        }
        else {
            return "";
        }
        
    }

    public InscricaoItem getInscricaoItem() {
        return inscricao.getItem(atividade);
    }

    public void inscreverAtividade() throws IOException {
        InscricaoItem i = new InscricaoItem();
        i.setEvento(getEvento());
        i.setPessoa(getUsuarioCorrente());
        i.setAtividade(atividade);
        Rastrear(i);
        inscricao.add(i);
        
        inscricaoDAO.Salvar(i);
        
        inscricaoItem = i;
    }

    public String cancelarInscricaoAtividade() {
        inscricao.remove(getInscricaoItem());
            inscricaoItem = null;
            return "inscricaoAtividade.xhtml";
    }


    public void processaQuestionario(Inscricao i, Questionario qr) {

        i = inscricaoDAO.Refresh(i);
        
        if (i.getResposta() == null) {
            QuestionarioResposta r = new QuestionarioResposta();
            r.setPessoa(getUsuarioCorrente());
            r.setQuestionario(qr);
            i.setResposta(r);
            Rastrear(r);
            Rastrear(i);
            inscricaoDAO.Salvar(i);
        }

        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();
        Map<String, String> req = ec.getRequestParameterMap();
        for (String key : req.keySet()) {
            if (key.contains("valor-")) {
                String idQuestao = key.substring(key.lastIndexOf("-") + 1);
                String valor = req.get(key);
                Long id = Long.parseLong(idQuestao);
                Questao q = questionarioDAO.AbrirQuestao(id);
                QuestaoResposta r = i.getResposta().RespostaDeQuestao(q);
                if(r == null)
                    r = new QuestaoResposta();
                r.setQuestao(q);
                r.setValor(valor);
                Rastrear(r);
                i.getResposta().add(r);
            }
        }

        Rastrear(i);
        Rastrear(i.getResposta());
        respostaDAO.Salvar(i.getResposta());
        inscricaoDAO.Salvar(i);
    }
    
    public void processaQuestionarioEvento() {
        processaQuestionario(inscricao, inscricao.getEvento().getQuestionario());
    }
    
    public void processaQuestionarioAtividade() {
        inscricaoItem = inscricao.getItem(atividade);
        processaQuestionario(getInscricaoItem(), getInscricaoItem().getAtividade().getQuestionario());
    }

}
