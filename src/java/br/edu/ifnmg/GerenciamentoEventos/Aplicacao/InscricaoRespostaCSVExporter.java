/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;

/**
 *
 * @author petronio
 */
public class InscricaoRespostaCSVExporter extends CSVExporter<Inscricao>{

    @Override
    protected StringBuilder gerarCabecalho(Inscricao obj) {
        StringBuilder sb = new StringBuilder();
        for(QuestionarioSecao s : obj.getResposta().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes())
                sb.append(q.getEnunciado()).append(";");
        
        return sb;
    }

    @Override
    protected StringBuilder gerarLinha(Inscricao obj) {
        StringBuilder sb = new StringBuilder();
        for(QuestionarioSecao s : obj.getResposta().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes())
                sb.append(obj.getResposta().RespostaDeQuestao(q).getValor()).append(";");
        
        return sb;
    }
    
}
