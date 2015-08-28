/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.edu.ifnmg.RetornoPagSeguro.DataAccess;

import br.edu.ifnmg.DomainModel.Configuracao;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoRepositorio;
import java.util.List;

/**
 *
 * @author petronio
 */
public class ConfiguracaoDAO extends DAO<Configuracao> implements ConfiguracaoRepositorio {

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
        Configuracao c = Abrir(chave);
        
        if(c != null){
            c.setValor(valor);
        } else {
            c = new Configuracao(chave, valor);
        }
        
        return Salvar(c);
    }

    @Override
    public boolean Set(Pessoa pessoa, String chave, String valor) {
        Configuracao c = Abrir(pessoa, chave);
        
        if(c != null){
            c.setValor(valor);
        } else {
            c = new Configuracao(chave, valor);
            c.setUsuario(pessoa);
        }
        
        return Salvar(c);
    }
}
