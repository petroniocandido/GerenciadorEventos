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
package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author petronio
 * @param <T>
 */
public abstract class ControllerBaseRelatorio<T extends Entidade> extends ControllerBase {

    @EJB
    EventoRepositorio daoEvento;

    
    private String relatorio;
    private String arquivoSaida;

    Evento evento;

    private List<T> dados;

    protected abstract Map<String, Object> carregaParametros();

    public abstract List<T> getDados();

    public Map<String, Object> getParametrosComuns() throws MalformedURLException {
        HashMap<String, Object> par = new HashMap<>();
        String tmp = getConfiguracao("DIRETORIO_ARQUIVOS") + evento.getBanner().getUri();
        par.put("banner", tmp);
        par.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));  
        return par;
    }

    public void executaRelatorioPDF() throws JRException, IOException {

        InputStream reportStream = null;
        try {
            reportStream = getClass().getResourceAsStream(relatorio);
            //JasperReport report = JasperCompileManager.compileReport(reportStream);

            JRDataSource ds = new JRBeanCollectionDataSource(getDados(), true);
            //JasperPrint jasperPrint = JasperFillManager.fillReport(report, carregaParametros(), ds);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, carregaParametros(), ds);

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + getArquivoSaida() + ".pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (JRException ex) {
            Logger.getLogger(ControllerBaseRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(String relatorio) {
        this.relatorio = relatorio;
    }

    public String getArquivoSaida() {
        return arquivoSaida;
    }

    public void setArquivoSaida(String arquivoSaida) {
        this.arquivoSaida = arquivoSaida;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    @PostConstruct
    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        evento = daoEvento.Abrir(Long.parseLong(evt));
    }

}
