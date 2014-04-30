/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class InscricaoDAO 
    extends DAOGenerico<Inscricao> 
    implements InscricaoRepositorio {

    DAOGenerico<InscricaoItem> itemDAO;
    
    public InscricaoDAO(){
        super(Inscricao.class);
        itemDAO = new DAOGenerico<>(InscricaoItem.class);
    }
    
    @PostConstruct
    public void inicializar() {
        itemDAO.setManager(getManager());
    }
    
    @Override
    public List<Inscricao> Buscar(Inscricao filtro) {
        return IgualA("id", filtro.getId())
                .IgualA("pessoa", filtro.getPessoa())
                .IgualA("evento", filtro.getEvento())
                .IgualA("tipo", filtro.getTipo())
                .Buscar();
    }
    
    public List<InscricaoItem> Buscar(InscricaoItem filtro) {
        return itemDAO.IgualA("id", filtro.getId())
                .IgualA("pessoa", filtro.getPessoa())
                .IgualA("evento", filtro.getEvento())
                .IgualA("atividade", filtro.getAtividade())
                .IgualA("tipo", filtro.getTipo())
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
    public boolean Apagar(Inscricao i){
        /*for(InscricaoItem item : i.getItens()){
            i.remove(item);
        }
        for(Arquivo a : i.getArquivos())
            i.remove(a);*/
        return super.Apagar(i);
    }
    
}
