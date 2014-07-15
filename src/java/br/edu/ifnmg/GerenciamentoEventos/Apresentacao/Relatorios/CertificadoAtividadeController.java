/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Relatorios;



import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "certificadoAtividadeController")
@RequestScoped
public class CertificadoAtividadeController
        extends ControllerBaseRelatorio<InscricaoItem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public CertificadoAtividadeController() {
        setArquivoSaida("certificadoAtividade");
        setRelatorio("CertificadoAtividade.jasper");
    }
    
    Inscricao inscricao;
    
    Atividade atividade;
    
    String funcao;
             
    @Override
    protected Map<String, Object> carregaParametros() {
        try {
            
            Map<String, Object> tmp = getParametrosComuns();
            tmp.put("background", getConfiguracao("DIRETORIO_ARQUIVOS") + getEvento().getCertificadoFundo().getUri());
            tmp.put("assinatura1", getConfiguracao("DIRETORIO_ARQUIVOS") + getEvento().getCertificadoAssinatura1().getUri());
            tmp.put("assinatura2", getConfiguracao("DIRETORIO_ARQUIVOS") + getEvento().getCertificadoAssinatura2().getUri());
            tmp.put("data", new Date());
            tmp.put("funcao", funcao);
            return tmp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListaPresencaEventoController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public List<InscricaoItem> getDados() {
        List<InscricaoItem> tmp = new ArrayList<>();
        tmp.add(inscricao.getItem(atividade));
        return tmp;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
        setEvento(inscricao.getEvento());
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    
    
}
