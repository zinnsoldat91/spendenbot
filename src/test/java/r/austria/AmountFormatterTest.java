package r.austria;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountFormatterTest {

    @Test
    public void testFormatInteger(){
        BigDecimal amount = new BigDecimal("50");
        String formatted = new AmountFormatter(amount).getFormattedAmount();
        Assertions.assertThat(formatted).isEqualTo("50,00");
    }

    @Test
    public void testFormatFloat(){
        BigDecimal amount = new BigDecimal("50.50");
        String formatted = new AmountFormatter(amount).getFormattedAmount();
        Assertions.assertThat(formatted).isEqualTo("50,50");
    }

    @Test
    public void testMoreThanThousand(){
        BigDecimal amount = new BigDecimal("1050.50");
        String formatted = new AmountFormatter(amount).getFormattedAmount();
        Assertions.assertThat(formatted).isEqualTo("1.050,50");
    }

}