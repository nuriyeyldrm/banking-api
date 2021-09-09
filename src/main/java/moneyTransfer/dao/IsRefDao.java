package moneyTransfer.dao;

import moneyTransfer.dao.mapper.IsRefDaoImpl;
import moneyTransfer.model.IsRef;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(IsRefDaoImpl.class)
public interface IsRefDao {

    @SqlUpdate("CREATE TABLE isRef (referenceNumber INT AUTO_INCREMENT(500,1) NOT NULL," +
            " isError BOOLEAN," +
            " PRIMARY KEY (referenceNumber))")
    void createTable();

    @SqlQuery("SELECT referenceNumber, isError FROM isRef WHERE referenceNumber = :referenceNumber")
    IsRef getIsRef(@Bind("referenceNumber") int referenceNumber);

    @SqlUpdate("INSERT INTO isRef (isError) " +
            "values (:ir.isError)")
    @GetGeneratedKeys
    @Transaction
    int createIsRef(@BindBean("ir") IsRef isRef);
}
