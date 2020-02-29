package com.redecommunity.proxy.punish.dao;

import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.proxy.punish.data.PunishReason;
import com.redecommunity.proxy.punish.manager.PunishReasonManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class PunishReasonDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_punish_reason";
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
                                "`group_id` INTEGER NOT NULL," +
                                "`type` VARCHAR(255) NOT NULL," +
                                "`duration` INTEGER NOT NULL," +
                                "`duration_type` VARCHAR(255) NOT NULL" +
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

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                PunishReason punishReason = PunishReasonManager.toReason(resultSet);

                if (punishReason != null) PunishReasonManager.getPunishReasons().add(punishReason);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
