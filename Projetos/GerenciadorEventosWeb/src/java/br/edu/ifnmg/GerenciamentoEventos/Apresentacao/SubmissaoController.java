/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Arquivo;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.AreaConhecimentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRespostaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.SubmissaoAvaliacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.SubmissaoStatus;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Isla Guedes
 */
@Named(value = "submissaoController")
@RequestScoped
public class SubmissaoController
        extends ControllerBaseEntidade<Submissao> implements Serializable {

    /**
     * Creates a new instance of SubmissaoController
     */
    public SubmissaoController() {
    }

    @EJB
    SubmissaoRepositorio dao;

    @EJB
    InscricaoRepositorio daoInsc;

    @EJB
    EventoRepositorio daoEvt;

    @EJB
    AtividadeRepositorio daoAtiv;

    @EJB
    AreaConhecimentoRepositorio daoArea;

    @EJB
    QuestionarioRepositorio questionarioDAO;

    @EJB
    QuestionarioRespostaRepositorio respostaDAO;
    
    @EJB
    SubmissaoService service;

    AreaConhecimento areaConhecimento;

    String palavraChave;

    Evento evento;

    Atividade atividade;
    
    Pessoa avaliador;

    @Override
    public Submissao getFiltro() {
        if (filtro == null) {
            filtro = new Submissao();
            String stat = getSessao("sbctrl_status");
            if (stat != null) {
                filtro.setStatus(SubmissaoStatus.valueOf(stat));
            }
            filtro.setTitulo(getSessao("sbctrl_titulo"));
            filtro.setAutor1(getSessao("sbctrl_autor1"));
            filtro.setAutor2(getSessao("sbctrl_autor2"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Submissao filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("sbctrl_status", filtro.getStatus().toString());
            setSessao("sbctrl_titulo", filtro.getTitulo());
            setSessao("sbctrl_autor1", filtro.getAutor1());
            setSessao("sbctrl_autor2", filtro.getAutor2());
        }
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarSubmissao.xhtml");
        setPaginaListagem("listagemSubmissoes.xhtml");
        checaEventoPadrao();
    }
    
     public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null ) {
            Evento padrao = daoEvt.Abrir(Long.parseLong(evt));
            setEvento(padrao);
        }
    }

    InscricaoItem inscricaoItem;

    public InscricaoItem getInscricao() {
        if (inscricaoItem == null) {
            inscricaoItem = (InscricaoItem) getSessao("inscricaoItem", daoInsc);
        }
        return inscricaoItem;
    }

    public String criaSubmissaoAtividade() {

        if (getInscricao() == null) {
            return "inscricaoAtividade.xhtml";
        }

        novo();

        return "submissao.xhtml";
    }

    @Override
    public List<Submissao> getListagem() {
        if (getFiltro().getStatus() != null || getEvento() != null || getAtividade() != null) {
            return dao.Buscar(getFiltro(), getEvento(), getAtividade());
        } else {
            return dao.BuscarTexto(getFiltro().getTitulo());
        }
    }

    @Override
    public void limpar() {
        setEntidade(new Submissao());
    }

    public SubmissaoStatus[] getStatus() {
        return SubmissaoStatus.values();
    }

    @Override
    public void salvar() {

        if (getInscricao() != null) {
            getEntidade().setInscricao(getInscricao());
        }

        super.salvar();

        checagem();

        setEntidade(entidade);

    }

    public String concluir() {

        if (checagem()) {
            entidade.pendente();
            super.salvar();
            return "minhasSubmissoes.xhtml";
        } else {
            return "";
        }

    }

    public boolean checagem() {
        if (!entidade.hasAreaConhecimento()) {
            Mensagem("Falha", "Você precisa adicionar pelo menos uma Área de conhecimento!");
            return false;
        }

        if (!entidade.hasPalavrasChave()) {
            Mensagem("Falha", "Você precisa adicionar pelo menos uma Palavra-Chave!");
            return false;
        }

        if (!entidade.hasMinimoDeArquivos()) {
            Mensagem("Falha", "Você precisa adicionar arquivos!");
            return false;
        }

        if (!entidade.hasMinimoDeAutores()) {
            Mensagem("Falha", "Você precisa adicionar autores!");
            return false;
        }

        return true;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }
    
    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public void addAreaConhecimento() {
        if (getEntidade().getAreasConhecimento().size() <= 2) {
            getEntidade().add(areaConhecimento);
            if (dao.Salvar(entidade)) {
                Mensagem("Sucesso", "Área de conhecimento adicionada com êxito!");
            } else {
                Mensagem("Falha", "Falha ao adicionar Área de conhecimento!");
                AppendLog("Erro ao anexar a Área de conhecimento " + String.valueOf(areaConhecimento) + " a submissão " + entidade + ":" + dao.getErro());
            }
            areaConhecimento = null;
        } else {
            Mensagem("Atenção", "Devem ser cadastradas no máximo duas áreas de conhecimento!");
        }
    }

    public void removeAreaConhecimento() {
        getEntidade().remove(areaConhecimento);
        dao.Salvar(entidade);
        areaConhecimento = null;
    }

    public String getPalavraChave() {
        return palavraChave;
    }

    public void setPalavraChave(String palavraChave) {
        this.palavraChave = palavraChave;
    }

    public void addPalavraChave() {
        if (getEntidade().getPalavraschave().size() <= 5) {
            getEntidade().add(palavraChave);
            if (dao.Salvar(entidade)) {
                Mensagem("Sucesso", "Palavra chave adicionada com êxito!");
            } else {
                Mensagem("Falha", "Falha ao adicionar palavra chave!");
                AppendLog("Erro ao anexar a palavra chave " + String.valueOf(palavraChave) + " a submissão " + entidade + ":" + dao.getErro());
            }
            palavraChave = null;
        } else {
            Mensagem("Atenção", "Devem ser cadastradas no máximo 5 palavras-chave!");
        }
    }

    public void removePalavraChave() {
        getEntidade().remove(palavraChave);
        dao.Salvar(entidade);
        palavraChave = null;
    }

    public void fileUploadListener(FileUploadEvent event) {
        entidade = dao.Refresh(getEntidade());
        Arquivo tmp = criaArquivo(event.getFile());
        String arq = (String) event.getComponent().getAttributes().get("arquivo");
        switch (arq) {
            case "Arquivo Identificado":
                entidade.setArquivo1(tmp);
                break;
            case "Arquivo Não Identificado":
                entidade.setArquivo2(tmp);
                break;
        }

        if (dao.Salvar(entidade)) {
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + tmp + " a submissão " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + tmp + " a submissão " + entidade + ":" + dao.getErro());
        }
    }

    public GenericDataModel getPublicoDataModel() {
        return new GenericDataModel<>(getPublicoListagem(), repositorio);
    }
    
    public GenericDataModel getAvaliadorDataModel() {
        return new GenericDataModel<>(getAvaliadorListagem(), repositorio);
    }

    public List<Submissao> getPublicoListagem() {
        InscricaoItem inscricaoItem = (InscricaoItem) getSessao("inscricaoItem", daoInsc);
        if (inscricaoItem == null) {
            return null;
        }

        return dao.IgualA("inscricao", inscricaoItem).Buscar();
    }
    
    public List<Submissao> getAvaliadorListagem() {
        return dao.PorAvaliador(SubmissaoStatus.Atribuido, getEvento(), getUsuarioCorrente());
    }

    public void onPublicoRowSelect(SelectEvent event) {
        try {
            Submissao obj = (Submissao) event.getObject();
            setId(obj.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("submissao.xhtml");
        } catch (IOException ex) {
            Mensagem("Falha", "Falha ao abrir submissão!");
            AppendLog("Falha ao abrir submissão:" + ex);
        }
    }

    public String abreAvaliacao() {
        setSessao("inscricaoItem", getEntidade().getInscricao());
        return "submissaoAvaliar.xhtml";
    }

    public QuestionarioResposta processaQuestionario() {

        //i = inscricaoDAO.Refresh(i);
        QuestionarioResposta resposta = getEntidade().getResposta();

        if (resposta == null) {
            Questionario qr = ((InscricaoItem) getEntidade().getInscricao()).getAtividade().getQuestionario();

            resposta = new QuestionarioResposta(getUsuarioCorrente(), qr);
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
        //respostaDAO.Salvar(resposta);

        return resposta;

    }

    public Evento getEvento() {
        if (evento == null) {
            evento = (Evento) getSessao("sbctrl_evt", daoEvt);
        }
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
        setSessao("sbctrl_evt", evento);
    }

    public Atividade getAtividade() {
        if (atividade == null) {
            atividade = (Atividade) getSessao("sbctrl_ativ", daoAtiv);
        }
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
        setSessao("sbctrl_ativ", atividade);
    }

    public void reindexarAreasConhecimento() {
        for (Submissao s : dao.Buscar()) {
            if (!s.getAreasConhecimento().isEmpty()) {
                List<AreaConhecimento> remover = new ArrayList<>();
                List<AreaConhecimento> adicionar = new ArrayList<>();
                for (AreaConhecimento a : s.getAreasConhecimento()) {
                    if (!a.isArea() && !a.isGrandeArea()) {
                        remover.add(a);
                        AreaConhecimento tmp = daoArea.Abrir(a.getAreaCodigo());
                        adicionar.add(tmp);
                    }
                }
                for (AreaConhecimento c : remover) {
                    s.remove(c);
                }
                for (AreaConhecimento c : adicionar) {
                    s.add(c);
                }
                if (!dao.Salvar(s)) {
                    AppendLog(dao.getErro().getMessage());
                }
            }
        }
    }

    public Pessoa getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(Pessoa avaliador) {
        this.avaliador = avaliador;
    }
    
    public void addAvaliador() {
        getEntidade().add(avaliador);
        if(dao.Salvar(getEntidade())){
            Mensagem("Sucesso!", "Avaliador adicionado com êxito!");
        } else {
            Mensagem("Falha!", "Falha ao adicionar avaliador!");
        }
    }
    
    public void removeAvaliador() {
        getEntidade().remove(avaliador);
        if(dao.Salvar(getEntidade())){
            Mensagem("Sucesso!", "Avaliador removido com êxito!");
        } else {
            Mensagem("Falha!", "Falha ao remover avaliador!");
        }
    }
    
    public void distribuir() {
        service.Distribuir(getEvento());
    }
    
    private void processar(SubmissaoStatus status){
        QuestionarioResposta resposta = processaQuestionario();
        
        SubmissaoAvaliacao avaliacao = new SubmissaoAvaliacao();
        avaliacao.setAvaliador(getUsuarioCorrente());
        avaliacao.setStatus(status);
        avaliacao.setSubmissao(getEntidade());
        avaliacao.setResposta(resposta);
        
        getEntidade().add(avaliacao);
        
        if(dao.Salvar(getEntidade())){
            Mensagem("Sucesso!", "Avaliação registrada com êxito!");
            AppendLog("Avaliação registrada:" + getEntidade().getTitulo() + " - " + status);
        } else {
            Mensagem("Falha!", "Avaliação não registrada!");
            AppendLog("Avaliação não registrada:" + getEntidade().getTitulo() + " - " + status + " = " + dao.getErro());
        }
    }
    
    public void aprovar() {
        processar(SubmissaoStatus.Aprovado);        
    }
    
    public void reprovar() {
        processar(SubmissaoStatus.Reprovado); 
    }
    
    public void verificarStatus(){
        service.VerificarStatus(getEvento());
    }
    
    public List<Pessoa> getAutores() {
        List<Pessoa> tmp = new ArrayList<>();
        for(Submissao s : getListagem()){
            tmp.add(s.getInscricao().getPessoa());
        }
        return tmp;
    }
    
    public List<Pessoa> getAvaliadores() {
        List<Pessoa> tmp = new ArrayList<>();
        for(Submissao s : getListagem()){
            tmp.addAll(s.getAvaliadores());
        }
        return tmp;
    }

}
