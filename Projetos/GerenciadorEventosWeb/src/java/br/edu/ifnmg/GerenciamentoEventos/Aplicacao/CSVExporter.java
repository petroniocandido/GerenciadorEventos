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
package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author petronio
 */
public abstract class CSVExporter<T> {

    protected abstract StringBuilder gerarLinha(T obj);

    protected abstract StringBuilder gerarCabecalho(T obj);

    protected String limparTexto(String input) {
        return "\"" + input.replace(";", ".").replace("\"", "") + "\"";
    }
    
    protected String checknull(String input) {
        return input == null ? "" : input;
    }

    public String gerarCSV(Collection<T> colecao) {
        StringBuilder sb = new StringBuilder();

        Iterator<T> it = colecao.iterator();
        T obj = it.next();
        sb.append(gerarCabecalho(obj)).append('\n');
        sb.append(gerarLinha(obj)).append('\n');
        while (it.hasNext()) {
            try {
                sb.append(gerarLinha(it.next())).append('\n');
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return sb.toString();
    }
}
