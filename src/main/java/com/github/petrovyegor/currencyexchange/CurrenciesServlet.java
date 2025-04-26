package com.github.petrovyegor.currencyexchange;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class CurrenciesServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            String message = connectAndGetAll();
//            PrintWriter printWriter = response.getWriter();
//            printWriter.println("<html>");
//            printWriter.println("<h1>" + message + "</h1>");
//            printWriter.println("</html>");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {            PrintWriter printWriter = response.getWriter();
            printWriter.println("<html>");
            printWriter.println("<h1>test</h1>");
            printWriter.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String connectAndGetAll() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection co = DriverManager.getConnection("jdbc:sqlite:CurrencyExchange.sqlite");
        Statement statement = co.createStatement();
        String query = "SELECT Id, Code, FullName, Sign\n" +
                "FROM Currencies;";
        ResultSet rs = statement.executeQuery(query);

        StringBuilder result = new StringBuilder();

        while (rs.next()) {
            int id = rs.getInt(1);//можно указать номер столбца, либо полное название
            String code = rs.getString(2);
            String fullName = rs.getString(3);
            String sign = rs.getString(4);
            result.append(id + "\t" + code + "\t" + fullName + "\t" + sign).append("\r\n");
            //System.out.println(id + "\t" + code + "\t" + fullName + "\t" + sign);
        }
        rs.close();//закрываем резалт сэт
        statement.close();//закрываем стейтмент
        return result.toString();
    }
}
