package br.com.fiap.javags.chat;

import br.com.fiap.javags.chat.ChatService;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Route("chat")
@UIScope
@Component
public class ChatView extends VerticalLayout {

    private final ChatService chatService;
    private final MessageList messageList = new MessageList();
    private final MessageInput messageInput = new MessageInput();

    public ChatView(ChatService chatService) {
        this.chatService = chatService;

        // Configurações do layout
        configureComponents();

        // Mensagem inicial
        addMessage("Olá, como posso ajudar?", "Energinho", true);

        // Evento para envio de mensagem
        messageInput.addSubmitListener(event -> send(event.getValue()));

        // Adicionando os componentes à tela
        add(messageList, messageInput);
    }

    private void configureComponents() {
        messageList.setWidthFull();
        messageList.setHeightFull();
        messageInput.setWidthFull();
        setHeightFull();
        setWidthFull();
    }

    private void send(String message) {
        addMessage(message, "Você", false);

        try {
            String response = chatService.sendToAi(message);
            addMessage(response, "Energinho", true);
        } catch (Exception e) {
            addMessage("Desculpe, não consegui processar sua mensagem. Tente novamente mais tarde.", "Energinho", true);
        }
    }

    private void addMessage(String message, String username, boolean isChatAssistant) {
        var messageItem = new MessageListItem(
                message,
                Instant.now(),
                username,
                isChatAssistant
                        ? "https://avatar.iran.liara.run/public/job/operator/male"
                        : "https://avatar.iran.liara.run/public/job/doctor/male"
        );

        var messages = new ArrayList<>(messageList.getItems());
        messages.add(messageItem);
        messageList.setItems(messages);
    }
}
