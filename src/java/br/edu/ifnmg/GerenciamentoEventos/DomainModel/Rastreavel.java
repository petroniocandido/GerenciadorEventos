/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.util.Date;

/**
 *
 * @author petronio
 */
public interface Rastreavel {
    public Pessoa getCriador();
    public Date getDataCriacao();
    public Pessoa getUltimoAlterador();
    public Date getDataUltimaAlteracao();
}
