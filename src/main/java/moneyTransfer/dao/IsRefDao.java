package moneyTransfer.dao;

import moneyTransfer.model.IsRef;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(IsRefDaoImpl.class)
public interface IsRefDao {

    @SqlUpdate("CREATE TABLE isRef (referenceNumber INT AUTO_INCREMENT(500,1) NOT NULL," +
            " isError BOOLEAN," +
            " PRIMARY KEY (referenceNumber))")
    void createTable();

    @SqlUpdate("INSERT INTO isRef (isError) " +
            "values (:ir.isError)")
    @GetGeneratedKeys
    @Transaction
    int createIsRef(@BindBean("ir") IsRef isRef);
}
