package discordcasino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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
                Button stand = Button.danger("Stand", "Stand");
                event.getChannel()
                        .sendMessage(
                                "Dealer's Cards; total = " + game.getDealerCards().get(0).pointValue() + " + ?")
                        .addFiles(arrayD.get(0), arrayD.get(1))
                        .setActionRow(stand)
                        .queue();
                Button hit = Button.primary("Hit", "Hit");
                List<FileUpload> array = game.getCardsUploads();
                event.getChannel().sendMessage("Your Cards; total = " + game.getOne().total())
                        .addFiles(array.get(0), array.get(1))
                        .setActionRow(hit)
                        .queue();
                break;
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("Hit")) {
            System.out.println("HIT PRESSED");
            String str = game.hit();
            List<FileUpload> array = game.getCardsUploads();
            // event.editMessageAttachments(array).queue();
            // event.editMessage("Your Cards; total = edit" +
            // game.getOne().total()).queue();
            // fixed
            MessageEditData m = new MessageEditBuilder().setContent("Your Cards; total = " + game.getOne().total())
                    .setFiles(array)
                    .build();
            event.editMessage(m).queue();
            System.out.println(game.getOne().total());
        }
        if (event.getButton().getId().equals("Stand")) {
            System.out.println("STAND PRESSED");
            String str = game.stand();
            List<FileUpload> array = game.getDealerCardsUploads();
            // event.editMessageAttachments(array).queue();
            // event.editMessage("Your Cards; total = edit" +
            // game.getOne().total()).queue();
            // fixed
            MessageEditData m = new MessageEditBuilder()
                    .setContent("Dealer's Cards; total = " + game.getDealer().total())
                    .setFiles(array)
                    .build();
            event.editMessage(m).queue();
            event.reply("You" + str);
            System.out.println(game.getOne().total());
        }
    }
}
