package discordcasino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class App extends ListenerAdapter {

    private BlackJack game = new BlackJack();

    public static void main(String[] args) throws Exception {
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
                game = new BlackJack();
                event.reply("Black Jack").setEphemeral(true).queue();
                List<FileUpload> arrayD = game.getDealerCardsUploads();
                event.getChannel().sendMessage("Dealers Cards")
                        .addFiles(arrayD.get(0), arrayD.get(1))
                        .queue();
                Button hit = Button.primary("Hit", "Hit");
                Button stand = Button.danger("Stand", "Stand");
                final List<ItemComponent> list = new ArrayList<>();
                list.add(hit);
                list.add(stand);
                List<FileUpload> array = game.getCardsUploads();
                event.getChannel().sendMessage("Your Cards " + game.getOne().total())
                        .addFiles(array.get(0), array.get(1))
                        .setActionRow(list)
                        .queue();
                break;
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("Hit")) {
            System.out.println("HIT PRESSED");
            game.hit();
            List<FileUpload> array = game.getCardsUploads();
            // event.editMessageAttachments(array).queue();
            // event.editMessage("Your Cards; total = edit" +
            // game.getOne().total()).queue();
            // fixed
            MessageEditData m = new MessageEditBuilder().setContent("Your Cards; total = edit" + game.getOne().total())
                    .setFiles(array)
                    .build();
            event.editMessage(m).queue();
            System.out.println(game.getOne().total());
        }
    }
}
