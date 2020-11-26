package r.austria.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import r.austria.AmountFormatter;
import r.austria.Donation;
import r.austria.DonationListener;
import r.austria.debra.TotalDonationSource;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DiscordBot extends ListenerAdapter implements DonationListener {

    private final static Logger LOG = Logger.getLogger(DiscordBot.class.getName());
    private final static String footerImage = "https://raw.githubusercontent.com/zinnsoldat91/spendenbot/main/src/main/resources/images/embed_logo.png";

    private final JDA discordApi;
    private TotalDonationSource totalDonationSource;

    private final String guildId;
    private final List<String> channelIds;

    public DiscordBot() throws LoginException {
        discordApi = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN"))
                .addEventListeners(this)
                .build();
        this.guildId = Objects.requireNonNull(System.getenv("DISCORD_GUILD_ID"));
        channelIds = fetchChannelIds();
    }

    private List<String> fetchChannelIds() {
        final String commaSeparatedIds = System.getenv("DISCORD_CHANNEL_IDS");
        return Arrays.stream(commaSeparatedIds.split(",")).map(String::trim).collect(Collectors.toList());
    }

    @Override
    public void onDonationReceived(Donation donation) {
        sendDonationMessage(donation);
    }

    private void sendDonationMessage(Donation donation) {
        try {
            MessageEmbed message = buildEmbed(donation);
            for (String channel : channelIds) {
                LOG.info(String.format("Sending discord message for donation %s", donation));
                discordApi.getGuildById(guildId).getTextChannelById(channel)
                        .sendMessage(message)
                        .submit();
            }
        } catch (Exception e) {
            LOG.severe("Error while sending message: " + e.getMessage());
        }
    }

    public void setTotalDonationSource(TotalDonationSource totalDonationSource) {
        this.totalDonationSource = totalDonationSource;
    }

    private MessageEmbed buildEmbed(Donation donation) {
        EmbedBuilder builder = new EmbedBuilder();
        if (totalDonationSource != null) {
            BigDecimal totalAmount = totalDonationSource.getTotalAmount();
            if (totalAmount != null) {
                builder.addField("Aktueller Spendenbetrag", formatAmount(totalAmount) + " Euro", true);
            }
        }
        builder.setDescription(donation.getMessage());
        builder.setTitle(String.format(":bell: %s hat %s Euro gespendet :bell:", donation.getDonator(), formatAmount(donation.getAmount())));
        builder.setFooter("Spende auch du für Debra Austria unter http://tiny.cc/schmetterling2020", footerImage);
        return builder.build();
    }

    private String formatAmount(BigDecimal amount) {
        return new AmountFormatter(amount).getFormattedAmount();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            String content = event.getMessage().getContentRaw();
            if (content.equalsIgnoreCase("!spenden")) {
                if (totalDonationSource != null) {
                    BigDecimal totalAmount = totalDonationSource.getTotalAmount();
                    if (totalAmount != null) {
                        event.getTextChannel().sendMessage("Aktuell wurden **" + formatAmount(totalAmount) + " Euro** für Debra Austria gespendet. Spende auch du unter https://tiny.cc/schmetterling2020").submit();
                    }
                }
            }
        } catch (Exception e) {
            LOG.severe("Error while sending message: " + e.getMessage());
        }
    }
}
