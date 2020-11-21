package r.austria;

import javax.security.auth.login.LoginException;

public class DonationBotApplication {

    public static void main(String[] args) throws LoginException {
        IncomingDonationListener incomingDonationListener = new IncomingDonationListener("ws://spenden.baba.fm:8765/");
    }
}
