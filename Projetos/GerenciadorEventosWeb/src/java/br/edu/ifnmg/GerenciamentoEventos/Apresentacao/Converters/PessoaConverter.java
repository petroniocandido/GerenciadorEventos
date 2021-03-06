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
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.DomainModel.Atuacao;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.DomainModel.PessoaTipo;
import br.edu.ifnmg.DomainModel.PronomeTratamento;
import br.edu.ifnmg.DomainModel.Services.PessoaRepositorio;
import br.edu.ifnmg.DomainModel.Sexo;
import br.edu.ifnmg.DomainModel.Titulacao;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericConverter;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorioLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author petronio
 */
@Named(value = "usuarioConverter")
@Singleton
public class PessoaConverter
        extends GenericConverter<Pessoa, PessoaRepositorio>
        implements Serializable {

    @EJB
    PessoaRepositorioLocal dao;
    
    PessoaTipo[] tipos;
    
    Atuacao[] atuacoes;
    
    PronomeTratamento[] tratamentos;
    
    Sexo[] sexos;
    
    Titulacao[] titulacoes;
    
    public PessoaTipo[] getTipos() {
        if(tipos == null)
            tipos = PessoaTipo.values();
        return tipos;
    }

    public PronomeTratamento[] getTratamentos() {
        if(tratamentos == null)
            tratamentos = PronomeTratamento.values();
        return tratamentos;
    }

    public Sexo[] getSexos() {
        if(sexos == null)
            sexos = Sexo.values();
        return sexos;
    }

    public Titulacao[] getTitulacoes() {
        if(titulacoes == null)
            titulacoes = Titulacao.values();
        return titulacoes;
    }
    
    public Atuacao[] getAtuacoes() {
        if(atuacoes == null)
            atuacoes = Atuacao.values();
        return atuacoes;
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
    public List<Pessoa> autoCompletePessoa(String query) {
        return dao.BuscarTexto(query);
    }
}
