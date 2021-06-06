package moneyTransfer.dao;

import moneyTransfer.model.Account;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(AccountDaoImpl.class)
public interface AccountDao {

    @SqlUpdate("CREATE TABLE account (accountNumber INT NOT NULL," +
            " currencyCode varchar(300)," +
            " balance decimal(20,2)," +
            " PRIMARY KEY (accountNumber))")
    void createTable();

    @SqlQuery("SELECT * FROM account WHERE accountNumber = :accountNumber")
    Account getAccount(@Bind("accountNumber") int accountNumber);

    @SqlQuery("SELECT * FROM account")
    List<Account> getAllAccounts();

    @SqlQuery("SELECT * FROM account WHERE accountNumber = :accountNumber FOR UPDATE")
    Account getAccountForUpdate(@Bind("accountNumber") Integer accountNumber);

    @SqlQuery("SELECT * FROM account WHERE accountNumber = :accountNumber AND currencyCode = :currencyCode")
    Account getUserAccount(@Bind("accountNumber") Integer accountNumber, @Bind("currencyCode") String currencyCode);

    @SqlUpdate("INSERT INTO account (accountNumber, currencyCode, balance) " +
            "values (:a.accountNumber, :a.currencyCode, :a.balance)")
    @GetGeneratedKeys
    @Transaction
    int createAccount(@BindBean("a") Account account);

    @SqlUpdate("UPDATE account SET balance = :balance WHERE accountNumber = :accountNumber")
    int updateBalance(@Bind("accountNumber")Integer accountNumber, @Bind("balance") double balance);

}
