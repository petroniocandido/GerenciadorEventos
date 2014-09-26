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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author petronio
 */
public abstract class CSVImporter<T> {
    
    protected HashMap<String,Integer> cabecalho;
    
    private void gerarCabecalho(String csv){
        cabecalho = new HashMap<>();
        String colunas[] = csv.split(";");
        for(int index = 0; index < colunas.length; index++){
            cabecalho.put(colunas[index], index);
        }        
    }
    
    protected abstract T gerarObjeto(String linha);
    
    public List<T> importarCSV(String csv){
        String linhas[] = csv.split("\n");
        
        gerarCabecalho(linhas[0]);
        
        List<T> lista = new ArrayList<>();
        
        for(int linha = 1; linha < linhas.length; linha++){
            lista.add( gerarObjeto(linhas[linha]) );
        }
        
        return lista;
    }
}
