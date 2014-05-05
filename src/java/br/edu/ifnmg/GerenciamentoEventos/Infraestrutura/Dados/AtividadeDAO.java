/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author petronio
 */
@Singleton
public class AtividadeDAO 
    extends DAOGenerico<Atividade> 
    implements AtividadeRepositorio {
    
    DAOGenerico<AtividadeTipo> daoTipo;
    
    public AtividadeDAO(){
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
        IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .IgualA("evento", filtro.getEvento())
                .IgualA("inicio", filtro.getInicio())
                .IgualA("local", filtro.getLocal())
                .Like("descricao", filtro.getDescricao())
                .IgualA("status", filtro.getStatus())
                .IgualA("termino", filtro.getTermino());
        if(filtro.getTipo() != null){
            if(filtro.getTipo().getId() != null && filtro.getTipo().getId() > 0) IgualA("tipo", filtro.getTipo());
            else Join("tipo", "t").IgualA("t.publico", filtro.getTipo().getPublico());
        }
                
        return   Buscar();
    }

    @Override
    public boolean Salvar(Atividade obj) {
        if(obj.getControle()== null){
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
        return daoTipo
                .Like("nome", obj.getNome())
                .IgualA("publico", obj.getPublico())
                .Buscar();
    }

    @Override
    public List<Atividade> BuscarAtividadesDoUsuario(Pessoa obj) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        return MaiorOuIgualA("termino", cal.getTime())
                .DiferenteDe("status", Status.Cancelado)
                .DiferenteDe("status", Status.Concluido)
                .Join("responsaveis", "r").IgualA("r.id", obj.getId())
                .Buscar();
    }
}
