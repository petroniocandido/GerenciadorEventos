/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
