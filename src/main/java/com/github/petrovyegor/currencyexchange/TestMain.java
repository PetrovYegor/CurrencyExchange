package com.github.petrovyegor.currencyexchange;

import java.sql.*;
import java.util.Scanner;

public class TestMain {
    Connection co;

    public static void main(String[] args) throws SQLException {

        TestMain program = new TestMain();
        program.open();
        //program.insert();
        program.select();
        program.close();
    }

    void open() {
        try {
            Class.forName("org.sqlite.JDBC");
            //co = DriverManager.getConnection("jdbc:sqlite:users.db");
            co = DriverManager.getConnection("jdbc:sqlite:CurrencyExchange.sqlite");
            System.out.println("Connected");
        } catch (Exception e) {
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
        }
    }

    void insert() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter code: ");
        String code = scanner.nextLine();
        System.out.print("Enter fullName: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter sign: ");
        String sign = scanner.nextLine();

        String query = "INSERT INTO Currencies (Code, FullName, Sign) VALUES ('" + code + "', '" + fullName + "', '" + sign + "');";    //формируем запрос для БД

        //Для выполнения запроса в БД нужен объект Statement. Он используется для любого скл запроса и он используется не просто так, а из объекта нашего подключения

        Statement statement = co.createStatement();

        //Дальше нам необходимо его выполнить

        //запросы бывают на получение данных и на обновление
        statement.executeUpdate(query);//сюда передаём запрос, который мы выполняем

        System.out.println("Rows added");//уведомляшка, что строки добавлены в БД

    }

    void select() {
        try {
            Statement statement = co.createStatement();
            String query = "SELECT Id, Code, FullName, Sign\n" +
                    "FROM Currencies;";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt(1);//можно указать номер столбца, либо полное название
                String code = rs.getString(2);
                String fullName = rs.getString(3);
                String sign = rs.getString(4);
                System.out.println(id + "\t" + code + "\t" + fullName + "\t" + sign);
            }
            rs.close();//закрываем резалт сэт
            statement.close();//закрываем стейтмент
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void close() {
        try {
            co.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}