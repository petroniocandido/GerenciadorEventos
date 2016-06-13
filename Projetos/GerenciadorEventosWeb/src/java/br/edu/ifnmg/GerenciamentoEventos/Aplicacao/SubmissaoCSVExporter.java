/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;

/**
 *
 * @author petronio
 */
public class SubmissaoCSVExporter extends CSVExporter<Submissao> {

    @Override
    protected StringBuilder gerarLinha(Submissao obj) {
        String ap = "";
        if(obj.getAreasConhecimento() != null && !obj.getAreasConhecimento().isEmpty())
            ap = checknull( limparTexto(obj.getAreasConhecimento().get(0).toString()));
        
        InscricaoItem it = (InscricaoItem) obj.getInscricao();
        
        String cp = "";
        String at = "";
        String its = "";
        Pessoa p = new Pessoa();
        
        if(it != null) {
            
            its = it.getId().toString();
            
           p = it.getPessoa();
           
           if(it.getAtividade() != null)
                at = limparTexto(checknull(it.getAtividade().getNome()));
           if(it.getPessoa().getCampus() != null)
               cp = it.getPessoa().getCampus().getNome();
        }
        return new StringBuilder(ap).append(";")
                .append(cp).append(";")
                .append(at).append(";")
                .append(limparTexto(checknull(p.getNome()))).append(";")
                .append(limparTexto(checknull(p.getEmail()))).append(";")
                .append(its).append(";")
                .append(limparTexto(checknull(obj.getTitulo()))).append(";")
                .append(limparTexto(checknull(obj.autores()))).append(";")
                .append(limparTexto(checknull(obj.palavrasChave()))).append(";")
                .append(limparTexto(checknull(obj.areasConhecimento()))).append(";")
                .append(limparTexto(checknull(obj.getStatus().getDescricao()))).append(";");
    }

    @Override
    protected StringBuilder gerarCabecalho(Submissao obj) {
        return new StringBuilder("areaprincipal;campus;atividade;pessoa;email;inscricao;titulo;autores;palavraschave;areas;status");
    }
    
}
