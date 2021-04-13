package moneyTransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.dropwizard.validation.OneOf;
import io.dropwizard.validation.ValidationMethod;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class Transfer {

    private boolean isError;

    private int referenceNumber;

    @NotNull(message = "Sender Account Number must not be null")
    private Integer senderAccountNumber;

    @NotNull(message = "Receiver Account Number must not be null")
    private Integer receiverAccountNumber;

    @NotNull(message = "Amount must not be null")
    @DecimalMin("0.01")
    private Double amount;

    @NotNull(message = "Currency Code must not be null")
    @OneOf({"TRY","USD","EUR"})
    private String currencyCode;

    public Transfer() {}

    public Transfer(Integer senderAccountNumber, Integer receiverAccountNumber, Double amount, String currencyCode) {
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public Transfer(boolean isError, int referenceNumber, Integer senderAccountNumber, Integer receiverAccountNumber,
                    Double amount, String currencyCode) {
        this.isError = isError;
        this.referenceNumber = referenceNumber;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.currencyCode = currencyCode;
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
    public Integer getSenderAccountNumber() {
        return senderAccountNumber;
    }

    @JsonProperty
    public Integer getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    @JsonProperty
    public Double getAmount() {
        return amount;
    }

    @JsonProperty
    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer that = (Transfer) o;

        return Objects.equal(this.isError, that.isError) &&
                Objects.equal(this.referenceNumber, that.referenceNumber) &&
                Objects.equal(this.senderAccountNumber, that.senderAccountNumber) &&
                Objects.equal(this.receiverAccountNumber, that.receiverAccountNumber) &&
                Objects.equal(this.amount, that.amount) &&
                Objects.equal(this.currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isError, referenceNumber, senderAccountNumber, receiverAccountNumber, amount,
                currencyCode);
    }

    @JsonIgnore
    @ValidationMethod(message = "sender account cannot be the same as receiver account")
    public boolean isSenderNotEqualToReceiver() {
        return !senderAccountNumber.equals(receiverAccountNumber);
    }
}