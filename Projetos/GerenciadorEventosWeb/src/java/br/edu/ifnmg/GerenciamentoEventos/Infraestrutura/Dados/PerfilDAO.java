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
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DomainModel.Perfil;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class PerfilDAO
        extends DAOGenerico<Perfil>
        implements PerfilRepositorio {

    public PerfilDAO() {
        super(Perfil.class);
    }

    @Override
    public Perfil Abrir(String nome) {
        return IgualA("nome", nome).Abrir();
    }

    @Override
    public boolean Salvar(Perfil obj) {

        if (super.Salvar(obj)) {
            if (obj.isPadrao()) {
                return DiferenteDe("id",obj.getId()).Setar("padrao", false).Atualiza();
            }
            return true;
        }
        else return false;
    }

    @Override
    public Perfil getPadrao() {
        return IgualA("padrao", true).Abrir();

    }

    @Override
    public List<Perfil> Buscar(Perfil filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .Like("descricao", filtro.getDescricao())
                .IgualA("padrao", filtro.isPadrao())
                .Buscar();

    }

}
