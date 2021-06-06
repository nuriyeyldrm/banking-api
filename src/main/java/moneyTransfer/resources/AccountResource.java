package moneyTransfer.resources;

import com.codahale.metrics.annotation.Timed;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.enums.Http;
import moneyTransfer.model.Account;
import moneyTransfer.model.IsRef;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @GET
    @Timed
    @Path("/{accountNumber}")
    public Account getAccount(@PathParam("accountNumber") int accountNumber){
        return accountDao.getAccount(accountNumber);
    }

    @POST
    @Timed
    public IsRef createAccount(@Valid Account account) {
        final int refNum = isRefDao.createIsRef(new IsRef(false));
        if (accountDao.getUserAccount(account.getAccountNumber(), account.getCurrencyCode()) != null){
            throw new WebApplicationException("account with same currency already exists for user", Http.OK.getCode());
        }

        accountDao.createAccount(account);

        return isRefDao.getIsRef(refNum);
    }

    @PUT
    @Timed
    @Path("/{accountNumber}")
    public IsRef updateAccount(@PathParam("accountNumber") @NotNull int accountNumber, @Valid Account account){
        final int refNum = isRefDao.createIsRef(new IsRef(false));
        if(accountNumber != account.getAccountNumber()){
            throw new WebApplicationException("accountNumber mismatch", Response.Status.BAD_REQUEST);
        }

        final Account account1 = accountDao.getAccount(accountNumber);

        if (account1 == null){
            throw new WebApplicationException("account not found", Http.OK.getCode());
        }

        accountDao.updateAccount(account);
        return isRefDao.getIsRef(refNum);
    }

    @POST
    @Timed
    @Path("/test")
    // returns real account with all value
    public Account createAccountForTest(@Valid Account account) {
        if (accountDao.getUserAccount(account.getAccountNumber(), account.getCurrencyCode()) != null){
            throw new WebApplicationException("account with same currency already exists for user", Http.OK.getCode());
        }

        final int accountNumber = accountDao.createAccount(account);

        return accountDao.getAccount(accountNumber);
    }

    @DELETE
    @Timed
    @Path("/{accountNumber}")
    public IsRef deleteAccount(@PathParam("accountNumber") int accountNumber){
        accountDao.deleteAccount(accountNumber);
        final int refNum = isRefDao.createIsRef(new IsRef(false));

        return isRefDao.getIsRef(refNum);
    }

}
