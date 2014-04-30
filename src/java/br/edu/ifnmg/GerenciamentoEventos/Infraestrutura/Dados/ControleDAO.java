/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ControleRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class ControleDAO 
    extends DAOGenerico<Controle> 
    implements ControleRepositorio {
    
    
    public ControleDAO(){
        super(Controle.class);
    }
    

    @Override
    public List<Controle> Buscar(Controle filtro) {
        IgualA("id", filtro.getId());
                
        return   Buscar();
    }

    @Override
    public Controle Abrir(Evento e) {
        return IgualA("evento", e).Abrir();
    }

    @Override
    public Controle Abrir(Atividade a) {
        return IgualA("atividade", a).Abrir();
    }

}
