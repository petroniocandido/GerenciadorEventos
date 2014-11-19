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
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Relatorios;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Date;
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
@Named(value = "lancamentosController")
@RequestScoped
public class LancamentosController
        extends ControllerBaseRelatorio<Lancamento>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LancamentosController() {
        setArquivoSaida("Lancamentos");
        setRelatorio("Lancamentos.jasper");
        filtro = new Lancamento();
        inicio = new Date();
        termino = new Date();
    }
    
    @EJB
    LancamentoRepositorio daoLancamento;
    
    Lancamento filtro;
    
    Date inicio;
    
    Date termino;
    
             
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
    public List<Lancamento> getDados() {
        return daoLancamento
                .IgualA("evento", getEvento())
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("status", filtro.getStatus())
                .IgualA("tipo", filtro.getTipo())
                .IgualA("categoria", filtro.getCategoria())
                .IgualA("cliente", filtro.getCliente())
                .IgualA("usuarioBaixa", filtro.getUsuarioBaixa())
                .MaiorOuIgualA("criacao", inicio)
                .MenorOuIgualA("criacao", termino)
                .Ordenar("categoria", "ASC")
                .Buscar();
    }

    public Lancamento getFiltro() {
        return filtro;
    }

    public void setFiltro(Lancamento filtro) {
        this.filtro = filtro;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }
    
    
}
