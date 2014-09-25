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

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.CSVImporter;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.PessoaCSVImporter;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author petronio
 */
@Named(value = "csvImporterController")
@RequestScoped
public class csvImporterController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public csvImporterController() {

    }
    @EJB
    PessoaRepositorio daoP;

    UploadedFile arquivo;

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }
    
    
    
    public void processaArquivo(){
        String val;

        BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(arquivo.getInputstream()));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            
            CSVImporter imp = new PessoaCSVImporter();
            
            List<Pessoa> tmp = imp.importarCSV(sb.toString());
            
            for(Pessoa p : tmp){
                if(daoP.Salvar(p))
                    Mensagem(p.getNome() + "Salvo com sucesso", p.getNome() + "Salvo com sucesso");
                else
                    Mensagem(p.getNome() + " - Falhou", p.getNome() + " - Falhou");
            }
                 

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
