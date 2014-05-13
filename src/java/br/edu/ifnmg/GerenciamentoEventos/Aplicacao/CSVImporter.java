/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 *
 * @author petronio
 */
public abstract class CSVImporter<T> {
    
    protected Dictionary<String,Integer> cabecalho;
    
    protected abstract Dictionary<String,Integer> gerarCabecalho(String csv);
    protected abstract T gerarObjeto(String linha);
    
    public List<T> importarCSV(String csv){
        String linhas[] = csv.split("\n");
        
        cabecalho = gerarCabecalho(linhas[0]);
        
        List<T> lista = new ArrayList<>();
        
        for(int linha = 1; linha < linhas.length; linha++){
            lista.add( gerarObjeto(linhas[linha]) );
        }
        
        return lista;
    }
}
