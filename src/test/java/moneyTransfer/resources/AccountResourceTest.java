package moneyTransfer.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AccountResourceTest {
    private static final AccountDao accountDao = mock(AccountDao.class);
    private static final IsRefDao isRefDao = mock(IsRefDao.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new AccountResource(accountDao, isRefDao)).build();


    private final Account account1 = new Account(123,
            Currency.getInstance("TRY").getCurrencyCode(),
            new BigDecimal("100.00").setScale(2, RoundingMode.UNNECESSARY).doubleValue());

    private final Account account2 = new Account(156,
            Currency.getInstance("TRY").getCurrencyCode(),
            new BigDecimal("23.45").setScale(2, RoundingMode.UNNECESSARY).doubleValue());

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
        reset(accountDao);
    }


    @Test
    public void testGetAllAccounts() {

        final List<Account> accounts = Arrays.asList(account1, account2);
        when(accountDao.getAllAccounts()).thenReturn(accounts);

        List<Account> accountList = new ArrayList<>();

        assertThat(resources.client().target("/accounts").request().get(accountList.getClass()).size())
                .isEqualTo(2);
    }

//    @Test
//    public void testCreateAccount() {
//
//        Account newAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
//                new BigDecimal("100.00").setScale(2, RoundingMode.UNNECESSARY).doubleValue());
//
//        Account savedAccount = new Account(123, Currency.getInstance("TRY").getCurrencyCode(),
//                new BigDecimal("100.00").setScale(2, RoundingMode.UNNECESSARY).doubleValue());
//
//        when(accountDao.createAccount(newAccount)).thenReturn(123);
//        when(accountDao.getAccount(123)).thenReturn(savedAccount);
//
//        assertThat(resources.client().target("/accounts/test").request().post(Entity.json(newAccount))
//                .readEntity(Account.class)).isEqualTo(savedAccount);
//
//        verify(accountDao).createAccount(newAccount);
//    }
}