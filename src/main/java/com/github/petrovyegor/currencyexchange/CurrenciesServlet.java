package com.github.petrovyegor.currencyexchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Currency> currencies = getAllCurrencies();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Преобразуем список валют в JSON и пишем в ответ
            objectMapper.writeValue(response.getWriter(), currencies);

        } catch (ClassNotFoundException e) {
            // Обработка ошибок
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database driver not found\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }

    }

//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {            PrintWriter printWriter = response.getWriter();
//            printWriter.println("<html>");
//            printWriter.println("<h1>test</h1>");
//            printWriter.println("</html>");
//    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private List<Currency> getAllCurrencies() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection co = DriverManager.getConnection("jdbc:sqlite:C:\\roadmap\\CurrencyExchange\\CurrencyExchange.sqlite");
        try (co; Statement statement = co.createStatement();) {
            String query = "SELECT Id, Code, FullName, Sign FROM Currencies;";
            ResultSet rs = statement.executeQuery(query);

            List<Currency> currencies = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt(1);//можно указать номер столбца, либо полное название
                String code = rs.getString(2);
                String fullName = rs.getString(3);
                String sign = rs.getString(4);
                currencies.add(new Currency(id, code, fullName, sign));
            }
            rs.close();//закрываем резалт сэт
            return currencies;
        }
    }
}
