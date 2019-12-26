package com.redecommunity.proxy.connection.listeners.motd.dao;

import com.google.common.collect.Sets;
import com.redecommunity.common.shared.databases.mysql.dao.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class MotdDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_motd";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`first_line` VARCHAR(255) NOT NULL," +
                                "`second_line` VARCHAR(255) NOT NULL," +
                                "`active` BOOLEAN NOT NULL" +
                                ");"
                )
        );
    }

    @Override
    public <T> Set<T> findAll() {
        Set<T> motd = Sets.newConcurrentHashSet();

        String query = String.format(
                "SELECT * FROM %s",
                this.getTableName()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return motd;
    }
}
