package org.jodt5.invoices.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jodt5.invoices.models.Payment;
import org.jodt5.invoices.models.utilities.RequestError;
import org.jodt5.invoices.services.IService;
import org.jodt5.invoices.services.InvoiceService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/payments/*"})
public class PaymentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Connection conn = (Connection) req.getAttribute("conn");
        IService service = new InvoiceService(conn);

        String pathInfo = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        if (pathInfo == null || pathInfo.equals("/")) {
            try (PrintWriter out = resp.getWriter()) {
                List<Payment> payments = service.getAllPayments();

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                String response = mapper.writeValueAsString(payments);
                out.print(response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String[] parts = pathInfo.split("/");

            if (parts.length > 2) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The path param is not correct!");
            }

            long id;

            try {
                id = Long.parseLong(parts[1]);
            } catch (NumberFormatException e) {
                id = 0L;
            }

            try {
                Optional<Payment> o = service.getPaymentById(id);
                try (PrintWriter out = resp.getWriter()) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());

                    if (o.isPresent()) {
                        String response = mapper.writeValueAsString(o.get());
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.print(response);
                    } else {
                        RequestError error = new RequestError();
                        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        error.setMessage("Payment not found.");
                        error.setDescription("The provided payment id doesn't exist in our records.");
                        String errorResponse = mapper.writeValueAsString(error);

                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print(errorResponse);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Connection conn = (Connection) req.getAttribute("conn");
        IService service = new InvoiceService(conn);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        String[] parts = pathInfo.split("/");

        if (parts.length == 2) {
            long id;

            try {
                id = Long.parseLong(parts[1]);
            } catch (NumberFormatException e) {
                id = 0L;
            }

            if (id > 0) {
                Optional<Payment> o;

                try (PrintWriter out = resp.getWriter()) {
                    o = service.getPaymentById(id);

                    if (o.isPresent()) {
                        service.deletePayment(id);
                        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        ObjectMapper mapper = new ObjectMapper();
                        RequestError error = new RequestError();

                        resp.setContentType("application/json");

                        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        error.setMessage("Payment not found.");
                        error.setDescription("The provided payment id doesn't exist in our records.");
                        String errorResponse = mapper.writeValueAsString(error);

                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print(errorResponse);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: Debes enviar un número como ID.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: no se ha provisto un id correcto.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Connection conn = (Connection) req.getAttribute("conn");
        IService service = new InvoiceService(conn);

        ObjectMapper mapper = new ObjectMapper();
        Payment payment = mapper.readValue(req.getInputStream(), Payment.class);

        if (payment.getId() == null) payment.setId(0L);

        try {
            service.savePayment(payment);

            if (payment.getId() > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
