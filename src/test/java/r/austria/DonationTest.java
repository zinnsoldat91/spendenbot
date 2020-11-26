package r.austria;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DonationTest {

    @Test
    public void test_Donation_from_message(){
        String message = "Jemand hat 50,00 Euro gespendet!<br />Vielen Dank!<br />Nachricht:<br />Danke Braka, daß du die Aktion abwickelst, danke deine Tätigkeit fürr/Austria, danke für den Discordserver, allen Beteiligten ein herzlichesDankeschön!";
        Donation donation = new Donation(message);
        assertThat(donation.getAmount()).isEqualByComparingTo("50.00");
        assertThat(donation.getDonator()).isEqualTo("Jemand");
        assertThat(donation.getMessage()).isEqualTo("Danke Braka, daß du die Aktion abwickelst, danke deine Tätigkeit fürr/Austria, danke für den Discordserver, allen Beteiligten ein herzlichesDankeschön!");

    }


    @Test
    public void test_remove_br(){
        String message = "Jemand hat 50,00 Euro gespendet!<br />Vielen Dank!<br />Nachricht:<br />Danke Braka,<br><br>daß du die Aktion abwickelst, danke deine Tätigkeit fürr/Austria, danke für den Discordserver, allen Beteiligten ein herzlichesDankeschön!<br><br>";
        Donation donation = new Donation(message);
        assertThat(donation.getAmount()).isEqualByComparingTo("50.00");
        assertThat(donation.getDonator()).isEqualTo("Jemand");
        assertThat(donation.getMessage()).isEqualTo("Danke Braka, daß du die Aktion abwickelst, danke deine Tätigkeit fürr/Austria, danke für den Discordserver, allen Beteiligten ein herzlichesDankeschön!");

    }

}