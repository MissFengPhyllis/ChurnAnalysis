package com.phyllis.transferhistory_analysis.beans;

public class UserChurnAcc {
    private String churnDates;
    private Long churnNumber;

    public UserChurnAcc() {
    }

    public UserChurnAcc(String churnDates, Long churnNumber) {
        this.churnDates = churnDates;
        this.churnNumber = churnNumber;
    }

    public String getChurnDates() {
        return churnDates;
    }

    public void setChurnDates(String churnDates) {
        this.churnDates = churnDates;
    }

    public Long getChurnNumber() {
        return churnNumber;
    }

    public void setChurnNumber(Long churnNumber) {
        this.churnNumber = churnNumber;
    }

    @Override
    public String toString() {
        return "UserChurnAcc{" +
                "churnDates='" + churnDates + '\'' +
                ", churnNumber=" + churnNumber +
                '}';
    }
}
