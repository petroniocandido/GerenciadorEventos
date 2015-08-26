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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestaoResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioResposta;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;

/**
 *
 * @author petronio
 */
public class InscricaoRespostaCSVExporter extends CSVExporter<Inscricao> {

    @Override
    protected StringBuilder gerarCabecalho(Inscricao obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("pessoa;email;cpf;inscricao;");

        if (obj.getEvento() == null) {
            return sb;
        }

        for (QuestionarioSecao s : obj.getEvento().getQuestionario().getSecoes()) {
            for (Questao q : s.getQuestoes()) {
                sb.append(q.getEnunciado()).append(";");
            }
        }

        return sb;
    }

    @Override
    protected StringBuilder gerarLinha(Inscricao obj) {
        StringBuilder sb = new StringBuilder();
        try {
            if (obj.getResposta() == null) {
                return sb;
            }

            QuestionarioResposta resp = obj.getResposta();

            sb.append(limparTexto(obj.getPessoa().getNome()))
                    .append(";").append(limparTexto(obj.getPessoa().getEmail()))
                    .append(";").append(limparTexto(obj.getPessoa().getCpf()))
                    .append(";").append(limparTexto(obj.getId().toString()))
                    .append(";");
            for (QuestionarioSecao s : resp.getQuestionario().getSecoes()) {
                for (Questao q : s.getQuestoes()) {
                    QuestaoResposta qr = resp.RespostaDeQuestao(q);
                    if (qr != null) {
                        String tmp = qr.getValor();
                        sb.append(tmp == null ? "" : limparTexto(tmp));
                    }
                    sb.append(";");
                }
            }
        } catch (Exception ex) {
            sb.append(obj.toString());
        }
        return sb;
    }

}
