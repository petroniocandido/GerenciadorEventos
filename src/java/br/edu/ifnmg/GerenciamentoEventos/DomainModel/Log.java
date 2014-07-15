/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author petronio
 */
@Entity
@Cacheable(false)
public class Log implements Entidade, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Pessoa usuario;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEvento;
        
    @ManyToOne
    private Permissao permissao;
    
    @Lob
    private String descricao;
    
    private String maquina;

    public Log() {
        this.dataEvento = new Date();
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getUsuario() {
        return usuario;
    }

    public void setUsuario(Pessoa usuario) {
        this.usuario = usuario;
    }

    public Date getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Date data) {
        this.dataEvento = data;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
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
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log[ id=" + id + " ]";
    }

    @Override
    public Pessoa getCriador() {
       return null;
    }

    @Override
    public void setCriador(Pessoa obj) {
        
    }

    @Override
    public Date getDataCriacao() {
        return dataEvento;
    }

    @Override
    public void setDataCriacao(Date obj) {
        this.dataEvento = obj;
    }

    @Override
    public Pessoa getUltimoAlterador() {
        return null;
    }

    @Override
    public void setUltimoAlterador(Pessoa obj) {
        
    }

    @Override
    public Date getDataUltimaAlteracao() {
        return dataEvento;
    }

    @Override
    public void setDataUltimaAlteracao(Date obj) {
        
    }

    @Override
    public Long getVersao() {
        return 1L;
    }
    
}
