/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author petronio
 */
public class PessoaCSVExporter extends CSVExporter<Pessoa> {

    DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm");
    
    @Override
    protected StringBuilder gerarLinha(Pessoa obj) {
        return new StringBuilder(obj.getNome()).append(";")
                .append(obj.getCpf()).append(";")
                .append(obj.getTelefone()).append(";")
                .append(obj.getEmail()).append(";")
                .append(df.format(obj.getDataNascimento())).append(";")
                .append(obj.getPerfil().getId().toString()).append(";")
                .append(obj.getTipo()).append(";");
    }

    @Override
    protected StringBuilder gerarCabecalho(Pessoa obj) {
        return new StringBuilder("nome;cpf;telefone;email;dataNascimento;perfil;tipo;");
        
    }
    
}
