package org.jodt5.invoices.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private Long id;
    private Vendor vendor;
    private String invoiceNumber;
    private LocalDate emissionDate;
    private LocalDate dueDate;
    private Double amount;
    private List<Payment> paymentsOnAccount;

    public Invoice() {
        this.paymentsOnAccount = new ArrayList<>();
    }

    public Invoice(Long id, Vendor vendor, String invoiceNumber, LocalDate emissionDate, LocalDate dueDate, Double amount, List<Payment> paymentsOnAccount) {
        this.id = id;
        this.vendor = vendor;
        this.invoiceNumber = invoiceNumber;
        this.emissionDate = emissionDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paymentsOnAccount = paymentsOnAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(LocalDate emissionDate) {
        this.emissionDate = emissionDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Payment> getPaymentsOnAccount() {
        return paymentsOnAccount;
    }

    public void setPaymentsOnAccount(List<Payment> paymentsOnAccount) {
        this.paymentsOnAccount = paymentsOnAccount;
    }
}
