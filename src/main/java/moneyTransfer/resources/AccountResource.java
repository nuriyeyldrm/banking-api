package moneyTransfer.resources;

import com.codahale.metrics.annotation.Timed;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.enums.Http;
import moneyTransfer.model.Account;
import moneyTransfer.model.IsRef;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final AccountDao accountDao;
    private final IsRefDao isRefDao;

    public AccountResource(AccountDao accountDao, IsRefDao isRefDao) {
        this.accountDao = accountDao;
        this.isRefDao = isRefDao;
    }

    @GET
    @Timed
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @POST
    @Timed
    public IsRef createAccount(@Valid Account account) {
        final int refNum = isRefDao.createIsRef(new IsRef(true));
        if (accountDao.getUserAccount(account.getAccountNumber(), account.getCurrencyCode()) != null){
            throw new WebApplicationException("account with same currency already exists for user, referenceNumber: "
                    + refNum, Http.OK.getCode());
        }

        final int accountReferenceNumber = accountDao.createAccount(account);

        return accountDao.getIsRef(accountReferenceNumber);
    }

    @POST
    @Timed
    @Path("/test")
    // returns real account with all value
    public Account createAccountForTest(@Valid Account account) {
        final int refNum = isRefDao.createIsRef(new IsRef(true));
        if (accountDao.getUserAccount(account.getAccountNumber(), account.getCurrencyCode()) != null){
            throw new WebApplicationException("account with same currency already exists for user, referenceNumber: "
                    + refNum, Http.OK.getCode());
        }

        final int accountReferenceNumber = accountDao.createAccount(account);

        return accountDao.getAccount(accountReferenceNumber);
    }

}
