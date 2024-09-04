package ru.derendyaev.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class EvokerBotService extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            System.out.println(update);

            Long userId = update.getMessage().getChatId();

            switch (command) {
                case "/whenAwake":
                    LocalDateTime telegramTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(update.getMessage().getDate()), ZoneId.systemDefault());
                    List<LocalDateTime> timeAwakeList = List.of(
                            telegramTime.plusHours(1).plusMinutes(30),
                            telegramTime.plusHours(3),
                            telegramTime.plusHours(4).plusMinutes(30),
                            telegramTime.plusHours(6),
                            telegramTime.plusHours(7).plusMinutes(30),
                            telegramTime.plusHours(9)
                    );
                    sendMessageWithButtons(update.getMessage().getChatId(), timeAwakeList);
                    break;
                default:
                    String textToSend = "Send me command:\n" +
                            "/whenAwake";

                    sendMessageToUser(userId, textToSend);
            }
        }

        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }

    private void sendMessageWithButtons(Long chatId, List<LocalDateTime> timeAwakeList) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
         // TODO: Добавить опционально
         // message.setText(getRecommendedSleepTime(timeAwakeList));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        int counter = 0;
        for (LocalDateTime time : timeAwakeList) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String buttonText = String.format("%d:%d", time.getHour(), time.getMinute());
            button.setText(buttonText);
            button.setCallbackData(buttonText);
            row.add(button);

            if (++counter % 3 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        if (!row.isEmpty()) {
            rows.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getRecommendedSleepTime(List<LocalDateTime> timeAwakeList) {
        return String.format("Рекомендуется проснуться в %d:%d или %d:%d, чтобы сон прошел 5-6 полных циклов\n" +
                        "Вы можете проснуться в:",
                timeAwakeList.get(timeAwakeList.size()-2).getHour(), timeAwakeList.get(timeAwakeList.size()-2).getMinute(),
                timeAwakeList.get(timeAwakeList.size()-1).getHour(), timeAwakeList.get(timeAwakeList.size()-1).getMinute());
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();  // Получаем значение кнопки
        Long chatId = callbackQuery.getMessage().getChatId();  // Получаем chatId

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Ставь будильник на: " + callbackData + "\nСладких снов)");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendMessageToUser(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
