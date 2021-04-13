package moneyTransfer.dao;

import moneyTransfer.model.Account;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDaoImpl implements ResultSetMapper<Account>{
    @Override
    public Account map(int n, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Account(resultSet.getBoolean("isError"),
                resultSet.getInt("referenceNumber"),
                resultSet.getInt("accountNumber"),
                resultSet.getString("currencyCode"),
                resultSet.getDouble("balance"));
    }
}
