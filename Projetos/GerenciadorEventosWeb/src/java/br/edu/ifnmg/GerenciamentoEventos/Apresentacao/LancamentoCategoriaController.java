/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
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
@Named(value = "lancamentoCategoriaController")
@RequestScoped
public class LancamentoCategoriaController
        extends ControllerBaseEntidade<LancamentoCategoria>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LancamentoCategoriaController() {
    }
    
    @EJB
    LancamentoRepositorio dao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarLancamentoCategoria.xhtml");
        setPaginaListagem("listagemLancamentoCategorias.xtml");
    }

    @Override
    public LancamentoCategoria getFiltro() {
        if (filtro == null) {
            filtro = new LancamentoCategoria();
            filtro.setNome(getSessao("lctctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(LancamentoCategoria filtro) {
        this.filtro = filtro;
        if(filtro != null)
            setSessao("lctctrl_nome", filtro.getNome());

    }
    
    @Override
    public LancamentoCategoria getEntidade() {
        if (entidade == null) {
            Long tmp = getId();
            if(tmp == 0L)
                entidade = new LancamentoCategoria();
            else 
                entidade = dao.AbrirCategoria(tmp);
        }
        return entidade;
    }
   
    @Override
    public void salvar() {
        Rastrear(entidade);

        // salva o objeto no BD
        if (dao.SalvarCategoria(entidade)) {

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
    public String abrir() {
        setEntidade(dao.AbrirCategoria(id));
        return "editarLancamentoCategoria.xhtml";
    }

    @Override
    public void limpar() {
        setEntidade(new LancamentoCategoria());
    }
    
    @Override
    public GenericDataModel getDataModel(){
        LancamentoCategoriaDataModel dm = new LancamentoCategoriaDataModel(dao.BuscarCategorias(filtro),null);
        dm.setLancamentoRepositorio(dao);
        return dm;
    }
    
    @Override
    public List<LancamentoCategoria> getListagemGeral() {
        return dao.listagemCategorias();
    }
    
}
