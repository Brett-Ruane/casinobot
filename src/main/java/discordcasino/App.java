package discordcasino;

import java.io.File;
import java.util.Collections;

import javafx.application.Application;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;

public class App extends ListenerAdapter {

    private static String[] a;

    public static void main(String[] args) throws Exception {
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
        switch (event.getName()) {
            case "blackjack":
                File file = new File("GameCards/back1.GIF");
                FileUpload upload = FileUpload.fromData(file);
                event.getChannel().sendMessage("Here is my image !").addFiles(upload).queue();
                event.reply("working on it!!!").setEphemeral(true).queue();
                break;
        }
    }
}
