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

import br.edu.ifnmg.DataAccess.DAOGenerico;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import br.edu.ifnmg.DomainModel.Services.Repositorio;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class InscricaoDAO 
    extends DAO<Inscricao> 
    implements InscricaoRepositorio {

    DAO<InscricaoItem> itemDAO;
    
    public InscricaoDAO(){
        super(Inscricao.class);
        itemDAO = new DAO<>(InscricaoItem.class);
    }
    
    @PostConstruct
    public void inicializar() {
        itemDAO.setManager(getManager());
    }
    
    @Override
    public List<Inscricao> Buscar(Evento evt, String participante){
        return Join("pessoa", "p")
                .IgualA("tipo", InscricaoTipo.Inscricao)
                .IgualA("evento", evt)
                .Like("p.nome", participante)
                .Ordenar("p.nome", "ASC")
                .Buscar();
    }
    
    @Override
    public List<Inscricao> Buscar(Inscricao filtro) {
        return IgualA("id", filtro.getId())
                .IgualA("pessoa", filtro.getPessoa())
                .IgualA("evento", filtro.getEvento())
                .IgualA("tipo", filtro.getTipo())
                .IgualA("categoria", filtro.getCategoria())
                .Ordenar("categoria", "ASC")
                .Ordenar("ordem", "ASC")
                .Buscar();
    }
    
    @Override
    public List<InscricaoItem> Buscar(InscricaoItem filtro) {
        return itemDAO.IgualA("id", filtro.getId())
                .IgualA("pessoa", filtro.getPessoa())
                .IgualA("evento", filtro.getEvento())
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("tipo", filtro.getTipo())
                .IgualA("categoria", filtro.getCategoria())
                .Ordenar("categoria", "ASC")
                .Ordenar("ordem", "ASC")
                .Buscar();
    }

    @Override
    public Inscricao Abrir(Evento evt, Pessoa p) {
        return IgualA("evento", evt)
                .IgualA("pessoa", p)
                .IgualA("tipo", InscricaoTipo.Inscricao)
                .Abrir();
    }
    
    @Override
    public InscricaoItem Abrir(Inscricao i, Atividade a) {
        return itemDAO.IgualA("inscricao", i)
                .IgualA("atividade", a)
                .IgualA("tipo", InscricaoTipo.InscricaoItem)
                .Abrir();
    }
    
    @Override
    public boolean Apagar(Inscricao i){
        /*for(InscricaoItem item : i.getItens()){
            i.remove(item);
        }
        for(Arquivo a : i.getArquivos())
            i.remove(a);*/
        return super.Apagar(i);
    }
    
    @Override
    public boolean Salvar(InscricaoItem i){
/*        Inscricao tmp = i.getInscricao();
        tmp.add(i);
        return Salvar(tmp);
 */
        return itemDAO.Salvar(i);
    }
    
    @Override
    public boolean Apagar(InscricaoItem i){
        Inscricao tmp = i.getInscricao();
        tmp.remove(i);
        return Salvar(tmp);
    }

    @Override
    public Repositorio<InscricaoItem> getRepositorioItem() {
        return itemDAO;
    }

    @Override
    public InscricaoItem AbrirItem(Long id) {
        return itemDAO.Abrir(id);
    }
    
    @Override
    public Long QuantidadeInscricoes(Inscricao i, Atividade a) {
        Query query = itemDAO.getManager().createQuery("select count(i) from InscricaoItem i where i.inscricao =:inscricao "
                + "and i.atividade =:atividade and i.categoria =:normal");
        query.setParameter("inscricao", i).setParameter("atividade", a).setParameter("normal", InscricaoCategoria.Normal);
        return (Long)query.getSingleResult();
    }
    
}
