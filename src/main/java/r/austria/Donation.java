package r.austria;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Donation {

    private static final String MESSAGE_PATTERN = "(.*) hat (\\d{1,9},\\d{2}) Euro gespendet!<br \\/>Vielen Dank!<br \\/>Nachricht:<br \\/>(.*)";
    private static final Pattern PATTERN = Pattern.compile(MESSAGE_PATTERN);

    public Donation(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.find()) {
            this.donator = matcher.group(1);
            String amountString = matcher.group(2);
            this.amount = new BigDecimal(amountString.replace(',', '.'));
            this.message = matcher.group(3);
        } else {
            throw new IllegalArgumentException("String in Wrong format");
        }
    }

    private final String donator;
    private final BigDecimal amount;
    private final String message;

    public String getDonator() {
        return donator;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donator='" + donator + '\'' +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                '}';
    }
}