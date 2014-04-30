/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Stateful
public class LogServiceImpl implements LogService {

    @EJB
    private PermissaoRepositorio permissaoDAO;

    @EJB
    private LogRepositorio logDAO;
    
    @EJB
    AutenticacaoService autentitacao;

    @Override
    public boolean Append(String msg) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ip = httpServletRequest.getRemoteHost();
            String page = FacesContext.getCurrentInstance().getViewRoot().getViewId();

            page = page.substring(1, page.length());

            Permissao p = permissaoDAO.Abrir(page);

            Log log = new Log();
            log.setDescricao(msg);
            log.setUsuario(autentitacao.getUsuarioCorrente());
            log.setPermissao(p);
            log.setMaquina(ip);

            logDAO.Salvar(log);

        } catch (Exception ex) {
            return false;
        } 
        return true;
    }

}
