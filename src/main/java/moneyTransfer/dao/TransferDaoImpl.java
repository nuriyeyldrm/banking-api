package moneyTransfer.dao;

import moneyTransfer.model.Transfer;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDaoImpl implements ResultSetMapper<Transfer> {
    @Override
    public Transfer map(int n, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Transfer(resultSet.getInt("senderAccountNumber"),
                resultSet.getInt("receiverAccountNumber"),
                resultSet.getDouble("amount"),
                resultSet.getString("currencyCode"));
    }
}
