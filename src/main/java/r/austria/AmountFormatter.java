package r.austria;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class AmountFormatter {
    private final BigDecimal amount;

    public AmountFormatter(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    public String getFormattedAmount() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(amount);
    }
}
