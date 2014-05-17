/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        sb.append("pessoa;email;");
        for(QuestionarioSecao s : obj.getResposta().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes())
                sb.append(q.getEnunciado()).append(";");
        
        return sb;
    }

    @Override
    protected StringBuilder gerarLinha(InscricaoItem obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj.getPessoa().getNome()).append(";").append(obj.getPessoa().getEmail()).append(";");
        for(QuestionarioSecao s : obj.getResposta().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes())
                sb.append(obj.getResposta().RespostaDeQuestao(q).getValor()).append(";");
        
        return sb;
    }
    
}
