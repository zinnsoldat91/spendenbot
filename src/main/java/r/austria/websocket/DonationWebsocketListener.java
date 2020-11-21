package r.austria.websocket;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import r.austria.DonationListener;
import r.austria.discord.DiscordBot;
import r.austria.Donation;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DonationWebsocketListener extends WebSocketListener {

    private static final Logger LOG = Logger.getLogger(DonationWebsocketListener.class.getName());
    private final List<DonationListener> donationListeners = new ArrayList<>();
    private boolean connected;
    private WebSocket webSocket;
    private final String url;


    public DonationWebsocketListener(String url) throws LoginException {
        this.url = url;
        this.webSocket = createWebsocketConnection();
    }

    public void addDonationListener(DonationListener donationListener) {
        this.donationListeners.add(donationListener);
    }

    private void notifyDonationListeners(Donation donation) {
        for(DonationListener donationListener : this.donationListeners) {
            donationListener.onDonationReceived(donation);
        }
    }

    private WebSocket createWebsocketConnection() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        WebSocket webSocket = client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
        return webSocket;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        this.connected = true;
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {
        LOG.info(String.format("Received message %s", message));
        Donation donation = new Donation(message);
        this.notifyDonationListeners(donation);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        LOG.warning("Donation Websocket closing");
        super.onClosing(webSocket, code, reason);
        connected = false;
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        LOG.warning("Donation Websocket closed");
        while (!connected) {
            LOG.warning("Trying to reconnect");
            this.webSocket = createWebsocketConnection();
            try{
                Thread.sleep(5000L);
            }catch (InterruptedException e){
                LOG.severe(e.getMessage());
            }
        }
    }
}
