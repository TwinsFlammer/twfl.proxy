package com.redefocus.proxy.punish.dao;

import com.google.common.collect.Sets;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.proxy.punish.data.RevokeReason;
import com.redefocus.proxy.punish.manager.RevokeReasonManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class RevokeReasonDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_revoke_reasons";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`name` VARCHAR(255) NOT NULL," +
                                "`display_name` VARCHAR(255) NOT NULL," +
                                "`description` TEXT NOT NULL," +
                                "`group_id` INTEGER NOT NULL" +
                                ");",
                        this.getTableName()
                )
        );
    }

    @Override
    public <T> Set<T> findAll() {
        String query = String.format(
                "SELECT * FROM %s",
                this.getTableName()
        );

        Set<T> reasons = Sets.newConcurrentHashSet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                RevokeReason revokeReason = RevokeReasonManager.toRevokeReason(resultSet);

                reasons.add((T) revokeReason);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return reasons;
    }
}
