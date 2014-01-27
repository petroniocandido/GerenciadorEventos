/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "usuarioController")
@SessionScoped
public class PessoaController
        extends ControllerBaseEntidade<Pessoa>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PessoaController() {
        id = 0L;
        setEntidade(new Pessoa());
        setFiltro(new Pessoa());
    }

    @EJB
    PessoaRepositorio dao;
    @EJB
    HashService hash;

    String senha1, senha2;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Pessoa> autoCompletePessoa(String query) {
        Pessoa i = new Pessoa();
        i.setNome(query);
        return dao.Buscar(i);
    }

    @Override
    public void filtrar() {
        listagem = dao.Buscar(filtro);
    }

    @Override
    public void salvar() {

        if (senha1.length() != 0) {

            if (senha1.equals(senha2)) {
                entidade.setSenha(hash.getMD5(senha1));
            } else {
                Mensagem("Erro", "As senhas não conferem!");
                return;
            }
        }

        SalvarEntidade();

        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        ApagarEntidade();
        filtrar();
        return "listarUsuarios.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarUsuario.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemUsuarios.xhtml";
    }

    @Override
    public void limpar() {

        setEntidade(new Pessoa());
    }

    @Override
    public String novo() {
        limpar();
        return "editarUsuario.xhtml";
    }

    @Override
    public List<Pessoa> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Pessoa> listagem) {
        this.listagem = listagem;
    }

    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

    public String getSenha2() {
        return senha2;
    }

    public void setSenha2(String senha2) {
        this.senha2 = senha2;
    }
    
    
}
