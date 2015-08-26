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

import br.edu.ifnmg.DomainModel.Permissao;
import br.edu.ifnmg.DomainModel.Services.PermissaoRepositorio;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class PermissaoDAO
        extends DAO<Permissao>
        implements PermissaoRepositorio {

    public PermissaoDAO() {
        super(Permissao.class);
    }

    @Override
    public List<Permissao> Buscar(Permissao filtro) {
        return Like("nome", filtro.getNome())
                .Like("uri", filtro.getUri())
                .Buscar();
    }

    @Override
    public Permissao Abrir(String uri) {
        try {
            Query q = getManager()
                    .createNamedQuery("permissoes.url")
                    .setParameter("url", uri)
                    .setHint("eclipselink.QUERY_RESULTS_CACHE", "TRUE");
            return (Permissao) q.getSingleResult();
        } catch (Exception ex) {
            setErro(ex);
            return null;
        }
    }

}
