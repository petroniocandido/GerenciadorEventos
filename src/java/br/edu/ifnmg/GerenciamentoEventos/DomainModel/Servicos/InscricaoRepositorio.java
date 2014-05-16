/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface InscricaoRepositorio extends Repositorio<Inscricao> {
    public Inscricao Abrir(Evento evt, Pessoa p);
    public List<InscricaoItem> Buscar(InscricaoItem filtro);
    public boolean Salvar(InscricaoItem i);
    public boolean Apagar(InscricaoItem i);
}
