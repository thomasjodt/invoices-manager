package org.jodt5.invoices.repositories;

import org.jodt5.invoices.models.Vendor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorRepository implements IRepository<Vendor> {
    private final Connection conn;

    public VendorRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Vendor> getAll() throws SQLException {
        List<Vendor> vendors = new ArrayList<>();

        try (
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM vendors")
        ) {
            while (rs.next()) {
                Vendor vendor = getVendor(rs);
                vendors.add(vendor);
            }
        }
        return vendors;
    }

    @Override
    public Vendor getById(Long id) throws SQLException {
        Vendor vendor = null;

        try (PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM vendors WHERE id=?"
        )) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vendor = getVendor(rs);
                }
            }
        }

        return vendor;
    }

    @Override
    public void save(Vendor vendor) throws SQLException {
        String sql;

        if (vendor != null && vendor.getId() > 0) {
            sql = "UPDATE vendors SET name=? WHERE id=?";
        } else {
            sql = "INSERT INTO vendors (name) VALUES (?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            assert vendor != null;
            stmt.setString(1, vendor.getName());

            if (vendor.getId() != null && vendor.getId() > 0) {
                stmt.setLong(2,vendor.getId());
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM vendors WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static Vendor getVendor(ResultSet rs) throws SQLException {
        Vendor vendor = new Vendor();
        vendor.setId(rs.getLong("id"));
        vendor.setName(rs.getString("name"));
        return vendor;
    }
}
