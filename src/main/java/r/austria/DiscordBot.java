package r.austria;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class DiscordBot extends ListenerAdapter {

    private final static Logger LOG = Logger.getLogger(DiscordBot.class.getName());

    private boolean ready = false;
    private final JDA discordApi;

    public DiscordBot() throws LoginException {
        discordApi = JDABuilder.createDefault("Nzc4MzE1MTEwMTgyNTUxNjIz.X7QMbg.LAmOcYJ54B8lxYpt1vx-QCaennI")
                .addEventListeners(this)
                .build();
    }

    public void sendDonationMessage(Donation donation) {
        if (ready) {
            LOG.info(String.format("Sending discord message for donation %s", donation));
            discordApi.getGuildById("778314765310361622").getTextChannelById("778314765310361625")
                    .sendMessage(buildEmbed(donation))
                    .submit();
        } else {
            LOG.warning("Discord API not ready, could not send message.");
        }
    }

    private MessageEmbed buildEmbed(Donation donation) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("Spender", donation.getDonator(), true);
        builder.addField("Betrag", donation.getAmount().toString(), true);
        builder.setDescription(donation.getMessage());
        builder.setTitle("Parteispendenalarm!");
        builder.setFooter("Spende auch du unter https://tinyurl.cc/schmetterling2020");
        return builder.build();
    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        ready = true;
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        super.onDisconnect(event);
        ready = false;
    }
}
