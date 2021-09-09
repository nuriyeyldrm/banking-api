package moneyTransfer.dao.mapper;

import moneyTransfer.model.IsRef;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IsRefDaoImpl implements ResultSetMapper<IsRef> {

    @Override
    public IsRef map(int n, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new IsRef(resultSet.getBoolean("isError"),
                resultSet.getInt("referenceNumber"));
    }
}
