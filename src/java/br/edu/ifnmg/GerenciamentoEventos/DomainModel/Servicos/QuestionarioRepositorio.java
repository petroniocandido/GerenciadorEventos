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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface QuestionarioRepositorio extends Repositorio<Questionario> {
    void Salvar(QuestionarioSecao obj);
    QuestionarioSecao AbrirSecao(Long id);
    List<QuestionarioSecao> Buscar(QuestionarioSecao obj);
    void Salvar(Questao obj);
    Questao AbrirQuestao(Long id);
    List<Questao> Buscar(Questao obj);
}
