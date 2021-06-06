package moneyTransfer.resources;

import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.junit.ResourceTestRule;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.dao.TransferDao;
import moneyTransfer.enums.Http;
import moneyTransfer.model.Account;
import moneyTransfer.model.Transfer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TransferResourceTest {
    private static final AccountDao accountDao = mock(AccountDao.class);
    private static final TransferDao transferDao = mock(TransferDao.class);
    private static final IsRefDao isRefDao = mock(IsRefDao.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TransferResource(accountDao, transferDao, isRefDao)).build();

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
        reset(transferDao);
        reset(accountDao);
    }

    @Test
    public void testTransfer() {

        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 156;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        Account senderAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        Account receiverAccount = new Account(156, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("150.00").setScale(2, RoundingMode.FLOOR).doubleValue());


        final BigDecimal newSenderAmount = BigDecimal.valueOf(senderAccount.getBalance())
                .setScale(2, RoundingMode.FLOOR).subtract(BigDecimal.valueOf(transfer.getAmount())
                        .setScale(2, RoundingMode.FLOOR));

        final BigDecimal newReceiverAmount = BigDecimal.valueOf(receiverAccount.getBalance())
                .setScale(2, RoundingMode.FLOOR).add(BigDecimal.valueOf(transfer.getAmount())
                        .setScale(2, RoundingMode.FLOOR));

        when(accountDao.getAccountForUpdate(senderAccountNumber)).thenReturn(senderAccount);
        when(accountDao.getAccountForUpdate(receiverAccountNumber)).thenReturn(receiverAccount);

        when(accountDao.updateBalance(senderAccountNumber, newSenderAmount.doubleValue())).thenReturn(senderAccountNumber);
        when(accountDao.updateBalance(receiverAccountNumber, newReceiverAmount.doubleValue())).thenReturn(receiverAccountNumber);

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(204);

        verify(accountDao).updateBalance(senderAccountNumber, newSenderAmount.doubleValue());
        verify(accountDao).updateBalance(receiverAccountNumber, newReceiverAmount.doubleValue());
    }

    @Test
    public void testTransferAmountGreaterThanSenderAmount() {
        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 156;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("1000.00").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        Account senderAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        Account receiverAccount = new Account(2, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("150.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        when(accountDao.getAccountForUpdate(senderAccountNumber)).thenReturn(senderAccount);
        when(accountDao.getAccountForUpdate(receiverAccountNumber)).thenReturn(receiverAccount);

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(Http.OK.getCode());
    }

    @Test
    public void testTransferCurrencyAndSenderAccountCurrencyDoNotMatch() {
        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 156;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("USD").getCurrencyCode());

        Account senderAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        Account receiverAccount = new Account(2, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("150.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        when(accountDao.getAccountForUpdate(senderAccountNumber)).thenReturn(senderAccount);
        when(accountDao.getAccountForUpdate(receiverAccountNumber)).thenReturn(receiverAccount);

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(Http.OK.getCode());
    }

    @Test
    public void testTransferReceiverAccountCurrencyAndSenderAccountCurrencyDoNotMatch() {
        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 156;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        Account senderAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        Account receiverAccount = new Account(2, Currency.getInstance("USD").getCurrencyCode(),
                new BigDecimal("150.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        when(accountDao.getAccountForUpdate(senderAccountNumber)).thenReturn(senderAccount);
        when(accountDao.getAccountForUpdate(receiverAccountNumber)).thenReturn(receiverAccount);

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(Http.OK.getCode());
    }

    @Test
    public void testTransferSameSenderAccountAndReceiverAccount() {
        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 123;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(422);

        ValidationErrorMessage msg = post.readEntity(ValidationErrorMessage.class);
        assertThat(msg.getErrors()).containsOnly("sender account cannot be the same as receiver account");
    }

    @Test
    public void testTransferSenderAccountDoesNotExist() {
        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 156;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        Account receiverAccount = new Account(2, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("150.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        when(accountDao.getAccountForUpdate(senderAccountNumber)).thenReturn(null);
        when(accountDao.getAccountForUpdate(receiverAccountNumber)).thenReturn(receiverAccount);

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(Http.OK.getCode());
    }

    @Test
    public void testTransferReceiverAccountDoesNotExist() {
        Integer senderAccountNumber = 123;
        Integer receiverAccountNumber = 156;
        Transfer transfer = new Transfer(senderAccountNumber, receiverAccountNumber,
                new BigDecimal("23.45").setScale(2, RoundingMode.FLOOR).doubleValue(),
                Currency.getInstance("TRY").getCurrencyCode());

        Account senderAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
                new BigDecimal("100.00").setScale(2, RoundingMode.FLOOR).doubleValue());

        when(accountDao.getAccountForUpdate(senderAccountNumber)).thenReturn(senderAccount);
        when(accountDao.getAccountForUpdate(receiverAccountNumber)).thenReturn(null);

        final Response post = resources.client().target("/transfers").request().post(Entity.json(transfer));
        assertThat(post.getStatus()).isEqualTo(Http.OK.getCode());
    }
}