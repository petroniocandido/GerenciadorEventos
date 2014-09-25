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

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.DomainModel.Configuracao;
import br.edu.ifnmg.DomainModel.Services.AutenticacaoService;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoRepositorio;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoService;
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
