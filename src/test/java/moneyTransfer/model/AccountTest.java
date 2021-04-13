package moneyTransfer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {
    private static final ObjectMapper mapper = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final Account account = new Account(false,1,123, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        final String expected = mapper.writeValueAsString(
                mapper.readValue(fixture("account.json"), Account.class));

        assertThat(mapper.writeValueAsString(account)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Account account = new Account(false,1,123, Currency.getInstance("TRY").toString(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        assertThat(mapper.readValue(fixture("account.json"), Account.class)).isEqualTo(account);
    }

}