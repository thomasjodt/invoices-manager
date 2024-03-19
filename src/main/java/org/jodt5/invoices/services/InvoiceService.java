package org.jodt5.invoices.services;

import org.jodt5.invoices.models.Invoice;
import org.jodt5.invoices.models.Payment;
import org.jodt5.invoices.models.Vendor;
import org.jodt5.invoices.repositories.IRepository;
import org.jodt5.invoices.repositories.InvoiceRepository;
import org.jodt5.invoices.repositories.PaymentRepository;
import org.jodt5.invoices.repositories.VendorRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InvoiceService implements IService {
    IRepository<Invoice> invoiceRepository;
    IRepository<Vendor> vendorRepository;
    IRepository<Payment> paymentRepository;

    public InvoiceService(Connection conn) {
        this.invoiceRepository = new InvoiceRepository(conn);
        this.vendorRepository = new VendorRepository(conn);
        this.paymentRepository = new PaymentRepository(conn);
    }

    @Override
    public List<Vendor> getAllVendors() throws SQLException {
        return vendorRepository.getAll();
    }

    @Override
    public Optional<Vendor> getVendorById(Long id) throws SQLException {
        return Optional.ofNullable(vendorRepository.getById(id));
    }

    @Override
    public void saveVendor(Vendor vendor) throws SQLException {
        vendorRepository.save(vendor);
    }

    @Override
    public void deleteVendor(Long id) throws SQLException {
        vendorRepository.delete(id);
    }

    @Override
    public List<Payment> getAllPayments() throws SQLException {
        return paymentRepository.getAll();
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) throws SQLException {
        return Optional.ofNullable(paymentRepository.getById(id));
    }

    @Override
    public void savePayment(Payment payment) throws SQLException {
        paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) throws SQLException {
        paymentRepository.delete(id);
    }

    @Override
    public List<Invoice> getAllInvoices() throws SQLException {
        return invoiceRepository.getAll();
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) throws SQLException {
        return Optional.ofNullable(invoiceRepository.getById(id));
    }

    @Override
    public void saveInvoice(Invoice invoice) throws SQLException {
        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) throws SQLException {
        invoiceRepository.delete(id);
    }
}
