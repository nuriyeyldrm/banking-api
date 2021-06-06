package moneyTransfer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {
    private static final ObjectMapper mapper = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        Transfer transfer = new Transfer(123, 156,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        final String expected = mapper.writeValueAsString(
                mapper.readValue(fixture("transfer.json"), Transfer.class));

        assertThat(mapper.writeValueAsString(transfer)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        Transfer transfer = new Transfer(123, 156,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").toString());

        assertThat(mapper.readValue(fixture("transfer.json"), Transfer.class))
                .isEqualTo(transfer);
    }
}