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

import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Campus;
import br.edu.ifnmg.DomainModel.Perfil;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.Services.AreaConhecimentoRepositorio;
import br.edu.ifnmg.DomainModel.Services.CampusRepositorio;
import br.edu.ifnmg.DomainModel.Services.HashService;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.DomainModel.Services.PerfilRepositorio;
import br.edu.ifnmg.DomainModel.Titulacao;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
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
    PessoaRepositorioLocal dao;
    @EJB
    PerfilRepositorio daoP;
    @EJB
    CampusRepositorio daoC;
    @EJB
    AreaConhecimentoRepositorio daoAC;
    
    @Inject
    HashService hash;
    
    String senha1, senha2;
    
    AreaConhecimento areaConhecimento;

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
            filtro.setPerfil((Perfil)getSessao("pctrl_perfil", daoP));
            filtro.setNome(getSessao("pctrl_nome"));
            filtro.setCpf(getSessao("pctrl_cpf"));
            filtro.setEmail(getSessao("pctrl_email"));
            filtro.setCampus((Campus)getSessao("pctrl_campus",daoC));
            if(getSessao("pctrl_titulacao") != null && !getSessao("pctrl_titulacao").isEmpty())
                filtro.setTitulacaoMaxima(Titulacao.valueOf(getSessao("pctrl_titulacao")));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Pessoa filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("pctrl_perfil", filtro.getPerfil());
            setSessao("pctrl_nome", filtro.getNome());
            setSessao("pctrl_cpf", filtro.getCpf());
            setSessao("pctrl_email", filtro.getEmail());
            setSessao("pctrl_campus", filtro.getCampus());
            if(filtro.getTitulacaoMaxima() != null)
                setSessao("pctrl_titulacao", filtro.getTitulacaoMaxima().name());
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

    

    public AreaConhecimento getAreaConhecimento() {
        if(areaConhecimento == null)
            areaConhecimento = (AreaConhecimento)getSessao("pctrl_ac",daoAC);
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        setSessao("pctrl_ac", areaConhecimento);
        this.areaConhecimento = areaConhecimento;
    }
    
     
    public void addAreaConhecimento() {
        getEntidade().add(areaConhecimento);
        if(dao.Salvar(entidade))
            Mensagem("Salvo com sucesso", "Salvo com sucesso");
        else
            Mensagem("Falha", "Falha ao salvar!");
        areaConhecimento = null; 
    }

    public void removeAreaConhecimento() {
        getEntidade().remove(areaConhecimento);
        dao.Salvar(entidade);
        areaConhecimento = null; 
    }
    
   
    @Override
    public List<Pessoa> getListagem() {
        if(getFiltro().getPerfil() != null)
            return dao.Buscar(getFiltro());
        else
            return dao.BuscarTexto(getFiltro());
    }
    
    public List<Pessoa> getListagemAvaliadores() {
        return dao
                .IgualA("perfil", daoP.Abrir( Long.parseLong( getConfiguracao("PERFILSELECAOAVALIADOR") ) ))
                .Like("nome", getFiltro().getNome())
                .IgualA("campus", getFiltro().getCampus())
                .IgualA("titulacaoMaxima", getFiltro().getTitulacaoMaxima())
                .Join("areasConhecimento", "ac")
                .IgualA("ac.id", areaConhecimento)
                .Buscar();
    }
    
    public GenericDataModel getAvaliadoresDataModel() {
        return new GenericDataModel<>(getListagemAvaliadores(), repositorio);
    }
    
    public void reindexarAreasConhecimento() {
        for (Pessoa s : dao
                .IgualA("perfil", daoP.Abrir( Long.parseLong( getConfiguracao("PERFILSELECAOAVALIADOR") ) ))
                .Buscar()) {
            if (!s.getAreasConhecimento().isEmpty()) {
                List<AreaConhecimento> remover = new ArrayList<>();
                List<AreaConhecimento> adicionar = new ArrayList<>();
                for (AreaConhecimento a : s.getAreasConhecimento()) {
                    if (!a.isArea() && !a.isGrandeArea()) {
                        remover.add(a);
                        AreaConhecimento tmp = daoAC.Abrir(a.getAreaCodigo());
                        adicionar.add(tmp);
                    }
                }
                for (AreaConhecimento c : remover) {
                    s.remove(c);
                }
                for (AreaConhecimento c : adicionar) {
                    s.add(c);
                }
                if (!dao.Salvar(s)) {
                    AppendLog(dao.getErro().getMessage());
                }
            }
        }
    }

}
