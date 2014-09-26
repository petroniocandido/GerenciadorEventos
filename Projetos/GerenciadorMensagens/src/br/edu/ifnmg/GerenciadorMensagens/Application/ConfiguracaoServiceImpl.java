/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciadorMensagens.Application;

import br.edu.ifnmg.DomainModel.Configuracao;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoRepositorio;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoService;
import br.edu.ifnmg.GerenciadorMensagens.DataAccess.ConfiguracaoDAO;

/**
 *
 * @author petronio
 */
public class ConfiguracaoServiceImpl implements ConfiguracaoService {

    ConfiguracaoRepositorio configuracao = new ConfiguracaoDAO();
    
    @Override
    public String get(String chave) {
        Configuracao c = configuracao.Abrir(chave);
        if (c == null) {
            c = configuracao.Abrir(chave);
        }
        if (c != null) {
            return c.getValor();
        } else {
            return null;
        }
    }

    @Override
    public boolean set(String chave, String valor) {
        return configuracao.Set(chave, valor);
    }

    @Override
    public boolean setLocal(String chave, String valor) {
        return configuracao.Set(chave, valor);
    }
    
}
