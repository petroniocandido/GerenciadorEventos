/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Services.Repositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface SubmissaoRepositorio extends Repositorio<Submissao> {
    public List<Submissao> Buscar(Submissao filtro, Evento e, Atividade a);
    public List<AreaConhecimento> AreasPorEvento(Evento e);
}
