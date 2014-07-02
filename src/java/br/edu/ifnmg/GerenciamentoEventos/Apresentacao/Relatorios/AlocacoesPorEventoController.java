/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Relatorios;

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Alocacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AlocacaoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "alocacoesPorEventoController")
@RequestScoped
public class AlocacoesPorEventoController
        extends ControllerBaseRelatorio<Alocacao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AlocacoesPorEventoController() {
        setArquivoSaida("AlocacoesPorEvento");
        setRelatorio("AlocacoesPorEvento.jasper");
        filtro = new Alocacao();
    }

    @EJB
    AlocacaoRepositorio daoAlocacao;

    Alocacao filtro;

    @Override
    protected Map<String, Object> carregaParametros() {
        try {

            Map<String, Object> tmp = getParametrosComuns();
            tmp.put("data", new Date());
            return tmp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListaPresencaEventoController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public List<Alocacao> getDados() {
        if (filtro.getInicio() != null) {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(filtro.getInicio());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            daoAlocacao.MenorOuIgualA("inicio", cal.getTime());
        }
        return daoAlocacao
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("evento", filtro.getEvento())
                .IgualA("responsavel", filtro.getResponsavel())
                .MaiorOuIgualA("inicio", filtro.getInicio())
                
                // .Join("atividade", "a")
                // .Ordenar("a.inicio", "ASC")
                .Buscar();
    }

    public Alocacao getFiltro() {
        return filtro;
    }

    public void setFiltro(Alocacao filtro) {
        this.filtro = filtro;
    }

}
