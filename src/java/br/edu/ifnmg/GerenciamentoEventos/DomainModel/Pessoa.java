/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.DomainModel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author petronio
 */
@Cacheable
@Entity
@Table(name = "pessoas", indexes = {
    @Index(columnList = "cpf"),
    @Index(columnList = "email")})
public class Pessoa implements Entidade, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @NotNull
    @Size(min = 3, max = 300)
    @Column(nullable = false, length = 300)
    private String nome;

    @NotNull
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @NotNull
    @Size(min = 5, max = 300)
    @Column(nullable = false, unique = true, length = 300)
    private String email;

    @Column(length = 11)
    private String telefone;

    @NotNull
    @Column(nullable = false)
    private String senha;

    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @ManyToOne(fetch = FetchType.EAGER)
    private Perfil perfil;

    
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

    @Transient
    private String cpfFormatado;

    public String getCpf() {
        if (cpfFormatado == null) {
            if (cpf != null && cpf.length() == 11) {
                cpfFormatado = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
            }
        }
        return cpfFormatado;
    }

    public void setCpf(String cpf) {
        if (cpf != null) {
            this.cpf = cpf.replace(".", "").replace("-", "");
            cpfFormatado = null;
        }
    }

    @Transient
    private String telefoneFormatado;

    public String getTelefone() {
        if (telefoneFormatado == null) {
            if (telefone != null && telefone.length() == 10) {
                telefoneFormatado = "(" + telefone.substring(0, 2) + ") " + telefone.substring(3, 4) + "-" + telefone.substring(6, 4);
            }
        }
        return telefoneFormatado;
    }

    public void setTelefone(String telefone) {
        if (telefone != null) {
            this.telefone = telefone.replace(" ", "").replace("(", "").replace(")", "").replace("-", "");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
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
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa[ id=" + id + " ]";
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
