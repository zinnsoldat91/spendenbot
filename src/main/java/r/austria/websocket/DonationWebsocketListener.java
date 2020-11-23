package r.austria.websocket;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import r.austria.Donation;
import r.austria.DonationListener;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DonationWebsocketListener extends WebSocketListener {

    private static final Logger LOG = Logger.getLogger(DonationWebsocketListener.class.getName());
    private final List<DonationListener> donationListeners = new ArrayList<>();
    private final String url;

    public DonationWebsocketListener(String url) throws LoginException {
        this.url = url;
        createWebsocketConnection();
    }

    public void addDonationListener(DonationListener donationListener) {
        this.donationListeners.add(donationListener);
    }

    private void notifyDonationListeners(Donation donation) {
        for (DonationListener donationListener : this.donationListeners) {
            donationListener.onDonationReceived(donation);
        }
    }

    private void createWebsocketConnection() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        LOG.info(String.format("Connected to %s", url));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {
        LOG.info(String.format("Received message %s", message));
        Donation donation = new Donation(message);
        this.notifyDonationListeners(donation);
    }
}
