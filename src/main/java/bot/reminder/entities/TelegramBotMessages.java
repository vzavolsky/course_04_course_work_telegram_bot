package bot.reminder.entities;

public class TelegramBotMessages {

    public String start(String userName) {
        return String.format(
                "Hey you, %s. What's up?\n" +
                        "This is ReminderBot. Where you can create reminder notes.\n" +
                        "/info - more info;\n" +
                        "/help - all commands. ", userName);
    }

    public String help() {
        return String.format(
                "All commands:\n" +
                        "/show - show your all notes;\n" +
                        "/delete {note_id} - delete specific note (type it manually);\n" +
                        "/info - how to use it."
        );
    }

    public String info() {
        return String.format(
                "How to use it?\n" +
                        "Just create a note with format: 01.01.2022 20:00 Text of you note.\n" +
                        "And wait this notification at the specific time.");
    }

    public String deleted(String note) {
        return String.format("Note \"%s\" was deleted.", note);
    }

    public String notFound() {
        return "The note wasn't found.";
    }

    public String unknow() {
        return "Hey, dude. That command wasn't reserved. Check /help command. :)";
    }
}
