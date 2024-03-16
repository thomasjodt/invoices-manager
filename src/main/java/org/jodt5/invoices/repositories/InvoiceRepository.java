package org.jodt5.invoices.repositories;

import org.jodt5.invoices.models.Invoice;
import org.jodt5.invoices.models.Payment;
import org.jodt5.invoices.models.Vendor;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class InvoiceRepository implements IRepository<Invoice> {
    Connection conn;

    public InvoiceRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Invoice> getAll() throws SQLException {
        List<Invoice> invoices;
        try (
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT " +
                        "i.*, " +
                        "v.name AS vendor, " +
                        "p.amount AS payed_amount, " +
                        "p.payment_date, " +
                        "p.id AS payment_id " +
                    "FROM " +
                        "invoices AS i " +
                        "INNER JOIN vendors AS v ON (i.vendor_id = v.id) " +
                        "LEFT JOIN payments AS p ON (i.id = p.invoice_id) "
            )
            ) {
            Map<Long, Invoice> invoicesMap = new HashMap<>();

            while (rs.next()) {
                extracted(rs, invoicesMap);
            }

            invoices = new ArrayList<>(invoicesMap.values());
        }
        return invoices;
    }

    @Override
    public Invoice getById(Long id) throws SQLException {
        Invoice invoice = null;

        try (PreparedStatement stmt = conn.prepareStatement(
            "SELECT " +
                    "i.*, " +
                    "v.name AS vendor, " +
                    "p.amount AS payed_amount, " +
                    "p.payment_date, " +
                    "p.id AS payment_id " +
                "FROM " +
                    "invoices AS i " +
                    "INNER JOIN vendors AS v ON (i.vendor_id = v.id) " +
                    "LEFT JOIN payments AS p ON (i.id = p.invoice_id) " +
                "WHERE i.id=?;"
        )) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                Map<Long, Invoice> invoicesMap = new HashMap<>();

                while (rs.next()) {
                    extracted(rs, invoicesMap);

                    for (Invoice invoice1 : invoicesMap.values()) {
                        invoice = invoice1;
                    }
                }
            }
        }

        return invoice;
    }

    @Override
    public void save(Invoice invoice) throws SQLException {
        String sql = getSQL(invoice);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invoice.getInvoiceNumber());
            stmt.setDate(2, Date.valueOf(invoice.getEmissionDate()));
            stmt.setDate(3, Date.valueOf(invoice.getDueDate()));
            stmt.setDouble(4, invoice.getAmount());
            stmt.setLong(5, invoice.getVendor().getId());

            if (invoice.getId() != null && invoice.getId() > 0) {
                stmt.setLong(6, invoice.getId());
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM invoices WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static void extracted(ResultSet rs, Map<Long, Invoice> invoicesMap) throws SQLException {
        Invoice invoiceTemp = new Invoice();
        invoiceTemp.setId(rs.getLong("id"));
        invoiceTemp.setInvoiceNumber(rs.getString("invoice_number"));
        invoiceTemp.setEmissionDate(rs.getDate("emission_date").toLocalDate());
        invoiceTemp.setDueDate(rs.getDate("due_date").toLocalDate());
        invoiceTemp.setAmount(rs.getDouble("amount"));

        Vendor vendor = new Vendor();
        vendor.setId(rs.getLong("vendor_id"));
        vendor.setName(rs.getString("vendor"));
        invoiceTemp.setVendor(vendor);

        long paymentId = rs.getLong("payment_id");

        if (paymentId > 0) {
            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setAmount(rs.getDouble("payed_amount"));
            payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());

            if (invoicesMap.containsKey(invoiceTemp.getId())) {
                invoicesMap.get(invoiceTemp.getId()).getPaymentsOnAccount().add(payment);
            } else {
                invoiceTemp.getPaymentsOnAccount().add(payment);
                invoicesMap.put(invoiceTemp.getId(), invoiceTemp);
            }
        }
    }

    private static String getSQL(Invoice invoice) {
        String sql;

        if (invoice.getId() != null && invoice.getId() > 0) {
            sql = "UPDATE invoices " +
                    "SET invoice_number=?, " +
                    "emission_date=?, " +
                    "due_date=?, " +
                    "amount=?, " +
                    "vendor_id=? " +
                "WHERE id=?";
        } else {
            sql = "INSERT INTO invoices " +
                "(invoice_number, emission_date, due_date, amount, vendor_id) " +
                "VALUES (?,?,?,?,?)";
        }
        return sql;
    }
}
