package com.redecommunity.proxy.punish.dao;

import com.google.common.collect.Lists;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.proxy.punish.data.Punishment;
import com.redecommunity.proxy.punish.manager.PunishmentManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentDao extends Table {

    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_punishments";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KET AUTO_INCREMENT," +
                                "`user_id` INTEGER NOT NULL," +
                                "`staffer_id` INTEGER NOT NULL," +
                                "`motive_id` INTEGER NOT NULL," +
                                "`revoker_id` INTEGER," +
                                "`revoker_motive_id` INTEGER," +
                                "`hidden` BOOLEAN NOT NULL," +
                                "`perpetual` BOOLEAN NOT NULL," +
                                "`status` BOOLEAN NOT NULL," +
                                "`network` VARCHAR(25) NOT NULL," +
                                "`revoke_reason_id` VARCHAR(255)," +
                                "`proof` VARCHAR(255) NOT NULL," +
                                "`time` LONG NOT NULL," +
                                "`start_time` LONG," +
                                "`end_time` LONG NOT NULL," +
                                "`revoke_time` LONG," +
                                ");",
                        this.getTableName()
                )
        );
    }

    public <T extends Punishment> void insert(T object) {
        String query = String.format(
                "INSERT INTO %s " +
                        "(" +
                        "`user_id`," +
                        "`staffer_id`," +
                        "`motive_id`," +
                        "`hidden`," +
                        "`perpetual`," +
                        "`status`," +
                        "`network`," +
                        "`proof`," +
                        "`time`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "%d," +
                        "%d," +
                        "%d," +
                        "%b," +
                        "%b," +
                        "%s," +
                        "%s," +
                        "%d" +
                        ")",
                this.getTableName(),
                object.getId(),
                object.getStafferId(),
                object.getMotiveId(),
                object.isHidden(),
                object.isPerpetual(),
                object.getStatus(),
                object.getNetwork(),
                object.getProof(),
                object.getTime()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public <K, V, U, I> void update(HashMap<K, V> keys, U key, I value) {
        String where = this.generateWhere(keys);

        String query = String.format(
                "UPDATE %s WHERE %s=%s SET %s",
                this.getTableName(),
                key,
                value,
                where
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <K, V, U, I, T> Set<T> findAll(HashMap<K, V> keys) {
        String where = this.generateWhere(keys);

        String query = String.format(
                "SELECT * FROM %s WHERE %s",
                this.getTableName(),
                where
        );

        Set<T> punishments = Collections.emptySet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Punishment punishment = PunishmentManager.toPunishment(resultSet);

                punishments.add((T) punishment);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return punishments;
    }
}
