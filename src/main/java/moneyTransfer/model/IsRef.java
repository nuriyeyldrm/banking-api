package moneyTransfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class IsRef {

    private int referenceNumber;

    private final Boolean isError;

    public IsRef(Boolean isError){
        this.isError = isError;
    }

    public IsRef(boolean isError, int referenceNumber) {
        this.isError = isError;
        this.referenceNumber = referenceNumber;
    }

    @JsonProperty
    public boolean getIsError() {
        return isError;
    }

    @JsonProperty
    public int getReferenceNumber() {
        return referenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IsRef that = (IsRef) o;

        return Objects.equal(this.isError, that.isError) &&
                Objects.equal(this.referenceNumber, that.referenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isError, referenceNumber);
    }
}
