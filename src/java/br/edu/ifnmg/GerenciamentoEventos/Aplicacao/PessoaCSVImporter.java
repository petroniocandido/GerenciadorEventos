/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.PessoaTipo;
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
            if(cabecalho.containsKey("dataNascimento"))
                obj.setDataNascimento(df.parse(colunas[cabecalho.get("dataNascimento")]));
        } catch (ParseException ex) {
            Logger.getLogger(PessoaCSVImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            if(cabecalho.containsKey("tipo"))
                obj.setTipo(PessoaTipo.valueOf(colunas[cabecalho.get("tipo")]));
        } catch (Exception ex) {
            Logger.getLogger(PessoaCSVImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        obj.setSenha("123");
        return obj;
    }
    
}
