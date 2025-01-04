package org.example;

import org.example.commands.Command;
import org.reflections.Reflections;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import static org.example.PokerServer.players;

public class CommandManager {
    private static final Map<String, Class<? extends Command>> commands = new HashMap<>();

    static {
        // Automatyczne skanowanie klas w pakiecie
        Reflections reflections = new Reflections("org.example.commands");
        for (Class<? extends Command> clazz : reflections.getSubTypesOf(Command.class)) {
            commands.put(clazz.getSimpleName().toLowerCase().replace("command",""), clazz);
        }
    }

    public static Command getCommand(String name) {
        Class<? extends Command> commandClass = commands.get(name.toLowerCase());
        if (commandClass == null) {
            return new Command() {
                @Override
                public String execute(Player player, String[] args) {
                    return "invalid command";
                }
            };
        }
        try {
            return commandClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate command: " + name, e);
        }
    }

    public static void manage(Player player, String text) throws IOException {

        String[] splited = text.toLowerCase().split(" ");
        Command command = getCommand(splited[0]);
        String output = command.execute(player, splited);
        Handlers.send(getPlayerChannel(player), output);
    }

    public static SocketChannel getPlayerChannel(Player player) {
        for (Map.Entry<SocketChannel, Player> entry : players.entrySet()) {
            if (entry.getValue().equals(player)) {
                return entry.getKey();
            }
        }
        return null; // Jeśli nie znaleziono gracza
    }

}
