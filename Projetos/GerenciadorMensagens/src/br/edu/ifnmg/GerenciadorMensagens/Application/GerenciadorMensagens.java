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
package br.edu.ifnmg.GerenciadorMensagens.Application;

import br.edu.ifnmg.DomainModel.Services.MailService;
import br.edu.ifnmg.GerenciadorMensagens.DataAccess.MensagemDAO;
import br.edu.ifnmg.DomainModel.Mensagem;
import br.edu.ifnmg.DomainModel.Services.ConfiguracaoService;
import br.edu.ifnmg.DomainModel.Services.MensagemRepositorio;
import java.util.List;

/**
 *
 * @author petronio
 */
public class GerenciadorMensagens {

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            ConfiguracaoService configuracao = new ConfiguracaoServiceImpl();
            String conf_minutos;
            conf_minutos = configuracao.get("GERENCIADORMENSAGENS_INTERVALO");
            int minutos = 3;
            try {
                minutos = Integer.parseInt(conf_minutos);
            } catch (Exception ex) {

            }
            int total = 0, totalEnviado = 0, totalNaoEnviado = 0, totalErro = 0;
            br.edu.ifnmg.DomainModel.Services.LogService log = new LogServiceImpl();
            MailService mail = new MailServiceImpl();
            try {
                MensagemRepositorio mDAO = new MensagemDAO();
                List<Mensagem> mensagens = mDAO.Buscar();
                total = mensagens.size();
                for (Mensagem m : mensagens) {
                    try {
                        if (mail.enviar(m)) {
                            totalEnviado++;
                            mDAO.Apagar(m);
                        } else {
                            totalNaoEnviado++;
                            if (m.getNumeroTentativas() <= 5) {
                                m.setNumeroTentativas(m.getNumeroTentativas() + 1);
                            } else {
                                mDAO.Apagar(m);
                                log.Append("A mensagem \'" + m.getAssunto() + "\" para o destinatário \"" + m.getDestinatario()
                                        + " está sendo removida após 5 tentativas de envio sem êxito!");
                            }
                        }
                    } catch (Exception ex) {
                        totalErro++;
                        log.Append("Erro ao processar a mensagem : " + ex.getMessage());
                        System.out.println("Erro ao processar a mensagem : " + ex.getMessage());
                    }

                }

                log.Append("Término da execução do Gerenciador de Mensagens: " + total + " mensagens, "
                        + totalEnviado + " enviadas, " + totalNaoEnviado + " não enviadas, "
                        + totalErro + " erros.");
                System.out.println("Término da execução do Gerenciador de Mensagens: " + total + " mensagens, "
                        + totalEnviado + " enviadas, " + totalNaoEnviado + " não enviadas, "
                        + totalErro + " erros.");
            } catch (Exception ex) {
                System.out.println("Erro no Gerenciador de Mensagens: " + ex.getMessage());
            }
            Thread.sleep(1000 * 60 * minutos);
        }
    }

}
