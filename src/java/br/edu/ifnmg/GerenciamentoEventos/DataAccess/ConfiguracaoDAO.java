/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class ConfiguracaoDAO extends DAOGenerico<Configuracao> implements ConfiguracaoRepositorio {

    public ConfiguracaoDAO() {
        super(Configuracao.class);
    }

    @Override
    public List<Configuracao> Buscar(Configuracao filtro) {
        return IgualA("usuario", filtro.getUsuario())
                .IgualA("chave", filtro.getChave())
                .Buscar();
    }

    @Override
    public Configuracao Abrir(String chave) {
        return IgualA("chave", chave)
                .ENulo("usuario")
                .Abrir();
    }
    
    @Override
    public Configuracao Abrir(Pessoa pessoa, String chave) {
        return IgualA("usuario", pessoa)
                .IgualA("chave", chave)
                .Abrir();
    }

    @Override
    public boolean Set(String chave, String valor) {
        return IgualA("chave", chave)
                .ENulo("usuario")
                .Setar("valor", valor)
                .Atualiza();
    }

    @Override
    public boolean Set(Pessoa pessoa, String chave, String valor) {
        return IgualA("chave", chave)
                .IgualA("usuario", pessoa)
                .Setar("valor", valor)
                .Atualiza();
    }
}
