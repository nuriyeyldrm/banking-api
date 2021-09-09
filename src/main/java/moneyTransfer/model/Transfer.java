package moneyTransfer.model;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import io.dropwizard.validation.*;

import javax.validation.constraints.*;

public class Transfer {

    @NotNull(message = " must not be null")
    private Integer senderAccountNumber;

    @NotNull(message = " must not be null")
    private Integer receiverAccountNumber;

    @NotNull(message = " must not be null")
    @DecimalMin("0.01")
    private Double amount;

    @NotNull(message = " must not be null")
    @OneOf({"TRY","USD","EUR"})
    private String currencyCode;

    public Transfer() {}

    public Transfer(Integer senderAccountNumber, Integer receiverAccountNumber, Double amount, String currencyCode) {
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.currencyCode = currencyCode;
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

        return Objects.equal(this.senderAccountNumber, that.senderAccountNumber) &&
                Objects.equal(this.receiverAccountNumber, that.receiverAccountNumber) &&
                Objects.equal(this.amount, that.amount) &&
                Objects.equal(this.currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(senderAccountNumber, receiverAccountNumber, amount, currencyCode);
    }

    @JsonIgnore
    @ValidationMethod(message = "sender account cannot be the same as receiver account")
    public boolean isSenderNotEqualToReceiver() {
        return !senderAccountNumber.equals(receiverAccountNumber);
    }
}
