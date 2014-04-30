/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author petronio
 */
@Singleton
public class LancamentoDAO
        extends DAOGenerico<Lancamento>
        implements LancamentoRepositorio {

    DAOGenerico<LancamentoCategoria> daoCategoria;

    public LancamentoDAO() {
        super(Lancamento.class);
        daoCategoria = new DAOGenerico<>(LancamentoCategoria.class);
    }

    @PostConstruct
    public void inicializar() {
        EntityManager tmp = getManager();
        daoCategoria.setManager(tmp);
    }

    @Override
    public List<Lancamento> Buscar(Lancamento filtro) {
        if (filtro != null) {
            IgualA("id", filtro.getId())
                    .Like("descricao", filtro.getDescricao())
                    .IgualA("cliente", filtro.getCliente())
                    .IgualA("usuarioBaixa", filtro.getUsuarioBaixa())
                    .IgualA("baixa", filtro.getBaixa());

        }
        return Buscar();
    }

    @Override
    public LancamentoCategoria AbrirCategoria(Long id) {
        return daoCategoria.Abrir(id);
    }

    @Override
    public boolean SalvarCategoria(LancamentoCategoria obj) {
        return daoCategoria.Salvar(obj);
    }

    @Override
    public boolean ApagarCategoria(LancamentoCategoria obj) {
        return daoCategoria.Apagar(obj);
    }

    @Override
    public List<LancamentoCategoria> BuscarCategorias(LancamentoCategoria filtro) {
        if (filtro != null) {
            daoCategoria
                    .IgualA("id", filtro.getId())
                    .IgualA("nome", filtro.getNome())
                    .IgualA("padrao", filtro.isPadrao());
        }
        return daoCategoria.Ordenar("nome", "ASC").Buscar();
    }

}
