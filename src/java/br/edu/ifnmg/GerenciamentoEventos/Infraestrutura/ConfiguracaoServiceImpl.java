/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ConfiguracaoServiceImpl implements ConfiguracaoService, Serializable {

    @Inject
    AutenticacaoService autenticacao;
    
    @EJB
    ConfiguracaoRepositorio configuracao;
    
    @Override
    public String get(String chave) {
        Configuracao c = configuracao.Abrir(autenticacao.getUsuarioCorrente(), chave);
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
        return configuracao.Set(autenticacao.getUsuarioCorrente(), chave, valor);
    }
    
}
