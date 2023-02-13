package discordcasino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class App extends ListenerAdapter {

        private BlackJack game = new BlackJack();

        SlashCommandInteractionEvent bustAct;
        private long messageId;
        String standID = "";
        private boolean bust = false;
        private static EventWaiter waiter;

        List<FileUpload> arrayD = new ArrayList<FileUpload>();

        private String str = "";

        private Button hit;
        private Button stand;

        public static void main(String[] args) throws Exception {
                waiter = new EventWaiter();
                JDA casino = JDABuilder
                                .createLight("MTA3MzUyNTAyMjEwNDIyNzg2MA.GTnhor.1JHNIcr_7AZUKXo4_6AhQppZPEuVK2YMMAU7IM",
                                                Collections.emptyList())
                                .addEventListeners(new App(), waiter)
                                .setActivity(Activity.playing("Type /blackjack"))
                                .build();
                casino.updateCommands().addCommands(Commands.slash("blackjack", "Plays BlackJack")).queue();
        }

        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                switch (event.getName()) {
                        case "blackjack":
                                game = new BlackJack();
                                event.reply("Black Jack").setEphemeral(true).queue();
                                arrayD = game.getDealerCardsUploads();
                                stand = Button.danger("Stand", "Stand");
                                event.getChannel()
                                                .sendMessage(
                                                                "Dealer's Cards; total = " + game.getDealerCards()
                                                                                .get(0).pointValue() + " + ?")
                                                .addFiles(arrayD.get(0), arrayD.get(1))
                                                .setActionRow(stand).queue((message) -> {
                                                        messageId = message.getIdLong();
                                                });
                                bustAct = event;
                                standID = event.getChannel().getLatestMessageId();
                                hit = Button.primary("Hit", "Hit");
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
                        System.out.println("r# = " + event.getResponseNumber());
                        System.out.println("HIT PRESSED");
                        str = game.hit();
                        List<FileUpload> array = game.getCardsUploads();
                        // event.editMessageAttachments(array).queue();
                        // event.editMessage("Your Cards; total = edit" +
                        // game.getOne().total()).queue();
                        // fixed
                        if (str != "") {
                                bust = true;
                                final List<LayoutComponent> list = new ArrayList<>();
                                list.add(ActionRow.of(hit.asDisabled()));
                                MessageEditData m = new MessageEditBuilder()
                                                .setContent("Your Cards; total = " + game.getOne().total())
                                                .setFiles(array)
                                                .setComponents(list)
                                                .build();
                                event.editMessage(m).queue();
                                System.out.println("standID = " + standID);
                                changeStand(bustAct);
                        } else {
                                MessageEditData m = new MessageEditBuilder()
                                                .setContent("Your Cards; total = " + game.getOne().total())
                                                .setFiles(array)
                                                .build();
                                event.editMessage(m).queue();
                        }
                        System.out.println(game.getOne().total());
                }
                if (event.getButton().getId().equals("Stand")) {
                        System.out.println("EVENT = " + event.getButton().getId());
                        // if (bust == true) {
                        // stand.asDisabled();
                        // waiter.waitForEvent(ButtonInteractionEvent.class,
                        // e -> e.getUser().equals(event.getUser())
                        // && e.getChannel().equals/(event.getChannel())
                        // && !e.getMessage().equals(event.getMessage()),
                        // e -> event.editButton(stand).queue());
                        // }
                        System.out.println("STAND PRESSED");
                        str = game.stand();
                        List<FileUpload> array = game.getDealerCardsUploads();
                        // event.editMessageAttachments(array).queue();
                        // event.editMessage("Your Cards; total = edit" +
                        // game.getOne().total()).queue();
                        // fixed
                        final List<LayoutComponent> list = new ArrayList<>();
                        list.add(ActionRow.of(stand.asDisabled()));
                        MessageEditData m = new MessageEditBuilder()
                                        .setContent("Dealer's Cards; total = " + game.getDealer().total())
                                        .setFiles(array)
                                        .setComponents(list)
                                        .build();
                        event.editMessage(m).queue();
                        System.out.println(game.getDealer().total());
                        event.getChannel().sendMessage("<@" + event.getMember().getUser().getId() + "> - You " + str)
                                        .queue();
                }
        }

        public void changeStand(SlashCommandInteractionEvent s) {
                System.out.println("Stand mID = " + stand.getId());
                final List<LayoutComponent> list = new ArrayList<>();
                list.add(ActionRow.of(stand.asDisabled()));
                MessageEditData m = new MessageEditBuilder()
                                .setContent("Dealer's Cards; total = " + game.getDealerCards()
                                                .get(0).pointValue() + " + ?")
                                .setFiles(arrayD.get(0), arrayD.get(1))
                                .setComponents(list)
                                .build();
                s.getChannel().editMessageById(messageId, m).queue();
                s.getChannel().sendMessage("<@" + s.getMember().getUser().getId() + "> - You " + str)
                                .queue();
        }
}
