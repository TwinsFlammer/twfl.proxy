package com.redecommunity.proxy.listeners.general.tablist.dao;

import com.google.common.collect.Sets;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.proxy.listeners.general.tablist.data.TabList;
import com.redecommunity.proxy.listeners.general.tablist.manager.TabListManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class TabListDao extends Table {
    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_tablist";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS %s " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`header` TEXT NOT NULL," +
                                "`footer` TEXT NOT NULL," +
                                "`group_id` INTEGER NOT NULL," +
                                "`time` LONG NOT NULL," +
                                "`active` BOOLEAN NOT NULL" +
                                ");"
                )
        );
    }

    @Override
    public <T> Set<T> findAll() {
        String query = String.format(
                "SELECT * FROM %s",
                this.getTableName()
        );

        Set<T> tabLists = Sets.newConcurrentHashSet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                TabList tabList = TabListManager.toTabList(resultSet);

                tabLists.add((T) tabList);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return tabLists;
    }
}
