/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

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
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
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

    public List<Evento> getEventos() {
        Evento filtro = new Evento();
        filtro.setStatus(Status.EmExecucao);
        return eventoDAO.Buscar(filtro);
    }

   public List<AtividadeTipo> getAtividadesTipos() {
       List<AtividadeTipo> tmp = atividadeDAO.BuscarAtividadesTiposPorEvento(evento);
        return tmp;
    }


    public List<Atividade> getAtividades(AtividadeTipo t) throws IOException {
        List<Atividade> tmp = atividadeDAO.BuscarAtividadesPorEventoETipo(evento, t);
       return tmp;
    }

    public Inscricao getInscricao() {
        if (inscricao == null) {
            if (evento != null) {
                inscricao = inscricaoDAO.Abrir(evento, getUsuarioCorrente());
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
        this.atividade = null;
        this.inscricao = null;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public void inscreverEvento() throws IOException {
        inscricao = inscricaoservice.inscrever(evento, getUsuarioCorrente());

        if (inscricao != null) {
            processaQuestionarioEvento(inscricao);
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
        if (inscricao != null) {
            return inscricao.getItem(atividade);
        } else {
            return null;
        }
    }

    public void inscreverAtividade() throws IOException {
        inscricao = inscricaoDAO.Refresh(inscricao);
        inscricaoItem = inscricaoservice.inscrever(inscricao, atividade, getUsuarioCorrente());
        if (inscricaoItem != null) {
            processaQuestionarioAtividade(inscricao, inscricaoItem);
        }
    }

    public String cancelarInscricaoAtividade() {
        inscricao = inscricaoDAO.Refresh(inscricao);
        if (inscricaoservice.cancelar(getInscricaoItem())) {
            inscricaoItem = null;
            return "inscricao.xhtml";
        } else {
            Mensagem("ERRO", "Falha ao cancelar a inscrição! Entre em contato com o administrador!");
            AppendLog("Falha ao cancelar a inscrição");
            return "";
        }
    }

    public void processaQuestionario(Inscricao i, Questionario qr) {

        //i = inscricaoDAO.Refresh(i);
        QuestionarioResposta resposta = i.getResposta();

        if (resposta == null) {
            resposta = new QuestionarioResposta();
            resposta.setPessoa(getUsuarioCorrente());
            resposta.setQuestionario(qr);
        } else {
            resposta = respostaDAO.Refresh(resposta);
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

    public void processaQuestionarioEvento(Inscricao i) {
        processaQuestionario(i, i.getEvento().getQuestionario());
    }

    public void processaQuestionarioEvento() {
        processaQuestionario(getInscricao(), getInscricao().getEvento().getQuestionario());
    }

    public void processaQuestionarioAtividade(Inscricao i, InscricaoItem it) {
        processaQuestionario(it, it.getAtividade().getQuestionario());
    }

    public void processaQuestionarioAtividade() {
        processaQuestionario(getInscricaoItem(), getInscricaoItem().getAtividade().getQuestionario());
    }

    public void arquivoFileUpload(FileUploadEvent evt) {
        try {

            inscricao = inscricaoDAO.Refresh(inscricao);

            String key = evt.getComponent().getClientId();

            boolean item = key.contains("item-");

            Inscricao i = item ? getInscricaoItem() : inscricao;

            if (i == null) {
                if (item) {
                    inscreverAtividade();
                } else {
                    inscreverEvento();
                }

                i = item ? inscricaoItem : inscricao;
            }

            QuestionarioResposta resposta = i.getResposta();

            if (resposta == null) {
                resposta = new QuestionarioResposta();
                resposta.setPessoa(getUsuarioCorrente());
                resposta.setQuestionario(questionario);
            } else {
                resposta = respostaDAO.Refresh(resposta);
            }

            Arquivo arq = arqDAO.Salvar(evt.getFile().getInputstream(), evt.getFile().getFileName(),
                    getConfiguracao("DIRETORIO_ARQUIVOS"), getUsuarioCorrente());

            String idQuestao = key.substring(key.lastIndexOf("-") + 1);
            Long id = Long.parseLong(idQuestao);
            Questao q = questionarioDAO.AbrirQuestao(id);
            QuestaoResposta r = resposta.RespostaDeQuestao(q);

            if (r == null) {
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
    
    
    @Override
    public void enviarMensagem() {
       String tmp = getMensagem();
       List<Pessoa> admin = new ArrayList<Pessoa>();
       
       tmp = tmp + "\n" +getUsuarioCorrente().toString();
       
       if(getAtividade() != null){
           tmp = tmp + "\n" + "Atividade: " + getAtividade().getNome();
           admin.addAll(getAtividade().getResponsaveis());
       }
       
       if(getEvento() != null){
           tmp = tmp + "\n" + "Evento: " + getEvento().getNome();
           if(admin.isEmpty())
               admin.addAll(getEvento().getResponsaveis());
       }
       
       
       if(getInscricao() != null ){
           tmp = tmp + "\n" + "Inscrição: " + getInscricao().getId().toString();
       }
       setAssunto("[SGE]" + getAssunto());
       setMensagem(tmp);
       
       if(admin.isEmpty()){
           admin.add(pessoaDAO.Abrir("petronio.candido@gmail.com"));
       }
        setDestinatarios(admin);
       super.enviarMensagem();
    }
    
    public List<Atividade> getAtividadesPublicas() {
        return atividadeDAO.Join("tipo", "t")
                .IgualA("t.publico", true)
                .IgualA("evento", getEvento())
                .Ordenar("nome", "ASC")
                .Buscar();
    }

}
