package org.jodt5.invoices.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Payment {
    private Long id;
    private Double amount;
    private LocalDate paymentDate;

    public Payment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonFormat(pattern ="yyyy-MM-dd")
    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
