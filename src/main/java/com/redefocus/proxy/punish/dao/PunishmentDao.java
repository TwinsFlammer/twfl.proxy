package com.redefocus.proxy.punish.dao;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.redefocus.proxy.punish.manager.PunishmentManager;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.proxy.punish.data.Punishment;

import java.sql.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentDao<T extends Punishment> extends Table {

    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_punishment";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`user_id` INTEGER NOT NULL," +
                                "`staffer_id` INTEGER NOT NULL," +
                                "`reason_id` INTEGER NOT NULL," +
                                "`revoke_user_id` INTEGER," +
                                "`revoke_reason_id` INTEGER," +
                                "`count` INTEGER NOT NULL," +
                                "`hidden` BOOLEAN NOT NULL," +
                                "`perpetual` BOOLEAN NOT NULL," +
                                "`status` BOOLEAN NOT NULL," +
                                "`proof` VARCHAR(255)," +
                                "`time` LONG NOT NULL," +
                                "`start_time` LONG," +
                                "`end_time` LONG," +
                                "`revoke_time` LONG" +
                                ");",
                        this.getTableName()
                )
        );
    }

    public T insert(T object) {
        String query = String.format(
                "INSERT INTO %s " +
                        "(" +
                        "`user_id`," +
                        "`staffer_id`," +
                        "`reason_id`," +
                        "`count`," +
                        "`hidden`," +
                        "`perpetual`," +
                        "`status`," +
                        "`proof`," +
                        "`time`," +
                        "`end_time`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "%d," +
                        "%d," +
                        "%d," +
                        "%d," +
                        "%b," +
                        "%b," +
                        "%b," +
                        "%s," +
                        "%d," +
                        "%d" +
                        ");",
                this.getTableName(),
                object.getUserId(),
                object.getStafferId(),
                object.getReasonId(),
                object.getCount(),
                object.isHidden(),
                object.isPerpetual(),
                object.getStatus(),
                object.hasValidProof() ? null : "'" + object.getProof() + "'",
                object.getTime(),
                object.getEndTime()
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.execute();

            HashMap<Object, Object> keys = Maps.newHashMap();

            keys.put("user_id", object.getUserId());
            keys.put("staffer_id", object.getStafferId());
            keys.put("reason_id", object.getReasonId());
            keys.put("count", object.getCount());
            keys.put("hidden", object.isHidden());
            keys.put("perpetual", object.isPerpetual());
            keys.put("status", object.getStatus());

            if (object.hasValidProof())
                keys.put("proof", object.getProof());

            keys.put("time", object.getTime());

            if (object.isTemporary())
                keys.put("end_time", object.getEndTime());

            return (T) this.findOne(keys);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public <K, V, U, I> void update(HashMap<K, V> keys, U key, I value) {
        String where = this.generateWhere(keys);

        String query = String.format(
                "UPDATE %s SET %s WHERE `%s`=%d;",
                this.getTableName(),
                where,
                key,
                value
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

    public <K, V, T extends Punishment> T findOne(HashMap<K, V> keys) {
        String where = this.generateWhere(keys).replaceAll(",", " AND");

        String query = String.format(
                "SELECT * FROM %s WHERE %s;",
                this.getTableName(),
                where
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()) return (T) PunishmentManager.toPunishment(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public <K, V, U, I, T> Set<T> findAll(HashMap<K, V> keys) {
        String where = this.generateWhere(keys);

        String query = String.format(
                "SELECT * FROM %s WHERE %s;",
                this.getTableName(),
                where
        );

        Set<T> punishments = Sets.newConcurrentHashSet();

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
