package org.jodt5.invoices.models;

import java.time.LocalDate;

public class Payment {
    private Long id;
    private Double amount;
    private LocalDate paymentDate;

    public Payment() {
    }

    public Payment(Long id, Double amount, LocalDate paymentDate) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
