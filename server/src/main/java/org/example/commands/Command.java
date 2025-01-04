package org.example.commands;

import org.example.Player;

public interface Command {
    public abstract String execute(Player player, String[] args);
}
