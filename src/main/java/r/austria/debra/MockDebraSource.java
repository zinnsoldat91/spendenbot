package r.austria.debra;

import java.math.BigDecimal;

public class MockDebraSource implements DonationSource {
    @Override
    public TotalDonations getTotalDonations() {
        return new TotalDonations(10, new BigDecimal("200.49"));
    }
}
