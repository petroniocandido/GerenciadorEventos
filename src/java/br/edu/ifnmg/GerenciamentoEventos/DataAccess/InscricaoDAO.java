/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class InscricaoDAO 
    extends DAOGenerico<Inscricao> 
    implements InscricaoRepositorio {

    public InscricaoDAO(){
        super(Inscricao.class);
    }
    
    @Override
    public List<Inscricao> Buscar(Inscricao filtro) {
        return IgualA("id", filtro.getId())
                .IgualA("pessoa", filtro.getPessoa())
                .IgualA("evento", filtro.getEvento())
                .Buscar();
    }
    
}
