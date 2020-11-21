package r.austria;

import okhttp3.*;
import okio.ByteString;
import r.austria.discord.DiscordBot;

import javax.security.auth.login.LoginException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Playground extends WebSocketListener {
    public static void main(String[] args) throws LoginException, InterruptedException {
        Donation donation = new Donation("Zinnsoldat", new BigDecimal("50.00"), "Hawara es is sau geil");
        DiscordBot bot = new DiscordBot();
        Thread.sleep(1000L);
        bot.onDonationReceived(donation);
    }
}