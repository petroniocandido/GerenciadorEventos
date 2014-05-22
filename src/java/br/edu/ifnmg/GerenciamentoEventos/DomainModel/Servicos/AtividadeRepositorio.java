/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface AtividadeRepositorio extends Repositorio<Atividade> {
    public boolean SalvarTipo(AtividadeTipo obj);
    public boolean ApagarTipo(AtividadeTipo obj);
    public AtividadeTipo AbrirTipo(Long id);
    public List<AtividadeTipo> BuscarTipo(AtividadeTipo obj);
    public List<Atividade> BuscarAtividadesDoUsuario(Pessoa obj);
    public List<Atividade> BuscarAtividadesPorEventoETipo(Evento e, AtividadeTipo t);
    public List<AtividadeTipo> BuscarAtividadesTiposPorEvento(Evento e);
}
