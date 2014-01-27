/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import javax.ejb.Local;

/**
 *
 * @author petronio
 */
@Local
public interface PerfilRepositorio extends Repositorio<Perfil> {
    public Perfil Abrir(String nome);
    public Perfil getPadrao();

    @Override
    public boolean Salvar(Perfil obj);
}
