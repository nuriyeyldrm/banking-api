package moneyTransfer.main;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import moneyTransfer.dao.AccountDao;
import moneyTransfer.dao.DbConfig;
import moneyTransfer.dao.IsRefDao;
import moneyTransfer.dao.TransferDao;
import moneyTransfer.model.Account;
import moneyTransfer.model.Transfer;
import moneyTransfer.resources.AccountResource;
import moneyTransfer.resources.TransferResource;
import org.skife.jdbi.v2.DBI;

public class BankApp extends Application<DbConfig> {

    public static void main(String[] args) throws Exception {
        new BankApp().run(args);
    }

    @Override
    public void run(DbConfig dbConfig, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, dbConfig.getDataSourceFactory(), "h2");
        final TransferDao transferDao = jdbi.onDemand(TransferDao.class);
        final AccountDao accountDao = jdbi.onDemand(AccountDao.class);
        final IsRefDao isRefDao = jdbi.onDemand(IsRefDao.class);

        transferDao.createTable();
        accountDao.createTable();
        isRefDao.createTable();

        accountDao.createAccount(new Account(123,"TRY",100.00));
        accountDao.createAccount(new Account(156,"TRY",23.45));
        transferDao.createTransfer(new Transfer(156, 123,
                10.00, "TRY"));
        transferDao.createTransfer(new Transfer(123, 156,
                10.00, "TRY"));

        AccountResource accountResource = new AccountResource(accountDao, isRefDao);
        TransferResource transferResource = new TransferResource(accountDao, transferDao, isRefDao);

        environment.jersey().register(accountResource);
        environment.jersey().register(transferResource);
    }
}
