package com.phyllis.transferhistory_analysis.beans;

import java.util.Date;

public class UserChurnAcc {
    private Long valueDate;
    private Long currentNumber;
    private Long churnNumber;

    public UserChurnAcc() {
    }

    public UserChurnAcc(Long valueDate, Long currentNumber, Long churnNumber) {
        this.valueDate = valueDate;
        this.currentNumber = currentNumber;
        this.churnNumber = churnNumber;
    }

    public Long getValueDate() {
        return valueDate;
    }

    public void setValueDate(Long valueDate) {
        this.valueDate = valueDate;
    }

    public Long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public Long getChurnNumber() {
        return churnNumber;
    }

    public void setChurnNumber(Long churnNumber) {
        this.churnNumber = churnNumber;
    }

    @Override
    public String toString() {
        return "UserChurn{" +
                "valueDate=" + valueDate +
                ", currentNumber=" + currentNumber +
                ", churnNumber=" + churnNumber +
                '}';
    }
}
