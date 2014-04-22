/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ArquivoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoService;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
    InscricaoService inscricaoservice;

    @EJB
    PessoaRepositorio pessoaDAO;

    @EJB
    QuestionarioRepositorio questionarioDAO;

    @EJB
    QuestionarioRespostaRepositorio respostaDAO;

    @EJB
    ArquivoRepositorio arqDAO;

    Evento evento;

    Atividade atividade;

    Inscricao inscricao;

    InscricaoItem inscricaoItem;

    Questao questao;

    Questionario questionario;

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

    public Evento getEvento() {
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
        inscricao = inscricaoservice.inscrever(evento, getUsuarioCorrente());

        if (inscricao != null) {
            processaQuestionarioEvento();
        }
    }

    public String cancelarInscricaoEvento() {
        inscricao = inscricaoDAO.Refresh(inscricao);
        if (inscricaoservice.cancelar(inscricao)) {
            inscricao = null;
            return "selecionaEvento.xhtml";
        } else {
            return "";
        }

    }

    public InscricaoItem getInscricaoItem() {
        return inscricao.getItem(atividade);
    }

    public void inscreverAtividade() throws IOException {
        inscricao = inscricaoDAO.Refresh(inscricao);
        inscricaoItem = inscricaoservice.inscrever(inscricao, atividade, getUsuarioCorrente());
    }

    public String cancelarInscricaoAtividade() {
        inscricao = inscricaoDAO.Refresh(inscricao);
        inscricaoservice.cancelar(inscricaoItem);
        inscricaoItem = null;
        return "inscricaoAtividade.xhtml";
    }

    public void processaQuestionario(Inscricao i, Questionario qr) {

        i = inscricaoDAO.Refresh(i);

        QuestionarioResposta resposta = i.getResposta();

        if (resposta == null) {
            resposta = new QuestionarioResposta();
            resposta.setPessoa(getUsuarioCorrente());
            resposta.setQuestionario(qr);
        }

        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();
        Map<String, String> req = ec.getRequestParameterMap();
        for (String key : req.keySet()) {
            if (key.contains("valor-")) {

                String idQuestao = key.substring(key.lastIndexOf("-") + 1);

                if (idQuestao.contains("_focus")) {
                    continue;
                }

                if (idQuestao.contains("_input")) {
                    idQuestao = idQuestao.replace("_input", "");
                }

                Long id = Long.parseLong(idQuestao);
                Questao q = questionarioDAO.AbrirQuestao(id);
                QuestaoResposta r = resposta.RespostaDeQuestao(q);
                String valor = req.get(key);
                if (r == null) {
                    r = new QuestaoResposta();
                }
                r.setQuestao(q);
                r.setValor(valor);
                Rastrear(r);
                resposta.add(r);
            }
        }

        Rastrear(resposta);
        respostaDAO.Salvar(resposta);

        i.setResposta(resposta);

        Rastrear(i);
        inscricaoDAO.Salvar(i);

    }

    public void processaQuestionarioEvento() {
        processaQuestionario(inscricao, inscricao.getEvento().getQuestionario());
        inscricao = inscricaoDAO.Refresh(inscricao);
    }

    public void processaQuestionarioAtividade() {
        inscricaoItem = inscricao.getItem(atividade);
        processaQuestionario(getInscricaoItem(), getInscricaoItem().getAtividade().getQuestionario());
        inscricao = inscricaoDAO.Refresh(inscricao);
    }

    public void arquivoFileUpload(FileUploadEvent evt) {
        try {

            inscricao = inscricaoDAO.Refresh(inscricao);

            String key = evt.getComponent().getClientId();

            boolean item = key.contains("item-");
            
            Inscricao i = item ? getInscricaoItem(): inscricao;
            
            if(i == null){
                if(item)
                    inscreverAtividade();
                else 
                    inscreverEvento();
                
                i = item ? inscricaoItem: inscricao;
            }

            QuestionarioResposta resposta = i.getResposta();

            if (resposta == null) {
                resposta = new QuestionarioResposta();
                resposta.setPessoa(getUsuarioCorrente());
                resposta.setQuestionario(questionario);
            }

            Arquivo arq = arqDAO.Salvar(evt.getFile().getInputstream(), evt.getFile().getFileName(),
                    getConfiguracao("DIRETORIO_ARQUIVOS"), getUsuarioCorrente());

            String idQuestao = key.substring(key.lastIndexOf("-") + 1);
            Long id = Long.parseLong(idQuestao);
            Questao q = questionarioDAO.AbrirQuestao(id);
            QuestaoResposta r = resposta.RespostaDeQuestao(q);
            
            if(r == null){
                r = new QuestaoResposta();
                r.setQuestao(q);
            }

            r.setArquivo(arq);
            resposta.add(r);
            Rastrear(resposta);
            respostaDAO.Salvar(resposta);
            i.setResposta(resposta);
            Rastrear(i);
            inscricaoDAO.Salvar(i);

        } catch (IOException ex) {
            Logger.getLogger(PublicoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
