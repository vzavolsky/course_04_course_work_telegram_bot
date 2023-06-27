package bot.reminder.listener;

import bot.reminder.entities.TelegramBotNotification;
import bot.reminder.services.TelegramBotService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBotService telegramBotService;

    private Long chatId;
    private String messageText;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {} - {}",
                    update.message().chat().id(),
                    update.message().text());
            // Process your updates here
            if (update != null) {
                chatId = update.message().chat().id();
                messageText = telegramBotService.returnMessage(update);
                SendMessage message = new SendMessage(chatId, messageText);

                SendMessage buttons = message.replyMarkup(
                        new ReplyKeyboardMarkup(
                                new String[]{"Button 1", "Button 2"},
                                new String[]{"Button 3"}
                        )
                );

                /*SendMessage buttons3 = message.replyMarkup(
                        new ReplyKeyboardRemove()
                );*/
                SendResponse response = telegramBot.execute(message);

            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        Collection<TelegramBotNotification> currentNotes = telegramBotService.getCurrentNotes();
        if (currentNotes.size() > 0) {
            currentNotes.
                    forEach(telegramBotNotification -> {
                        SendMessage message = new SendMessage(telegramBotNotification.getChatId(), telegramBotNotification.getText());
                        SendResponse response = telegramBot.execute(message);
                    });
        }
    }

}
