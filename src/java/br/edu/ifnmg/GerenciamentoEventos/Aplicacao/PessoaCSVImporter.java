/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petronio
 */
public class PessoaCSVImporter extends CSVImporter<Pessoa> {

    DateFormat df = DateFormat.getDateInstance();
    
    @Override
    protected Pessoa gerarObjeto(String linha) {
        String colunas[] = linha.split(";");
        Pessoa obj = new Pessoa();
        obj.setNome(colunas[cabecalho.get("nome")]);
        obj.setCpf(colunas[cabecalho.get("cpf")]);
        obj.setEmail(colunas[cabecalho.get("email")]);
        obj.setTelefone(colunas[cabecalho.get("telefone")]);
        try {
            obj.setDataNascimento(df.parse(colunas[cabecalho.get("datanascimento")]));
        } catch (ParseException ex) {
            Logger.getLogger(PessoaCSVImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj;
    }
    
}
