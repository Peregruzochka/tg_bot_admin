package ru.peregruzochka.tg_bot_admin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;

@SpringBootApplication
@RequiredArgsConstructor
@EnableFeignClients(basePackages = "ru.peregruzochka.tg_bot_admin.client")
public class TgBotAdminApplication {
	private final TelegramBot bot;

	public static void main(String[] args) {
		SpringApplication.run(TgBotAdminApplication.class, args);
	}

	@Bean
	public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
		TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
		botApi.registerBot(bot);
		return botApi;
	}
}
