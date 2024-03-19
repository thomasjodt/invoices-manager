package org.jodt5.invoices.repositories;

import org.jodt5.invoices.models.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements IRepository<Payment> {
    private final Connection conn;

    public PaymentRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Payment> getAll() throws SQLException {
        List<Payment> payments = new ArrayList<>();

        try (
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM payments")
        ){
            while (rs.next()) {
                Payment payment = getPayment(rs);
                payments.add(payment);
            }
        }

        return payments;
    }

    @Override
    public Payment getById(Long id) throws SQLException {
        Payment payment = null;

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM payments WHERE id=?")) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payment = getPayment(rs);
                }
            }
        }
        return payment;
    }

    @Override
    public void save(Payment payment) throws SQLException {
        String sql;

        if (payment.getId() != null && payment.getId() > 0) {
            sql = "UPDATE payments SET amount=?, payment_date=? WHERE id=?";
        } else {
            sql = "INSERT INTO payments (amount, payment_date) VALUES (?,?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, payment.getAmount());
            stmt.setDate(2, Date.valueOf(payment.getPaymentDate()));

            if (payment.getId() != null && payment.getId() > 0) {
                stmt.setLong(3, payment.getId());
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM payments WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static Payment getPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getLong("id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        return payment;
    }
}
