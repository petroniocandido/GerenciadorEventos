/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DomainModel.Campus;
import br.edu.ifnmg.DomainModel.Services.CampusRepositorio;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author Isla Guedes
 */
@Singleton
public class CampusDAO
        extends DAO<Campus>
        implements CampusRepositorio {

    public CampusDAO() {
        super(Campus.class);
    }

    @Override
    public List<Campus> Buscar(Campus filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .Ordenar("nome", "ASC")
                .Buscar();
    }

    @Override
    public Campus Abrir(String sigla) {
        return IgualA("sigla", sigla).Abrir();
    }

}
