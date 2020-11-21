package r.austria.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import r.austria.Donation;
import r.austria.DonationListener;
import r.austria.debra.DonationSource;
import r.austria.debra.MockDebraSource;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class DiscordBot extends ListenerAdapter implements DonationListener {

    private final static Logger LOG = Logger.getLogger(DiscordBot.class.getName());
    private final static String footerImage = "https://raw.githubusercontent.com/zinnsoldat91/spendenbot/main/src/main/resources/images/embed_logo.png";

    private boolean ready = false;
    private final JDA discordApi;
    private DonationSource donationSource = new MockDebraSource();

    public DiscordBot() throws LoginException {
        discordApi = JDABuilder.createDefault("Nzc4MzE1MTEwMTgyNTUxNjIz.X7QMbg.AUgkM--c0GuNNQIQ4h_kiU4DI64")
                .addEventListeners(this)
                .build();
    }

    @Override
    public void onDonationReceived(Donation donation) {
        sendDonationMessage(donation);
    }

    private void sendDonationMessage(Donation donation) {
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
        builder.addField("Aktueller Spendenbetrag", donationSource.getTotalDonations().getTotalAmount().toString(), true);
        builder.addField("Anzahl Spenden", Integer.toString(donationSource.getTotalDonations().getNumberOfDonations()), true);
        builder.setDescription(donation.getMessage());
        builder.setTitle(String.format(":bell: %s hat %.2f Euro gespendet :bell:", donation.getDonator(), donation.getAmount()));
        builder.setFooter("Spende auch du für Debra Austria unter https://tinyurl.cc/schmetterling2020", footerImage);
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
