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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author petronio
 */
public class AtividadeCSVExporter extends CSVExporter<Atividade> {

    DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm");
    
    @Override
    protected StringBuilder gerarLinha(Atividade obj) {
        return new StringBuilder(limparTexto(obj.getNome())).append(";")
                .append(obj.getDescricao()!= null ? limparTexto(obj.getDescricao()) : "").append(";")
                .append(obj.getTipo()!= null ? limparTexto(obj.getTipo().getNome()): "").append(";")
                .append(obj.getInicio()!= null ?  df.format(obj.getInicio()) : "").append(";")
                .append(obj.getTermino()!= null ? df.format(obj.getTermino()) : "").append(";")
                .append(obj.getLocal() != null ? limparTexto(obj.getLocal().getNome()) : "").append(";")
                .append(obj.getCargaHoraria() > 0 ?  Integer.toString(obj.getCargaHoraria()) : "0").append(";")
                .append(obj.getValorInscricao() != null ? obj.getValorInscricao().toString() : "0").append(";")
                .append(obj.getNumeroVagas()> 0 ?  Integer.toString(obj.getNumeroVagas()) : "0").append(";")
                .append(obj.getInicioInscricao()!= null ? df.format(obj.getInicioInscricao() ) : "").append(";")
                .append(obj.getTerminoInscricao()!= null ? df.format(obj.getTerminoInscricao()) : "").append(";")
                .append(obj.getResponsavelPrincipal()!= null ? obj.getResponsavelPrincipal().getNome() : "").append(";");
    }

    @Override
    protected StringBuilder gerarCabecalho(Atividade obj) {
        return new StringBuilder("codigo;nome;descricao;tipo;inicio;termino;local;cargaHoraria;valorInscricao;numeroVagas"
                + "inscricaoInicio;inscricaoTermino;responsavelPrincipal");
        
    }
    
}
