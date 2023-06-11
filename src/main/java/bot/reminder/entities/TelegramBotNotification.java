package bot.reminder.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class TelegramBotNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private String text;
    private Long chatId;

    public TelegramBotNotification() {
    }

    public TelegramBotNotification(Long chatId, String text, LocalDateTime date) {
        this.date = date;
        this.text = text;
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TelegramBotNotification{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", chatId=" + chatId +
                '}';
    }
}
