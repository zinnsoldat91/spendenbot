package r.austria;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class IncomingDonationListener extends WebSocketListener {

    private static final Logger LOG = Logger.getLogger(IncomingDonationListener.class.getName());
    private final DiscordBot discordBot;
    private boolean connected;
    private WebSocket webSocket;
    private String url;


    public IncomingDonationListener(String url) throws LoginException {

        this.url = url;
        this.webSocket = createWebsocketConnection();

        discordBot = new DiscordBot();
    }

    private WebSocket createWebsocketConnection() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
        return client.newWebSocket(request, this);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        this.connected = true;
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {
        LOG.info(String.format("Received message %s", message));
        Donation donation = new Donation(message);
        discordBot.sendDonationMessage(donation);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        connected = false;
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        while (!connected) {
            this.webSocket = createWebsocketConnection();
            try{
                Thread.sleep(5000L);
            }catch (InterruptedException e){
                LOG.severe(e.getMessage());
            }
        }
    }
}
