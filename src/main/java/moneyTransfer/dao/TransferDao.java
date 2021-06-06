package moneyTransfer.dao;

import moneyTransfer.model.Transfer;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(TransferDaoImpl.class)
public interface TransferDao {

    @SqlUpdate("CREATE TABLE transfer (senderAccountNumber INT," +
            " receiverAccountNumber INT," +
            " amount decimal(20,2)," +
            " currencyCode varchar(300))")
    void createTable();

    @SqlQuery("SELECT * from transfer")
    List<Transfer> getAllTransfers();

    @SqlUpdate("INSERT INTO transfer (senderAccountNumber, receiverAccountNumber, amount, currencyCode) " +
            "values (:t.senderAccountNumber, :t.receiverAccountNumber, :t.amount, :t.currencyCode)")
    @GetGeneratedKeys
    @Transaction
    int createTransfer(@BindBean("t") Transfer transfer);

}
