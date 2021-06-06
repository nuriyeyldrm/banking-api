package moneyTransfer.resources;

import com.codahale.metrics.annotation.Timed;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.dao.TransferDao;
import moneyTransfer.enums.Http;
import moneyTransfer.model.Account;
import moneyTransfer.model.IsRef;
import moneyTransfer.model.Transfer;
import org.skife.jdbi.v2.sqlobject.Transaction;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
public class TransferResource {

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final IsRefDao isRefDao;

    public TransferResource(AccountDao accountDao, TransferDao transferDao, IsRefDao isRefDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.isRefDao = isRefDao;
    }

    @GET
    @Timed
    public List<Transfer> getAllTransfers() {
        return transferDao.getAllTransfers();
    }

    @POST
    @Timed
    @Transaction
    public IsRef createTransfer(@Valid Transfer transfer) {
        final Account senderAccount = accountDao.getAccountForUpdate(transfer.getSenderAccountNumber());
        final Account receiverAccount = accountDao.getAccountForUpdate(transfer.getReceiverAccountNumber());
        final int refNum = isRefDao.createIsRef(new IsRef(false));


        if (senderAccount == null) {
            throw new WebApplicationException("sender account not found", Http.OK.getCode());
        }

        if (receiverAccount == null) {
            throw new WebApplicationException("receiver account not found", Http.OK.getCode());
        }

        if (!transfer.getCurrencyCode().equals(senderAccount.getCurrencyCode())){
            throw new WebApplicationException("currencies do not match", Http.OK.getCode());
        }

        if (!senderAccount.getCurrencyCode().equals(receiverAccount.getCurrencyCode())){
            throw new WebApplicationException("account currencies do not match", Http.OK.getCode());
        }

        if (transfer.getAmount() > senderAccount.getBalance()){
            throw new WebApplicationException("not enough funds available for transfer", Http.OK.getCode());
        }

        final BigDecimal newSenderAmount = BigDecimal.valueOf(senderAccount.getBalance())
                .setScale(2, RoundingMode.FLOOR).subtract(BigDecimal.valueOf(transfer.getAmount())
                        .setScale(2, RoundingMode.FLOOR));

        final BigDecimal newReceiverAmount = BigDecimal.valueOf(receiverAccount.getBalance())
                .setScale(2, RoundingMode.FLOOR).add(BigDecimal.valueOf(transfer.getAmount())
                        .setScale(2, RoundingMode.FLOOR));

        accountDao.updateBalance(transfer.getSenderAccountNumber(), newSenderAmount.doubleValue());
        accountDao.updateBalance(transfer.getReceiverAccountNumber(), newReceiverAmount.doubleValue());

        transferDao.createTransfer(transfer);
        return isRefDao.getIsRef(refNum);
    }

}
