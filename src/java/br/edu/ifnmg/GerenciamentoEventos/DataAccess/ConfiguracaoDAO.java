/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Configuracao Abrir(String chave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
