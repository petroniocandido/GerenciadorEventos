/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.EventoInscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoInscricaoCategoriaRepositorio;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author Isla Guedes
 */
@Singleton
public class EventoInscricaoCategoriaDAO
        extends DAO<EventoInscricaoCategoria>
        implements EventoInscricaoCategoriaRepositorio {

    public EventoInscricaoCategoriaDAO() {
        super(EventoInscricaoCategoria.class);
    }

    @Override
    public List<EventoInscricaoCategoria> Buscar(EventoInscricaoCategoria filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .IgualA("evento", filtro.getEvento())
                .Ordenar("nome", "ASC")
                .Buscar();
    }

   

}
