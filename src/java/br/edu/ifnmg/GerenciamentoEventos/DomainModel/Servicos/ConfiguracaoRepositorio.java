/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;

/**
 *
 * @author petronio
 */
public interface ConfiguracaoRepositorio extends Repositorio<Configuracao> {
    public Configuracao Abrir(String chave);
}
