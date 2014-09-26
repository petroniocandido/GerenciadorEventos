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

import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.PessoaTipo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petronio
 */
public class PessoaCSVImporter extends CSVImporter<Pessoa> {

    DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm");
    
    @Override
    protected Pessoa gerarObjeto(String linha) {
        String colunas[] = linha.split(";");
        Pessoa obj = new Pessoa();
        obj.setNome(colunas[cabecalho.get("nome")]);
        if(cabecalho.containsKey("cpf"))
            obj.setCpf(colunas[cabecalho.get("cpf")]);
        if(cabecalho.containsKey("email"))
            obj.setEmail(colunas[cabecalho.get("email")]);
        if(cabecalho.containsKey("telefone"))
            obj.setTelefone(colunas[cabecalho.get("telefone")]);
        
        try {
            obj.setDataNascimento(df.parse(colunas[cabecalho.get("dataNascimento")]));
        } catch (ParseException ex) {
            Logger.getLogger(PessoaCSVImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            obj.setTipo(PessoaTipo.valueOf(colunas[cabecalho.get("tipo")]));
        } catch (Exception ex) {
            Logger.getLogger(PessoaCSVImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }
    
}
