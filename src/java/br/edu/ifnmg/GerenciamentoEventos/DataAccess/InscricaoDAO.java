/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
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

    @Override
    public Inscricao Abrir(Evento evt, Pessoa p) {
        return IgualA("evento", evt)
                .IgualA("pessoa", p)
                .Abrir();
    }
    
}
