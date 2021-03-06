package me.stuntguy3000.java.quotesbot.handler;

import me.stuntguy3000.java.quotesbot.object.Command;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.HashMap;

/**
 * Created by amir on 2015-11-25.
 */
public class CommandHandler {
    public HashMap<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();
    }

    public boolean executeCommand(String s, CommandMessageReceivedEvent event) {
        Command cmd = commands.get(s.toLowerCase());
        if (cmd == null) {
            return false;
        }
        cmd.processCommand(event);
        return true;
    }

    public String getBotFatherString() {
        StringBuilder sb = new StringBuilder();
        for (Command cmd : commands.values()) {
            sb
                    .append(cmd.createBotFatherString())
                    .append("\n");
        }

        return sb.toString();
    }

    public void registerCommand(Command cmd) {
        commands.put(cmd.getName().toLowerCase(), cmd);
    }
}
