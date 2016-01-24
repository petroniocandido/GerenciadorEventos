/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import br.edu.ifnmg.DomainModel.Pessoa;
import java.util.Comparator;

/**
 *
 * @author petronio
 */
public class PessoaComparator implements Comparator<Pessoa> {

    @Override
    public int compare(Pessoa p1, Pessoa p2) {
        try {
            return p1.getNome().compareTo(p2.getNome());
        } catch (NullPointerException e) {
            return -1;
        }
    }
}
