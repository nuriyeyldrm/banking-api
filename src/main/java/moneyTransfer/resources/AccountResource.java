package moneyTransfer.resources;

import com.codahale.metrics.annotation.Timed;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.enums.HttpStatus;
import moneyTransfer.model.Account;
import moneyTransfer.model.IsRef;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
            throw new WebApplicationException("account with same currency already exists for user",
                    HttpStatus.CONFLICT.getCode());
        }

        accountDao.createAccount(account);

        return isRefDao.getIsRef(refNum);
    }

    @PUT
    @Timed
    @Path("/{accountNumber}")
    public IsRef updateAccount(@PathParam("accountNumber") @NotNull int accountNumber, @Valid Account account){
        final int refNum = isRefDao.createIsRef(new IsRef(false));

        final Account account1 = accountDao.getAccount(accountNumber);

        if (account1 == null){
            throw new WebApplicationException("account not found", HttpStatus.NOT_FOUND.getCode());
        }

        accountDao.updateAccount(account);
        return isRefDao.getIsRef(refNum);
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
