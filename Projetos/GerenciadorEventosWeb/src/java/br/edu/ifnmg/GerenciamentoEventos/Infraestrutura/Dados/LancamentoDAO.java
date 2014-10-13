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
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;

/**
 *
 * @author petronio
 */
@Singleton
public class LancamentoDAO
        extends DAO<Lancamento>
        implements LancamentoRepositorio {

    DAO<LancamentoCategoria> daoCategoria;

    public LancamentoDAO() {
        super(Lancamento.class);
        daoCategoria = new DAO<>(LancamentoCategoria.class);
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
    public LancamentoCategoria CategoriaPadrao() {
        return daoCategoria.IgualA("padrao", true).Abrir();
    }

    @Override
    public LancamentoCategoria AbrirCategoria(Long id) {
        return daoCategoria.Abrir(id);
    }

    @Override
    public boolean SalvarCategoria(LancamentoCategoria obj) {
        if(obj.isPadrao()){
            daoCategoria.Setar("padrao", false).DiferenteDe("id", obj.getId()).Atualiza();
        }
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
