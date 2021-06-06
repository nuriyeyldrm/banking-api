package moneyTransfer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.dropwizard.validation.OneOf;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Account {

    @Positive(message = "Account Number must be positive")
    @NotNull(message = "Account Number must not be null")
    private Integer accountNumber;

    @NotNull(message = "Currency Code must not be null")
    @OneOf({"TRY","USD","EUR"})
    private String currencyCode;

    @NotNull(message = "Balance must not be null")
    private Double balance;

    @JsonCreator
    public Account() {}

    public Account(Integer accountNumber, String currencyCode, Double balance) {
        this.accountNumber = accountNumber;
        this.currencyCode = currencyCode;
        this.balance = balance;
    }

    @JsonProperty
    public Integer getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty
    public Double getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account that = (Account) o;

        return Objects.equal(this.accountNumber, that.accountNumber) &&
                Objects.equal(this.currencyCode, that.currencyCode) &&
                Objects.equal(this.balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber, currencyCode, balance);
    }
}
