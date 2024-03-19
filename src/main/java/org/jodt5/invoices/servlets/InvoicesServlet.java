package org.jodt5.invoices.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jodt5.invoices.models.Invoice;
import org.jodt5.invoices.models.utilities.RequestError;
import org.jodt5.invoices.services.IService;
import org.jodt5.invoices.services.InvoiceService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/invoices/*"})
public class InvoicesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

            try (PrintWriter out = resp.getWriter()) {
                Optional<Invoice> o = service.getInvoiceById(id);

                if (o.isPresent()) {
                    resp.setContentType("application/json");
                    resp.setStatus(HttpServletResponse.SC_OK);

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    String response = mapper.writeValueAsString(o.get());

                    out.print(response);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No se ha encontrado la factura que buscas.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (parts.length < 2) {

            try (PrintWriter out = resp.getWriter()) {
                List<Invoice> invoices = service.getAllInvoices();

                resp.setContentType("application/json");
                resp.setHeader("Access-Control-Allow-Origin", "*");
                resp.setStatus(HttpServletResponse.SC_OK);

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String response = mapper.writeValueAsString(invoices);

                out.print(response);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debes proporcionar solamente un ID.");
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
                Optional<Invoice> o;

                try (PrintWriter out = resp.getWriter()) {
                    o = service.getInvoiceById(id);

                    if (o.isPresent()) {
                        service.deleteInvoice(id);
                        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        ObjectMapper mapper = new ObjectMapper();
                        RequestError error = new RequestError();

                        resp.setContentType("application/json");

                        error.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        error.setMessage("Invoice not found.");
                        error.setDescription("The provided invoice id doesn't exist in our records.");
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
}
