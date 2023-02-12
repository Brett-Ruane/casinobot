package discordcasino;

import java.util.Collections;

import javafx.application.Application;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class App extends ListenerAdapter {

    private static String[] a;

    public static void main(String[] args) {
        a = args;
        JDA casino = JDABuilder
                .createLight("MTA3MzUyNTAyMjEwNDIyNzg2MA.GTnhor.1JHNIcr_7AZUKXo4_6AhQppZPEuVK2YMMAU7IM",
                        Collections.emptyList())
                .addEventListeners(new App())
                .setActivity(Activity.playing("Type /blackjack"))
                .build();
        casino.updateCommands().addCommands(Commands.slash("blackjack", "Plays BlackJack")).queue();
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // make sure we handle the right command
        switch (event.getName()) {
            case "blackjack":
                event.reply("working on it!!!");
                Application.launch(BlackJackUI.class, a);
                break;
        }
    }
}
