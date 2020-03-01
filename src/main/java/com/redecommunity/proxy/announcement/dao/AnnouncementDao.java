package com.redecommunity.proxy.announcement.dao;

import com.google.common.collect.Sets;
import com.redecommunity.proxy.announcement.data.Announcement;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.proxy.announcement.manager.AnnouncementManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class AnnouncementDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_announcement";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`title` VARCHAR(16) NOT NULL," +
                                "`message` VARCHAR(255) NOT NULL," +
                                "`url` VARCHAR(255)," +
                                "`active` BOOLEAN NOT NULL" +
                                ");",
                        this.getTableName()
                )
        );
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

    @Override
    public <K, V, T> T findOne(K key, V value) {
        String query = String.format(
                "SELECT * FROM %s WHERE `%s`=%d",
                this.getTableName(),
                key,
                value
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()) return (T) AnnouncementManager.toAnnouncement(resultSet);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> Set<T> findAll() {
        String query = String.format(
                "SELECT * FROM %s",
                this.getTableName()
        );

        Set<T> announcements = Sets.newConcurrentHashSet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Announcement announcement = AnnouncementManager.toAnnouncement(resultSet);

                announcements.add((T) announcement);
            }
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }

        return announcements;
    }
}
