/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoConfirmacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "credenciamentoController")
@RequestScoped
public class CredenciamentoController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public CredenciamentoController() {
        
    }

    @EJB
    InscricaoRepositorio dao;
    
    @EJB
    InscricaoConfirmacaoService serv;
    
    Long id;
    
    Inscricao inscricao;
    
    public void buscar(){
        inscricao = dao.Abrir(id);
    }
    
    public void credenciar(){
        if(inscricao == null){
            MensagemErro("ERRO", "Inscrição não encontrada!");
            return;
        } 
        
        if(serv.confirmar(inscricao, getUsuarioCorrente())){
            Mensagem("Sucesso!", "Inscrição confirmada com êxiwto!");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }
    
    

}
