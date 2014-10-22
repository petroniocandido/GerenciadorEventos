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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;

/**
 *
 * @author petronio
 */
public class InscricaoItemRespostaCSVExporter extends CSVExporter<InscricaoItem>{

    @Override
    protected StringBuilder gerarCabecalho(InscricaoItem obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("pessoa;email;cpf;inscricao;");
        for(QuestionarioSecao s : obj.getResposta().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes())
                sb.append(limparTexto(q.getEnunciado())).append(";");
        
        return sb;
    }

    @Override
    protected StringBuilder gerarLinha(InscricaoItem obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj.getPessoa().getNome())
                .append(";").append(limparTexto(obj.getPessoa().getEmail()))
                .append(";").append(limparTexto(obj.getPessoa().getCpf()))
                .append(";").append(limparTexto(obj.getInscricao().getId().toString()))
                .append(";");
        for(QuestionarioSecao s : obj.getResposta().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes()){
                QuestaoResposta qr = obj.getResposta().RespostaDeQuestao(q);
                if(qr != null){
                    String tmp = qr.getValor();
                    sb.append(tmp == null ? "" : limparTexto(tmp));
                }
                sb.append(";");
            }
        
        return sb;
    }
    
}
