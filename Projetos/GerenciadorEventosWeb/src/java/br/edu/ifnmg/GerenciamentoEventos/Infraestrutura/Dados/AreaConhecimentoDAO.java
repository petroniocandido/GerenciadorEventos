/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Services.AreaConhecimentoRepositorio;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author Isla Guedes
 */
@Singleton
public class AreaConhecimentoDAO
        extends DAO<AreaConhecimento>
        implements AreaConhecimentoRepositorio {

    public AreaConhecimentoDAO() {
        super(AreaConhecimento.class);
    }

    @Override
    public List<AreaConhecimento> Buscar(AreaConhecimento filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .Ordenar("numeroCNPQ", "ASC")
                .Buscar();
    }

    @Override
    public List<AreaConhecimento> BuscarTexto(String nome) {
        List<AreaConhecimento> list = getManager()
                .createNativeQuery("SELECT * FROM areasconhecimento WHERE MATCH(nome,numerocnpq) AGAINST(? IN BOOLEAN MODE)", AreaConhecimento.class)
                .setParameter(1, nome+"*")
                .getResultList();
        return list;
    }

    @Override
    public AreaConhecimento Abrir(String numeroCNPQ) {
        return IgualA("numeroCNPQ", numeroCNPQ).Abrir();
    }

}
