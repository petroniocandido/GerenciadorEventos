/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Perfil;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface PessoaRepositorioLocal extends PessoaRepositorio {
    public List<Pessoa> Buscar(Evento e);
    public List<Pessoa> Buscar(Atividade a); 
    public List<AreaConhecimento> AvaliadoresPorAreas(Perfil p);
    public List<Pessoa> BuscarAvaliadores(Perfil p, AreaConhecimento a);
}
