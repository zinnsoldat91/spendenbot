package r.austria;

import r.austria.discord.DiscordBot;
import r.austria.websocket.DonationWebsocketListener;

import javax.security.auth.login.LoginException;

public class DonationBotApplication {

    public static void main(String[] args) throws LoginException {
        DonationWebsocketListener incomingDonationListener = new DonationWebsocketListener("ws://spenden.baba.fm:8765/");
        incomingDonationListener.addDonationListener(new DiscordBot());
    }
}
