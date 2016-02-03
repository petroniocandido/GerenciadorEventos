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

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.DomainModel.Services.Repositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.DomainModel.Pessoa;
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
    public List<Atividade> Responsavel(Pessoa obj);
    public List<Atividade> Responsavel(Evento e, Pessoa obj);
    public List<AtividadeTipo> BuscarTextoTipo(String filtro);
}
