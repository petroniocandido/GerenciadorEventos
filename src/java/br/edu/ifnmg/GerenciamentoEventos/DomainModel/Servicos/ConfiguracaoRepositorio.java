/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;

/**
 *
 * @author petronio
 */
public interface ConfiguracaoRepositorio extends Repositorio<Configuracao> {
    public boolean Set(String chave, String valor);
    public boolean Set(Pessoa pessoa, String chave, String valor);
    public Configuracao Abrir(String chave);
    public Configuracao Abrir(Pessoa pessoa, String chave);
}
