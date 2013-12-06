/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class AtividadeDAO 
    extends DAOGenerico<Atividade> 
    implements AtividadeRepositorio {
    
    public AtividadeDAO(){
        super(Atividade.class);
    }

    @Override
    public List<Atividade> Buscar(Atividade filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .IgualA("responsavel", filtro.getResponsavel())
                .IgualA("evento", filtro.getEvento())
                .IgualA("inicio", filtro.getInicio())
                .IgualA("local", filtro.getLocal())
                .Like("descricao", filtro.getDescricao())
                .IgualA("status", filtro.getStatus())
                .IgualA("termino", filtro.getTermino())
                .IgualA("tipo", filtro.getTipo())
                .Buscar();
    }
}
