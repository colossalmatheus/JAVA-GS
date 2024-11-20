package br.com.fiap.javags.mail;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "email-queue")
    public void sendEmail(String message) throws MessagingException {
        emailService.sendEmail(message);
    }

}