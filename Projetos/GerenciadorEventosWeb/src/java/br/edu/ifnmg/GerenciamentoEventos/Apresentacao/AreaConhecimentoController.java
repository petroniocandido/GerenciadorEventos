/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Services.AreaConhecimentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Isla Guedes
 */
@Named(value = "areaConhecimentoController")
@RequestScoped
public class AreaConhecimentoController
    extends ControllerBaseEntidade<AreaConhecimento> implements Serializable {

    /**
     * Creates a new instance of AreaConhecimentoController
     */
    public AreaConhecimentoController() {
    }
    
    @EJB
    AreaConhecimentoRepositorio dao;
    
    @Override
    public AreaConhecimento getFiltro() {
        if (filtro == null) {
            filtro = new AreaConhecimento();
            filtro.setNome(getSessao("acctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(AreaConhecimento filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("acctrl_nome", filtro.getNome());
        }
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarAreaConhecimento.xhtml");
        setPaginaListagem("listagemAreasConhecimento.xhtml");
    }
    
    @Override
    public void limpar() {
        setEntidade(new AreaConhecimento());
    }
}

