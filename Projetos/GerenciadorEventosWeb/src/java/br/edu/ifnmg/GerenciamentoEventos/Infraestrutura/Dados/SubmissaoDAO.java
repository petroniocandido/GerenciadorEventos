/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Campus;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.SubmissaoStatus;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.Query;

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
                .IgualA("status", filtro.getStatus())
                .Like("titulo", filtro.getTitulo())
                .Like("autor1", filtro.getAutor1())
                .Like("autor2", filtro.getAutor2())
                .Ordenar("titulo", "ASC")
                .Buscar();
    }

    @Override
    public List<Submissao> Buscar(Submissao filtro, Evento e, Atividade a) {
        Join("inscricao", "i");
        return IgualA("id", filtro.getId())
                .IgualA("inscricao", filtro.getInscricao())
                .IgualA("status", filtro.getStatus())
                .IgualA("i.evento", e)
                .IgualA("i.atividade", a)
                .Like("titulo", filtro.getTitulo())
                .Like("autor1", filtro.getAutor1())
                .Like("autor2", filtro.getAutor2())
                .Ordenar("titulo", "ASC")
                .Buscar();
    }

    @Override
    public List<AreaConhecimento> AreasPorEvento(Evento e, SubmissaoStatus status) {
        Query query = getManager()
                .createQuery("Select ac from Submissao s "
                        + "join s.inscricao i join s.areasConhecimento ac "
                        + "where i.evento =:evento and s.status =:status "
                        + "order by ac.numeroCNPQ");
        query.setParameter("evento", e).setParameter("status", status);
        
        return query.getResultList();
        
    }
    
    @Override
    public List<Campus> CampusPorEvento(Evento e, SubmissaoStatus s){
         Query query = getManager()
                .createQuery("Select c from Submissao s "
                        + "join s.inscricao i join i.pessoa p join p.campus c "
                        + "where i.evento =:evento and s.status =:status "
                        + "order by c.nome");
        query.setParameter("evento", e).setParameter("status", s);
        
        return query.getResultList();
    }

    @Override
    public List<Submissao> BuscarTexto(String filtro) {
        List<Submissao> list = getManager()
                .createNativeQuery("SELECT * FROM submissoes WHERE MATCH(titulo,resumo,autor1,autor2,autor3,autor4,autor5) AGAINST(? IN BOOLEAN MODE)", Submissao.class)
                .setParameter(1, filtro + "*")
                .getResultList();
        return list;
    }
    
    @Override
    public List<Submissao> Buscar(SubmissaoStatus status, Evento e, Atividade at, AreaConhecimento a){
        Join("inscricao", "i").Join("areasConhecimento", "ac");
        return IgualA("status", status)
                .IgualA("i.evento", e)
                .IgualA("i.atividade", at)
                .IgualA("ac.id", a.getId())
                .Ordenar("titulo", "ASC")
                .Buscar();
    }
    
    @Override
    public List<Submissao> PorAvaliador(SubmissaoStatus status, Evento e, Pessoa p){
        Join("inscricao", "i").Join("avaliadores", "a");
        return IgualA("status", status)
                .IgualA("i.evento", e)
                .IgualA("a.id", p.getId())
                .Ordenar("titulo", "ASC")
                .Buscar();
    }

}
