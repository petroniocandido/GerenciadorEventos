/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author petronio
 */
@Cacheable
@Entity
@Table(name="perfis", indexes = {@Index(columnList = "padrao")})
public class Perfil implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    private String descricao;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Permissao home;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, targetEntity = Permissao.class)
    private List<Permissao> permissoes;
    
    private boolean padrao;

    public Perfil(String nome, String descricao, Permissao home, boolean padrao) {
        this.nome = nome;
        this.descricao = descricao;
        this.home = home;
        this.padrao = padrao;
        permissoes = new LinkedList<>();
    }

    public Perfil() {
        permissoes = new LinkedList<>();
    }
 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Permissao getHome() {
        return home;
    }

    public void setHome(Permissao home) {
        this.home = home;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }
    
    public void add(Permissao p){
        if(permissoes == null){
            permissoes = new LinkedList<>();
        }
        if(!permissoes.contains(p))
            permissoes.add(p);
    }
    
    public void remove(Permissao p){
        if(permissoes.contains(p))
            permissoes.remove(p);
    }

    public boolean isPadrao() {
        return padrao;
    }

    public void setPadrao(boolean padrao) {
        this.padrao = padrao;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil[ id=" + id + " ]";
    }
    
        @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Pessoa criador;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Pessoa ultimoAlterador;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @Version
    private Long versao;
    

    public Pessoa getCriador() {
        return criador;
    }

    public void setCriador(Pessoa criador) {
        this.criador = criador;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Pessoa getUltimoAlterador() {
        return ultimoAlterador;
    }

    public void setUltimoAlterador(Pessoa ultimoAlterador) {
        this.ultimoAlterador = ultimoAlterador;
    }

    public Date getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

}
