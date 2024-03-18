package org.jodt5.invoices.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jodt5.invoices.models.Invoice;
import org.jodt5.invoices.models.Payment;
import org.jodt5.invoices.services.IService;
import org.jodt5.invoices.services.InvoiceService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/invoices")
public class InvoicesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = (Connection) req.getAttribute("conn");
        IService service = new InvoiceService(conn);

        try {
            List<Invoice> invoices = service.getAllInvoices();
            for (Invoice invoice : invoices) {
                System.out.println(
                    invoice.getId() + " | "
                        + invoice.getInvoiceNumber()
                        + " | " + invoice.getAmount()
                        + " | " + invoice.getVendor().getName()
                        + " | " + invoice.getEmissionDate().toString()
                        + " | " + invoice.getDueDate().toString()
                        + " | pagos:"
                );
                for (Payment payment : invoice.getPaymentsOnAccount()) {
                    System.out.println("\t(" + payment.getId() + ") | " + payment.getAmount() + " | " + payment.getPaymentDate().toString());
                }
                System.out.println("-----------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
