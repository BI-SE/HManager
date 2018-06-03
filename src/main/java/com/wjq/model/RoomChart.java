package com.wjq.model;

import java.math.BigDecimal;

/**
 * Created by deior on 2018/6/3.
 */
public class RoomChart {

    private BigDecimal amount;

    private String date;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
