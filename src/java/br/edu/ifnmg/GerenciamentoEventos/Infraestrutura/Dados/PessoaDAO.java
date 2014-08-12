/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class PessoaDAO
        extends DAOGenerico<Pessoa>
        implements PessoaRepositorio {

    public PessoaDAO() {
        super(Pessoa.class);
    }

    @Override
    public List<Pessoa> Buscar(Pessoa filtro) {
        if (filtro != null) {
            IgualA("id", filtro.getId())
                    .Like("nome", filtro.getNome())
                    .Like("cpf", filtro.getCpf())
                    .Like("email", filtro.getEmail());
        }
        Ordenar("nome", "ASC");
        return Buscar();
    }
    
    @Override
    public List<Pessoa> Buscar(Evento e){
        Join("inscricoes", "i").IgualA("i.evento", e).Ordenar("nome", "ASC");
        return Buscar();
    }
    
    @Override
    public List<Pessoa> Buscar(Atividade a){
        Query q = getManager().createQuery("select o from Pessoa o join o.inscricoes i "
                + " join i.itens it where it.atividade = :a"
                + " order by o.nome ");
        q.setParameter("a", a);
        //Join("inscricoes", "i1").Join("i.itens", "i2").IgualA("i2.atividade", a).Ordenar("nome", "ASC");
        return q.getResultList();
    }

    @Override
    public Pessoa Abrir(String login) {
        return IgualA("email", login).Abrir();
    }

    @Override
    public Pessoa AbrirPorCPF(String cpf) {
        return IgualA("cpf", cpf).Abrir();
    }

}
