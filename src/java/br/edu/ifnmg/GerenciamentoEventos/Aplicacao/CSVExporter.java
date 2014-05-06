/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author petronio
 */
public abstract class CSVExporter<T> {
    protected abstract StringBuilder gerarCabecalho(T obj);
    protected abstract StringBuilder gerarLinha(T obj);
    
    public String gerarCSV(Collection<T> colecao){
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = colecao.iterator();
        T obj = it.next();
        sb.append(gerarCabecalho(obj)).append('\n');
        sb.append(gerarLinha(obj)).append('\n');
        while(it.hasNext()){
            sb.append(gerarLinha(it.next())).append('\n');
        }
        return sb.toString();
    }
}
