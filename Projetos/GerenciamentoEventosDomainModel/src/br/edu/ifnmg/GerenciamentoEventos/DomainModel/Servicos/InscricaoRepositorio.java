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
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.DomainModel.Pessoa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface InscricaoRepositorio extends Repositorio<Inscricao> {
    public List<Inscricao> Buscar(Evento evt, String participante);
    public Inscricao Abrir(Evento evt, Pessoa p);
    public InscricaoItem Abrir(Inscricao i, Atividade a);
    public InscricaoItem AbrirItem(Long id);
    public List<InscricaoItem> Buscar(InscricaoItem filtro);
    public boolean Salvar(InscricaoItem i);
    public boolean Apagar(InscricaoItem i);
    public Repositorio<InscricaoItem> getRepositorioItem();
    public Long QuantidadeInscricoes(Inscricao i, Atividade a);
}