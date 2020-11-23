package r.austria.debra;

import r.austria.Donation;
import r.austria.DonationListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebraDonationParser implements TotalDonationSource {

    private static final String AMOUNT_PATTERN = "var collected = '(.*)';";
    private static final Pattern PATTERN = Pattern.compile(AMOUNT_PATTERN);

    private static final Logger LOG = Logger.getLogger(DebraDonationParser.class.getName());

    private final String url;


    public DebraDonationParser(String url) {
        this.url = url;
    }

    @Override
    public synchronized BigDecimal getTotalAmount() {
        return fetchAmountFromHtml();
    }

    private BigDecimal fetchAmountFromHtml() {
        LOG.info(() -> "Fetching donatino amount from debra.");
        try (InputStream is = new URL(this.url).openStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = PATTERN.matcher(line);
                if (matcher.find()) {
                    return new BigDecimal(matcher.group(1).replace(',', '.'));
                }
            }
            LOG.warning(() -> "Could not fetch donation amount from Debra.");

            return null;
        } catch (IOException e) {
            LOG.severe(() -> "Error while parsing HTML page: " + e.getMessage());
        }
        return null;
    }

}
