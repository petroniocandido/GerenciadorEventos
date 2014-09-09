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

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.CSVExporter;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.InscricaoItemRespostaCSVExporter;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.InscricaoRespostaCSVExporter;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.PessoaCSVExporter;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author petronio
 */
@Named(value = "csvExporterController")
@RequestScoped
public class csvExporterController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public csvExporterController() {
        
    }

    @EJB
    EventoRepositorio dao;

    @EJB
    AtividadeRepositorio daoA;
    
    @EJB
    InscricaoRepositorio daoI;
    
    @EJB
    PessoaRepositorio daoP;

    Evento padrao;

    Atividade atividade;

    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            padrao = dao.Abrir(Long.parseLong(evt));
        }
    }

    public Evento getPadrao() {
        return padrao;
    }

    public void setPadrao(Evento padrao) {
        this.padrao = padrao;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public void exportaAtividade() {
        ServletOutputStream servletOutputStream = null;
        InscricaoItem tmp = new InscricaoItem();
        CSVExporter csv = new InscricaoItemRespostaCSVExporter();
        tmp.setAtividade(atividade);
        List<InscricaoItem> dados = daoI.Buscar(tmp);
        try {
            String arq = atividade.getNome().replace(" ", "");
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + arq + ".csv");
            servletOutputStream = httpServletResponse.getOutputStream();
            servletOutputStream.print(csv.gerarCSV(dados));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                servletOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void exportaEvento() {
        ServletOutputStream servletOutputStream = null;
        Inscricao tmp = new Inscricao();
        CSVExporter csv = new InscricaoRespostaCSVExporter();
        tmp.setEvento(padrao);
        List<Inscricao> dados = daoI.Buscar(tmp);
        try {
            String arq = padrao.getNome().replace(" ", "");
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + arq + ".csv");
            servletOutputStream = httpServletResponse.getOutputStream();
            servletOutputStream.print(csv.gerarCSV(dados));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                servletOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void exportaPessoasEvento() {
        ServletOutputStream servletOutputStream = null;
        CSVExporter csv = new PessoaCSVExporter();
        List<Pessoa> dados = daoP.Buscar(padrao);
        try {
            String arq = padrao.getNome().replace(" ", "");
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + arq + ".csv");
            servletOutputStream = httpServletResponse.getOutputStream();
            servletOutputStream.print(csv.gerarCSV(dados));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                servletOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void exportaPessoasAtividade() {
        ServletOutputStream servletOutputStream = null;
        CSVExporter csv = new PessoaCSVExporter();
        List<Pessoa> dados = daoP.Buscar(atividade);
        try {
            String arq = atividade.getNome().replace(" ", "");
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + arq + ".csv");
            servletOutputStream = httpServletResponse.getOutputStream();
            servletOutputStream.print(csv.gerarCSV(dados));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                servletOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(csvExporterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
