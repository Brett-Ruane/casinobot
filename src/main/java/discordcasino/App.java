package discordcasino;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
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
                event.reply("Black Jack").setEphemeral(true).queue();
                BlackJack game = new BlackJack();
                Button hit = Button.primary("Hit", "Hit");
                Button stand = Button.danger("Stand", "Stand");
                final List<ItemComponent> list = new ArrayList<>();
                list.add(hit);
                list.add(stand);
                List<FileUpload> array = game.getCardsUploads();
                event.getChannel().sendMessage("total = " + game.getOne().total()).addFiles(array.get(0), array.get(1))
                        .setActionRow(list)
                        .queue();
                break;
        }
    }
}
