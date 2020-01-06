package com.redecommunity.proxy.authentication.dao;

import com.google.common.collect.Sets;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.proxy.authentication.data.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class PasswordDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_users_password_history";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`user_id` INTEGER NOT NULL," +
                                "`password` VARCHAR(255) NOT NULL," +
                                "`time` LONG NOT NULL" +
                                ");",
                        this.getTableName()
                )
        );
    }

    public <T extends Password, U extends User> void insert(T object, U user) {
        String query = String.format(
                "INSERT INTO %s " +
                        "(" +
                        "`user_id`," +
                        "`password`," +
                        "`time`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "%d," +
                        "%s," +
                        "%d" +
                        ")",
                this.getTableName(),
                user.getId(),
                object.getPassword(),
                object.getTime()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <K, V extends Integer, T> Set<T> findAll(K key, V value) {
        String query = String.format(
                "SELECT * FROM %s WHERE `%s`=%d",
                this.getTableName(),
                key,
                value
        );

        Set<T> passwords = Sets.newConcurrentHashSet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Password password = Password.toPassword(resultSet);

                passwords.add((T) password);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return passwords;
    }
}
