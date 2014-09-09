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
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Stateless
public class LogServiceImpl implements LogService {

    @EJB
    private PermissaoRepositorio permissaoDAO;

    @EJB
    private LogRepositorio logDAO;
    
    @Inject
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
