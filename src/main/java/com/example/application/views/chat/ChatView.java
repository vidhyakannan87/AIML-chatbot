package com.example.application.views.chat;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.springframework.context.ApplicationContext;
import org.vaadin.artur.Avataaar;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@PageTitle("Chat")
@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@CssImport("styles/views/chat/chat-view.css")
public class ChatView extends VerticalLayout {

    private final UI ui;
    private final MessageList messageList = new MessageList();
    private final TextField message = new TextField();
    private Chat chatSession;
    private final ScheduledExecutorService executorService;
    private final ApplicationContext applicationContext;
    private Bot bot;

    public ChatView(Bot alice, ScheduledExecutorService executorService, ApplicationContext applicationContext) {
        this.executorService = executorService;
        this.applicationContext = applicationContext;
        ui = UI.getCurrent();
        chatSession = new Chat(alice);
        sendOpeningMessage();
        message.setPlaceholder("Enter a message...");
        message.setSizeFull();
        Button send = new Button(VaadinIcon.ENTER.create(), event -> sendMessage());
        send.addClickShortcut(Key.ENTER);
        HorizontalLayout inputLayout = new HorizontalLayout(message, send);
        inputLayout.addClassName("inputLayout");
        add(messageList, inputLayout);
        expand(messageList);
        setSizeFull();
    }

    private void sendMessage() {
        String text = message.getValue();
        messageList.addMessage("You", new Avataaar("Name"), text, true);
        message.clear();
        String answer = chatSession.multisentenceRespond(text);
        ui.access(() -> messageList.addMessage(
                "Alice", new Avataaar("Alice2"), answer, false));
//        executorService.schedule(() -> {
//                    String answer = chatSession.multisentenceRespond(text);
//                    ui.access(() -> messageList.addMessage(
//                            "Alice", new Avataaar("Alice2"), answer, false));
//                },new Random().ints(100, 300).findFirst().getAsInt(),
//              TimeUnit.MILLISECONDS);
    }

    private void sendOpeningMessage() {
        String openingMessage = "Welcome to another beautiful day,How are you feeling?";
        ui.access(() -> messageList.addMessage(
                "Alice", new Avataaar("Alice2"),openingMessage, false));
    }


}
