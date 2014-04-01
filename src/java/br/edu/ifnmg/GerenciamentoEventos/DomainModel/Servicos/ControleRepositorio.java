/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Controle;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface ControleRepositorio extends Repositorio<Controle> {
    public Controle Abrir(Evento e);
    public Controle Abrir(Atividade a);
}
