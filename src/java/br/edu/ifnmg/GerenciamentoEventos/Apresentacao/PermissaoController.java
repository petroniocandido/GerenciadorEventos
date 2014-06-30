/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "permissaoController")
@RequestScoped
public class PermissaoController
        extends ControllerBaseEntidade<Permissao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PermissaoController() {
    }

    @EJB
    PermissaoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaListagem("listagemPermissoes.xtml");
        setPaginaEdicao("editarPermissao.xhtml");
    }

    @Override
    public Permissao getFiltro() {
        if (filtro == null) {
            filtro = new Permissao();
            filtro.setUri(getSessao("filtro_uri"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Permissao filtro) {
        this.filtro = filtro;
        setSessao("filtro_uri", filtro.getUri());
    }

    @Override
    public void limpar() {
        setEntidade(new Permissao());
    }

    @Override
    public List<Permissao> getListagemGeral() {
        return dao.Buscar(new Permissao());
    }

}
