/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.util.Date;

/**
 *
 * @author petronio
 */

public interface Entidade  {

    public void setId(Long id);
    public Long getId();
    public Pessoa getCriador();
    public void setCriador(Pessoa obj);
    public Date getDataCriacao();
    public void setDataCriacao(Date obj);
    public Pessoa getUltimoAlterador();
    public void setUltimoAlterador(Pessoa obj);
    public Date getDataUltimaAlteracao();
    public void setDataUltimaAlteracao(Date obj);
    public Long getVersao();
    //public boolean isAtivo();
    //public void setAtivo(boolean value);
}
