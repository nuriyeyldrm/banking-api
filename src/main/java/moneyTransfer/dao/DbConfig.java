package moneyTransfer.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DbConfig extends io.dropwizard.Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private final DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

}
