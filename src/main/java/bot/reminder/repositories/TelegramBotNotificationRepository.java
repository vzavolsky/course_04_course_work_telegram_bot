package bot.reminder.repositories;

import bot.reminder.entities.TelegramBotNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TelegramBotNotificationRepository extends JpaRepository<TelegramBotNotification, Long> {

    @Query(value = "from TelegramBotNotification t where t.chatId = :chatId")
    Collection<TelegramBotNotification> getAllByChatId(Long chatId);

    Collection<TelegramBotNotification> getByDate(LocalDateTime date);
}
