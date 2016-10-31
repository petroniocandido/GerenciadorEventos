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



import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
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
@Named(value = "certificadoAvaliadorController")
@RequestScoped
public class CertificadoAvaliadorController
        extends ControllerBaseRelatorio<Evento>
        implements Serializable {

    
    /**
     * Creates a new instance of FuncionarioBean
     */
    public CertificadoAvaliadorController() {
        setArquivoSaida("CertificadoAvaliador");
        setRelatorio("CertificadoAvaliador.jasper");
    }
    
    @Override
    protected Map<String, Object> carregaParametros() {
        try {
            
            Map<String, Object> tmp = getParametrosComuns();
            tmp.put("background", getConfiguracao("DIRETORIO_ARQUIVOS") + getEvento().getCertificadoFundo().getUri());
            tmp.put("assinatura1", getConfiguracao("DIRETORIO_ARQUIVOS") + getEvento().getCertificadoAssinatura1().getUri());
            tmp.put("assinatura2", getConfiguracao("DIRETORIO_ARQUIVOS") + getEvento().getCertificadoAssinatura2().getUri());
            tmp.put("data", new Date());
            tmp.put("usuario", getUsuarioCorrente().getNome());
            return tmp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListaPresencaEventoController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public List<Evento> getDados() {
        List<Evento> tmp = new ArrayList<>();
        tmp.add(getEvento());
        return tmp;
    }

   
}
