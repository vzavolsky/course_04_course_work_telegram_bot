package bot.reminder.services;

import bot.reminder.entities.TelegramBotNotification;
import bot.reminder.repositories.TelegramBotNotificationRepository;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotService {

    private final TelegramBotNotificationRepository telegramBotNotificationRepository;

    public TelegramBotService(TelegramBotNotificationRepository telegramBotNotificationRepository) {
        this.telegramBotNotificationRepository = telegramBotNotificationRepository;
    }

    //private Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\:\\d{2}) (.{0,255})");
    private Matcher matcher;

    public String returnMessage(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        String userName = update.message().from().firstName();

        if (text.equals("/start")) {
            return String.format("Greetings you, %s.", userName);
        }

        matcher = pattern.matcher(text);
        if (matcher.matches()) {
            add(update);
            return "Something was added.";
        }

        return String.format("Hi again, %s.", userName);
    }

    private Collection<TelegramBotNotification> getAllByChatId(Long chatId) {
        return telegramBotNotificationRepository.getAllByChatId(chatId);
    }

    private boolean isNotificationExistsByUserId(Long chatId) {
        return getAllByChatId(chatId).size() > 0;
    }

    private void add(Update update) {
        TelegramBotNotification telegramBotNotification = new TelegramBotNotification();
        telegramBotNotification.setChatId(update.message().chat().id());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), formatter);

        telegramBotNotification.setDate(dateTime);
        telegramBotNotification.setText(matcher.group(2));

        telegramBotNotificationRepository.save(telegramBotNotification);

        // ----- //
        System.out.println(update.message().chat().id());
        System.out.println(matcher.groupCount());
        System.out.println(matcher.group(0));
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
        System.out.println(dateTime);
        System.out.println(telegramBotNotification);
    }
}
