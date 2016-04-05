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



import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "listagemAvaliadoresController")
@RequestScoped
public class ListagemAvaliadores
        extends ControllerBaseRelatorio<Pessoa>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ListagemAvaliadores() {
        setArquivoSaida("ListagemAvaliadores");
        setRelatorio("ListagemAvaliadores.jasper");
    }
    
    @EJB
    PessoaRepositorioLocal dao;
    
    @EJB
    PerfilRepositorio daoP;
    
    @EJB
    EventoRepositorio daoE;
    
    @PostConstruct
    public void init() {
        checaEventoPadrao();
    }
    
     public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null ) {
            Evento padrao = daoE.Abrir(Long.parseLong(evt));
            setEvento(padrao);
        }
    }
            
    @Override
    protected Map<String, Object> carregaParametros() {
        try {
            
            Map<String, Object> tmp = getParametrosComuns();
            tmp.put("data", new Date());
            return tmp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListagemAvaliadores.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public Collection<Pessoa> getDados() {
                
        Collection<Pessoa> tmp = dao.
                IgualA("perfil", daoP.Abrir( Long.parseLong( getConfiguracao("PERFILSELECAOAVALIADOR") ) ))
                .Ordenar("nome", "ASC")
                .Buscar();
        
        return tmp;
    }
}
