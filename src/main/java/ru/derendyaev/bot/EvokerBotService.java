package ru.derendyaev.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class EvokerBotService extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);

        String userName = null;
        Long userId = null;
        String textToSend = null;

        userId = update.getMessage().getChatId();
        userName = update.getMessage().getFrom().getUserName();
        textToSend = String.format(
                "User ID: %d\n " +
                        "Username: %s\n " +
                        "Message: %s",
                userId, userName, update.getMessage().getText());

        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotUsername() {
        return "@HedgehogQueueBot";
    }

    @Override
    public String getBotToken() {
        return "5714181264:AAHGrmqcQqtzki1vEw2x6aoTM2-BzMedGGo";
    }
}
