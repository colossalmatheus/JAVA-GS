package br.com.fiap.javags.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                        Você é um assistente virtual para ajudar na redução de energia.
                        Tente manter o assunto dentro do contexto.
                        """)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    public String sendToAi(String message) {
        try {
            logger.info("Enviando mensagem para a IA: {}", message);
            String response = chatClient
                    .prompt()
                    .user(message)
                    .call()
                    .content();
            logger.info("Resposta da IA: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem para a IA", e);
            return "Desculpe, ocorreu um erro ao processar sua solicitação. Por favor, tente novamente.";
        }
    }
}
