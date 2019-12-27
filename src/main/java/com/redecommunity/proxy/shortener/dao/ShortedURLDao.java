package com.redecommunity.proxy.shortener.dao;

import com.google.common.collect.Sets;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.proxy.shortener.data.ShortedURL;
import com.redecommunity.proxy.shortener.manager.ShortedURLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class ShortedURLDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`link` VARCHAR(255) NOT NULL," +
                                "`name` VARCHAR(255) NOT NULL," +
                                "`user_id` INTEGER NOT NULL," +
                                "`time` LONG NOT NULL," +
                                "`active` BOOLEAN NOT NULL" +
                                ");",
                        this.getTableName()
                )
        );
    }

    public <T extends ShortedURL> void insert(T object) {
        String query = String.format(
                "INSERT INTO %s " +
                        "(" +
                        "`link`," +
                        "`user_id`" +
                        "`time`," +
                        "`active`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "%s," +
                        "%d," +
                        "%d," +
                        "%b" +
                        ");",
                this.getTableName(),
                object.getUrl(),
                object.getUserId(),
                object.getTime(),
                object.isActive()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.execute();

            ShortedURL shortedURL = this.findOne("name", object.getName());

            ShortedURLManager.getShortedURLS().add(shortedURL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <K, V, T> T findOne(K key, V value) {
        String query = String.format(
                "SELECT * FROM %s WHERE `%s`='%s'",
                this.getTableName(),
                key,
                value
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()) return (T) ShortedURLManager.toShortedURL(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public <K, V, U, I extends Integer> void update(HashMap<K, V> keys, U key, I value) {
        String where = this.generateWhere(keys);

        String query = String.format(
                "UPDATE %s SET %s WHERE `%s`=%d",
                this.getTableName(),
                where,
                key,
                value
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <T> Set<T> findAll() {
        String query = String.format(
                "SELECT * FROM %s",
                this.getTableName()
        );

        Set<T> shortedURLS = Sets.newConcurrentHashSet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                ShortedURL shortedURL = ShortedURLManager.toShortedURL(resultSet);

                shortedURLS.add((T) shortedURL);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return shortedURLS;
    }
}
