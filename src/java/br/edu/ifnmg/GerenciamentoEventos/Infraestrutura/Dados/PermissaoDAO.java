/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class PermissaoDAO
        extends DAOGenerico<Permissao>
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
