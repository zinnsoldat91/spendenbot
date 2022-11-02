package r.austria;

import r.austria.debra.DebraDonationParser;
import r.austria.discord.DiscordBot;
import r.austria.websocket.DonationWebsocketListener;

import javax.security.auth.login.LoginException;

public class DonationBotApplication {

    public static void main(String[] args) throws LoginException {
        DonationWebsocketListener incomingDonationListener = new DonationWebsocketListener("ws://spenden.baba.fm:8765/");

        DiscordBot discordBot = new DiscordBot();
        DebraDonationParser debraDonationParser = new DebraDonationParser("https://em.altruja.de/discord-und-r-austria-2021");
        discordBot.setTotalDonationSource(debraDonationParser);

        incomingDonationListener.addDonationListener(discordBot);

    }
}
