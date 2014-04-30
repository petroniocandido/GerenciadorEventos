/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.HashService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
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
@Named(value = "configuracaoController")
@SessionScoped
public class ConfiguracaoController
        extends ControllerBaseEntidade<Configuracao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ConfiguracaoController() {
        id = 0L;
        setEntidade(new Configuracao());
        setFiltro(new Configuracao());
    }

    @EJB
    ConfiguracaoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Configuracao> autoCompleteConfiguracao(String query) {
        Configuracao i = new Configuracao();
        i.setChave(query);
        return dao.Buscar(i);
    }

    @Override
    public void filtrar() {
        listagem = dao.Buscar(filtro);
    }

    @Override
    public void salvar() {

        Rastrear(entidade);

        // salva o objeto no BD
        if (dao.Set(entidade.getUsuario(), entidade.getChave(), entidade.getValor())) {

            setId(entidade.getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }

        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        ApagarEntidade();
        filtrar();
        return "listarConfiguracoes.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarConfiguracao.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemConfiguracoes.xhtml";
    }

    @Override
    public void limpar() {

        setEntidade(new Configuracao());
    }

    @Override
    public String novo() {
        limpar();
        return "editarConfiguracao.xhtml";
    }

    @Override
    public List<Configuracao> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Configuracao> listagem) {
        this.listagem = listagem;
    }    
}
