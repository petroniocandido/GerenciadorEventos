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

import br.edu.ifnmg.DomainModel.Services.MailService;
import br.edu.ifnmg.GerenciadorMensagens.DomainModel.Mensagem;
import br.edu.ifnmg.GerenciadorMensagens.DomainModel.MensagemRepositorio;
import javax.ejb.EJB;
import javax.ejb.Stateless;


@Stateless
public class MailServiceImpl implements MailService {

    @EJB
    MensagemRepositorio dao;
    
    @Override
    public boolean enviar(String destinatario, String assunto, String corpo) {
        return dao.Salvar(new Mensagem(destinatario, assunto, corpo));
    }

}
