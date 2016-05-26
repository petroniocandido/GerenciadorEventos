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
        
        String sx = "";
        if(obj.getSexo() != null)
            sx = obj.getSexo().name();
        
        String tr = "";
        if(obj.getTratamento() != null)
            sx = obj.getTratamento().getDescricao();
        
        String cp = "";
        if(obj.getCampus() != null)
            cp = obj.getCampus().getNome();
        
        String tm = "";
        if(obj.getTitulacaoMaxima() != null)
            tm = obj.getTitulacaoMaxima().getDescricao();
        
        String at = "";
        if(obj.getAtuacao() != null)
            at = obj.getAtuacao().getDescricao();
        
        return new StringBuilder(limparTexto(obj.getNome())).append(";")
                .append(obj.getCpf() != null ? limparTexto(obj.getCpf()) : "").append(";")
                .append(obj.getTelefone() != null ? limparTexto(obj.getTelefone()): "").append(";")
                .append(obj.getEmail() != null ? limparTexto(obj.getEmail()) : "").append(";")
                .append(obj.getDataNascimento() != null ? df.format(obj.getDataNascimento()) : "").append(";")
                .append(obj.getPerfil() != null ? limparTexto(obj.getPerfil().getId().toString()) : "").append(";")
                .append(obj.getTipo() != null ? limparTexto(obj.getTipo().name()) : "").append(";")
                .append(limparTexto(sx)).append(";")
                .append(limparTexto(tr)).append(";")
                .append(limparTexto(cp)).append(";")
                .append(limparTexto(tm)).append(";")
                .append(limparTexto(at)).append(";");
    }

    @Override
    protected StringBuilder gerarCabecalho(Pessoa obj) {
        return new StringBuilder("nome;cpf;telefone;email;dataNascimento;perfil;tipo;sexo;tratamento;campus;titulacao;atuacao;");
        
    }
    
}
