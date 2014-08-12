/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class InscricaoRespostaCSVExporter extends CSVExporter<Inscricao>{

    @Override
    protected StringBuilder gerarCabecalho(Inscricao obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("pessoa;email;");
        
        if(obj.getEvento() == null)
            return sb;
        
        for(QuestionarioSecao s : obj.getEvento().getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes())
                sb.append(q.getEnunciado()).append(";");
        
        return sb;
    }

    @Override
    protected StringBuilder gerarLinha(Inscricao obj) {
        StringBuilder sb = new StringBuilder();
        if(obj.getResposta() == null) 
            return sb;
        
        QuestionarioResposta resp = obj.getResposta();
        
        sb.append(obj.getPessoa().getNome()).append(";").append(obj.getPessoa().getEmail()).append(";");
        for(QuestionarioSecao s : resp.getQuestionario().getSecoes())        
            for(Questao q : s.getQuestoes()){
                QuestaoResposta qr = resp.RespostaDeQuestao(q);
                if(qr != null){
                    String tmp = qr.getValor();
                    sb.append(tmp == null ? "" : tmp);
                }
                sb.append(";");
            }
        
        return sb;
    }
    
}
