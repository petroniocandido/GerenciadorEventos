/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.MailService;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class MailServiceImpl implements MailService {

    @EJB
    ConfiguracaoRepositorio configuracao;

    private String servidor;

    private String porta;

    private String autenticacao;

    private String tls;

    private String usuario;

    private String senha;

    public MailServiceImpl() {
        this.servidor = configuracao.Abrir("SMTP_SERVIDOR").getValor();
        this.porta = configuracao.Abrir("SMTP_PORTA").getValor();
        this.usuario = configuracao.Abrir("SMTP_USUARIO").getValor();
        this.senha = configuracao.Abrir("SMTP_SENHA").getValor();
        this.autenticacao = configuracao.Abrir("SMTP_AUTENTICACAO").getValor();
        this.tls = configuracao.Abrir("SMTP_TLS").getValor();
    }

    @Override
    public boolean enviar(String destinatario, String assunto, String corpo) {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", servidor);
        properties.setProperty("mail.user", usuario);
        properties.setProperty("mail.password", senha);
        properties.put("mail.smtp.port", senha);
        properties.put("mail.smtp.auth", autenticacao);
        properties.put("mail.smtp.starttls.enable", tls);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(usuario));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(destinatario));

            // Set Subject: header field
            message.setSubject(assunto);

            // Send the actual HTML message, as big as you like
            message.setContent(corpo,
                    "text/html");

            // Send message
            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            return false;
        }
    }

}