/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;


import br.edu.ifnmg.DomainModel.Campus;
import br.edu.ifnmg.DomainModel.Services.CampusRepositorio;
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
@Named(value = "campusController")
@RequestScoped
public class CampusController
    extends ControllerBaseEntidade<Campus> implements Serializable {

    /**
     * Creates a new instance of CampusController
     */
    public CampusController() {        
    }
    
    @EJB
    CampusRepositorio dao;
    
    @Override
    public Campus getFiltro() {
        if (filtro == null) {
            filtro = new Campus();
            filtro.setNome(getSessao("cctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Campus filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("cctrl_nome", filtro.getNome());
        }
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarCampus.xhtml");
        setPaginaListagem("listagemCampus.xhtml");
    }
    
    @Override
    public void limpar() {
        setEntidade(new Campus());
    }
}

