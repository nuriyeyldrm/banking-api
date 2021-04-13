package moneyTransfer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.dropwizard.validation.OneOf;

import javax.validation.constraints.NotNull;

public class Account {

    private boolean isError;

    private int referenceNumber;

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

    public Account(boolean isError, int referenceNumber, Integer accountNumber, String currencyCode, Double balance) {
        this.isError = isError;
        this.referenceNumber = referenceNumber;
        this.accountNumber = accountNumber;
        this.currencyCode = currencyCode;
        this.balance = balance;
    }

    @JsonProperty
    public boolean getIsError() {
        return isError;
    }

    @JsonProperty
    public int getReferenceNumber() {
        return referenceNumber;
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

        return Objects.equal(this.isError, that.isError) &&
                Objects.equal(this.referenceNumber, that.referenceNumber) &&
                Objects.equal(this.accountNumber, that.accountNumber) &&
                Objects.equal(this.currencyCode, that.currencyCode) &&
                Objects.equal(this.balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isError, referenceNumber, accountNumber, currencyCode, balance);
    }
}
