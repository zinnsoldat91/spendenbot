package r.austria.debra;

import java.math.BigDecimal;

public class TotalDonations {

    private final BigDecimal totalAmount;
    private final int numberOfDonations;

    public TotalDonations(int numberOfDonations, BigDecimal totalAmount) {
        this.numberOfDonations = numberOfDonations;
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public int getNumberOfDonations() {
        return numberOfDonations;
    }
}
