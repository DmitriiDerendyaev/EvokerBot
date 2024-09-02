package ru.derendyaev.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.derendyaev.bot.EvokerBotService;

@Configuration
public class BotInitializer {

    @Bean
    public TelegramBotsApi telegramBotsApi(EvokerBotService botService) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(botService);
        return telegramBotsApi;
    }
}

