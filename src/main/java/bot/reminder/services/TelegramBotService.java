package bot.reminder.services;

import bot.reminder.entities.TelegramBotMessages;
import bot.reminder.entities.TelegramBotNotification;
import bot.reminder.repositories.TelegramBotNotificationRepository;
import com.pengrad.telegrambot.model.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelegramBotService {

    private final TelegramBotNotificationRepository telegramBotNotificationRepository;
    private TelegramBotMessages message = new TelegramBotMessages();

    public TelegramBotService(TelegramBotNotificationRepository telegramBotNotificationRepository) {
        this.telegramBotNotificationRepository = telegramBotNotificationRepository;
    }

    private Pattern pattern = Pattern.compile("^(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\:\\d{2}) (.{0,255})");
    private Pattern delPattern = Pattern.compile("^(/delete) (\\d{0,30})");
    Matcher matcher;

    //private Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    public String returnMessage(Update update) {


        Long chatId = update.message().chat().id();
        String text = update.message().text();
        String userName = update.message().from().firstName();

        matcher = pattern.matcher(text);
        if (matcher.matches()) {
            add(update);
            return "Something was added.";
        }

        matcher = delPattern.matcher(text);
        if (matcher.matches()) {
            return delete(chatId);
        }

        switch (text) {
            case "/start":
                return message.start(userName);
            case "/show":
                return getAllByChatIdtoString(chatId);
            case "/help":
                return message.help();
            case "/info":
                return message.info();
            default:
                return String.format("Hi again, %s.", userName);
        }
    }

    private Collection<TelegramBotNotification> getAllByChatId(Long chatId) {
        return telegramBotNotificationRepository.getAllByChatId(chatId);
    }

    private String getAllByChatIdtoString(Long chatId) {
        return getAllByChatId(chatId).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
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
    }

    private String delete(Long chatId) {
        Long noteId = Long.valueOf((matcher.group(2)));
        TelegramBotNotification note = telegramBotNotificationRepository.findById(noteId).get();
        if (chatId.equals(note.getChatId())) {
            telegramBotNotificationRepository.deleteById(noteId);
            return message.deleted(note.getText());
        }
        return message.notFound();
    }

    public Collection<TelegramBotNotification> getCurrentNotes() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Collection<TelegramBotNotification> currentNotes = telegramBotNotificationRepository.getByDate(currentTime);
        return currentNotes;
    }
}
