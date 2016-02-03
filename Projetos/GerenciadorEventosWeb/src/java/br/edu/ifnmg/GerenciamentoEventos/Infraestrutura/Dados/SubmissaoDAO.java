/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author Isla Guedes
 */
@Singleton
public class SubmissaoDAO
        extends DAO<Submissao>
        implements SubmissaoRepositorio {

    public SubmissaoDAO() {
        super(Submissao.class);
    }

    @Override
    public List<Submissao> Buscar(Submissao filtro) {
        return IgualA("id", filtro.getId())
                .IgualA("inscricao", filtro.getInscricao())
                .Like("titulo", filtro.getTitulo())
                .Like("autor1", filtro.getAutor1())
                .Like("autor2", filtro.getAutor2())
                .Ordenar("titulo", "ASC")
                .Buscar();
    }
    
    @Override
    public List<Submissao> BuscarTexto(String filtro) {
        List<Submissao> list = getManager()
                .createNativeQuery("SELECT * FROM submissoes WHERE MATCH(titulo,resumo,autor1,autor2,autor3,autor4,autor5) AGAINST(? IN BOOLEAN MODE)", Submissao.class)
                .setParameter(1, filtro+"*")
                .getResultList();
        return list;
    }

   
}
