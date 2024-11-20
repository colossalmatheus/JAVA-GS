package br.com.fiap.javags.mail;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Injeção de dependência via construtor
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String message) {
        try {
            // Criação do email
            var email = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(email, true);

            // Configuração do destinatário, assunto e corpo
            helper.setTo("matheuscolossal1803@gmail.com");
            helper.setSubject("Novo Eletro");
            helper.setText("""
                    <h1>Novo Eletro</h1>
                    <p>%s</p>
                """.formatted(message), true);

            // Envio do email
            mailSender.send(email);

            System.out.println("Email enviado com sucesso!");
        } catch (MessagingException e) {
            System.err.println("Erro ao configurar o email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro ao enviar o email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
