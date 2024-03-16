package org.jodt5.invoices.services;

import org.jodt5.invoices.models.Invoice;
import org.jodt5.invoices.models.Payment;
import org.jodt5.invoices.models.Vendor;

import java.sql.SQLException;
import java.util.List;

public interface IService {
    /* --- Vendors --- */
    List<Vendor> getAllVendors() throws SQLException;
    Vendor getVendorById(Long id) throws SQLException;
    void saveVendor(Vendor vendor) throws SQLException;
    void deleteVendor(Long id) throws SQLException;

    /* --- Payments --- */
    List<Payment> getAllPayments() throws SQLException;
    Payment getPaymentById(Long id) throws SQLException;
    void savePayment(Payment payment) throws SQLException;
    void deletePayment(Long id) throws SQLException;

    /* --- Invoices --- */
    List<Invoice> getAllInvoices() throws SQLException;
    Invoice getInvoiceById(Long id) throws SQLException;
    void saveInvoice(Invoice invoice) throws SQLException;
    void deleteInvoice(Long id) throws SQLException;
}
