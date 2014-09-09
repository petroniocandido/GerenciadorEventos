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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class AtividadeDAO
        extends DAOGenerico<Atividade>
        implements AtividadeRepositorio {

    DAOGenerico<AtividadeTipo> daoTipo;

    public AtividadeDAO() {
        super(Atividade.class);
        daoTipo = new DAOGenerico<>(AtividadeTipo.class);

    }

    @PostConstruct
    public void inicializar() {
        EntityManager tmp = getManager();
        daoTipo.setManager(tmp);
    }

    @Override
    public List<Atividade> Buscar(Atividade filtro) {
        if (filtro != null) {
            IgualA("id", filtro.getId())
                    .Like("nome", filtro.getNome())
                    .IgualA("evento", filtro.getEvento())
                    .IgualA("inicio", filtro.getInicio())
                    .IgualA("local", filtro.getLocal())
                    .Like("descricao", filtro.getDescricao())
                    .IgualA("status", filtro.getStatus())
                    .IgualA("termino", filtro.getTermino());
            if (filtro.getTipo() != null) {
                if (filtro.getTipo().getId() != null && filtro.getTipo().getId() > 0) {
                    IgualA("tipo", filtro.getTipo());
                } else {
                    Join("tipo", "t").IgualA("t.publico", filtro.getTipo().getPublico());
                }
            }
        }
        return Buscar();
    }

    @Override
    public boolean Salvar(Atividade obj) {
        if (obj.getControle() == null) {
            obj.setControle(new Controle(obj, 0, 0));
        }
        return super.Salvar(obj);
    }

    @Override
    public boolean SalvarTipo(AtividadeTipo obj) {
        return daoTipo.Salvar(obj);
    }

    @Override
    public boolean ApagarTipo(AtividadeTipo obj) {
        return daoTipo.Apagar(obj);
    }

    @Override
    public AtividadeTipo AbrirTipo(Long id) {
        return daoTipo.Abrir(id);
    }

    @Override
    public List<AtividadeTipo> BuscarTipo(AtividadeTipo obj) {
        
        if(obj != null) {
            daoTipo
                .Like("nome", obj.getNome())
                .IgualA("publico", obj.getPublico());
        }
        return daoTipo.Ordenar("nome", "ASC").Buscar();
    }

    @Override
    public List<Atividade> BuscarAtividadesDoUsuario(Pessoa obj) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        /*
         return MaiorOuIgualA("termino", cal.getTime())
         .DiferenteDe("status", Status.Cancelado)
         .DiferenteDe("status", Status.Concluido)
         .Join("responsaveis", "r").IgualA("r.id", obj.getId())
         .Buscar();
         */
        Query q = getManager()
                .createNamedQuery("atividades.ativasUsuario")
                .setParameter("idUsuario", obj.getId())
                .setParameter("termino", cal.getTime())
                .setParameter("cancelado", Status.Cancelado)
                .setParameter("concluido", Status.Concluido)
                .setHint("eclipselink.QUERY_RESULTS_CACHE", "TRUE");
        return q.getResultList();
    }

    @Override
    public List<Atividade> BuscarAtividadesPorEventoETipo(Evento e, AtividadeTipo t) {
        Query q = getManager()
                .createNamedQuery("atividades.porTipoEvento")
                .setParameter("evento", e)
                .setParameter("tipo", t)
                .setParameter("pendente", Status.Pendente)
                .setParameter("emexecucao", Status.EmExecucao)
                .setHint("eclipselink.QUERY_RESULTS_CACHE", "TRUE");
        return q.getResultList();
    }

    @Override
    public List<AtividadeTipo> BuscarAtividadesTiposPorEvento(Evento e) {
        Query q = daoTipo.getManager()
                .createNamedQuery("atividadestipos.publicasPorEvento")
                .setParameter("evento", e)
                .setParameter("pendente", Status.Pendente)
                .setParameter("emexecucao", Status.EmExecucao)
                .setHint("eclipselink.QUERY_RESULTS_CACHE", "TRUE");
        return q.getResultList();
    }
}
