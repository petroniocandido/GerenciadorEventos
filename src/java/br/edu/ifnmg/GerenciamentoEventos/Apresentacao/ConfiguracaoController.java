/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
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
@Named(value = "configuracaoController")
@RequestScoped
public class ConfiguracaoController
        extends ControllerBaseEntidade<Configuracao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ConfiguracaoController() {
    }

    @EJB
    ConfiguracaoRepositorio dao;

    @EJB
    PessoaRepositorio pessoaDAO;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarConfiguracao.xhtml");
        setPaginaListagem("listagemConfiguracoes.xhtml");
    }

    public List<Configuracao> autoCompleteConfiguracao(String query) {
        Configuracao i = new Configuracao();
        i.setChave(query);
        return dao.Buscar(i);
    }

    @Override
    public Configuracao getFiltro() {
        if (filtro == null) {
            filtro = new Configuracao();
            filtro.setChave(getSessao("cnfctrl_chave"));
            filtro.setUsuario((Pessoa) getSessao("cnfctrl_usuario", pessoaDAO));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Configuracao filtro) {
        this.filtro = filtro;
        setSessao("cnfctrl_chave", filtro.getChave());
        setSessao("cnfctrl_usuario", filtro.getUsuario());
    }

    @Override
    public void salvar() {

        Rastrear(getEntidade());

        // salva o objeto no BD
        if (dao.Set(getEntidade().getUsuario(), getEntidade().getChave(), getEntidade().getValor())) {

            setId(getEntidade().getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + getEntidade().getClass().getSimpleName() + " " + getEntidade().getId() + "(" + getEntidade().toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + getEntidade().getClass().getSimpleName() + " " + getEntidade().getId() + "(" + getEntidade().toString() + ")" + ": " + repositorio.getErro().getMessage());
        }

        // atualiza a listagem
        filtrar();
    }

    @Override
    public void limpar() {

        setEntidade(new Configuracao());
    }

}
