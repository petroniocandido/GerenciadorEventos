/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.MailService;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class MailServiceImpl implements MailService {

    @EJB
    ConfiguracaoService configuracao;

    @EJB
    LogService log;

    private String servidor;

    private String porta;

    private String autenticacao;

    private String tls;

    private String usuario;

    private String senha;

    private String proxyServidor;

    private String proxyPorta;

    public MailServiceImpl() {
    }

    @PostConstruct
    public void init() {
        this.servidor = configuracao.get("SMTP_SERVIDOR");
        this.porta = configuracao.get("SMTP_PORTA");
        this.usuario = configuracao.get("SMTP_USUARIO");
        this.senha = configuracao.get("SMTP_SENHA");
        this.autenticacao = configuracao.get("SMTP_AUTENTICACAO");
        this.tls = configuracao.get("SMTP_TLS");
        this.proxyServidor = configuracao.get("PROXY_SERVIDOR");
        this.proxyPorta = configuracao.get("PROXY_PORTA");

    }

    @Override
    public boolean enviar(String destinatario, String assunto, String corpo) {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", servidor);
        //properties.put("mail.user", usuario);
        //properties.put("mail.password", senha);
        properties.put("mail.smtp.port", porta);
        properties.put("mail.smtp.auth", autenticacao);
        properties.put("mail.smtp.starttls.enable", tls);

        if (!proxyServidor.isEmpty()) {
            properties.put("http.proxyPort", proxyPorta);
            properties.put("http.proxyHost", proxyServidor);
            properties.setProperty("proxySet", "true");
            properties.setProperty("socksProxyHost", proxyServidor);
            properties.setProperty("socksProxyPort", proxyPorta);
        }

        // Get the default Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usuario, senha);
                    }
                });

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
            log.Append("Falha ao enviar e-mail para: " + destinatario + ". " + mex.getMessage());
            return false;
        }
    }

}
