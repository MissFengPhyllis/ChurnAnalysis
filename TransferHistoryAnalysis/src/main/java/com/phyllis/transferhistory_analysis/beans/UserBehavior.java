package com.phyllis.transferhistory_analysis.beans;

import java.util.Date;

public class UserBehavior {
    //定义私有属性
    private Long transferId;
    private Long partyId;
    private Long valueDate;

    public UserBehavior() {
    }

    public UserBehavior(Long transferId, Long partyId,  Long valueDate) {
        this.transferId = transferId;
        this.partyId = partyId;
        this.valueDate = valueDate;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }


    public Long getValueDate() {
        return valueDate;
    }

    public void setValueDate(Long valueDate) {
        this.valueDate = valueDate;
    }

    @Override
    public String toString() {
        return "UserBehavior{" +
                "transferId=" + transferId +
                ", partyId=" + partyId +
                ", valueDate=" + valueDate +
                '}';
    }
}

