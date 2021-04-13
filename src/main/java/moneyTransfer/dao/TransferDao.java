package moneyTransfer.dao;

import moneyTransfer.model.IsRef;
import moneyTransfer.model.Transfer;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper({TransferDaoImpl.class, IsRefDaoImpl.class})
public interface TransferDao {

    @SqlUpdate("CREATE TABLE transfer (referenceNumber INT AUTO_INCREMENT(250,1) NOT NULL," +
            " isError BOOLEAN," +
            " senderAccountNumber INT," +
            " receiverAccountNumber INT," +
            " amount decimal(20,2)," +
            " currencyCode varchar(300)," +
            " PRIMARY KEY (referenceNumber))")
    void createTable();

    @SqlQuery("SELECT * from transfer")
    List<Transfer> getAllTransfers();

    @SqlQuery("SELECT referenceNumber, isError FROM transfer WHERE referenceNumber = :referenceNumber")
    IsRef getIsRef(@Bind("referenceNumber") int referenceNumber);

    @SqlUpdate("INSERT INTO transfer (senderAccountNumber, receiverAccountNumber, amount, currencyCode) " +
            "values (:t.senderAccountNumber, :t.receiverAccountNumber, :t.amount, :t.currencyCode)")
    @GetGeneratedKeys
    @Transaction
    int createTransfer(@BindBean("t") Transfer transfer);

}
