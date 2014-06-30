/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author petronio
 */
@Named(value = "usuarioController")
@RequestScoped
public class PessoaController
        extends ControllerBaseEntidade<Pessoa>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PessoaController() {
    }

    @EJB
    PessoaRepositorio dao;
    @Inject
    HashService hash;

    String senha1, senha2;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarUsuario.xhtml");
        setPaginaListagem("listagemUsuarios.xtml");
    }

    @Override
    public Pessoa getFiltro() {
        if (filtro == null) {
            filtro = new Pessoa();
            filtro.setNome(getSessao("pctrl_nome"));
            filtro.setCpf(getSessao("pctrl_cpf"));
            filtro.setEmail(getSessao("pctrl_email"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Pessoa filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("pctrl_nome", filtro.getNome());
            setSessao("pctrl_cpf", filtro.getCpf());
            setSessao("pctrl_email", filtro.getEmail());
        }
    }

    @Override
    public void salvar() {

        if (senha1 != null && senha1.length() != 0) {

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
    public void limpar() {
        setEntidade(new Pessoa());
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

    public List<Pessoa> getPessoas() {
        List<Pessoa> tmp = new ArrayList<>();
        tmp.add(entidade);
        return tmp;
    }

}
