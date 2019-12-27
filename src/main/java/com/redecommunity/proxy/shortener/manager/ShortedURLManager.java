package com.redecommunity.proxy.shortener.manager;

import com.google.common.collect.Lists;
import com.redecommunity.proxy.shortener.dao.ShortedURLDao;
import com.redecommunity.proxy.shortener.data.ShortedURL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by @SrGutyerrez
 */
public class ShortedURLManager {
    private static List<ShortedURL> shortedURLS = Lists.newArrayList();

    public ShortedURLManager() {
        ShortedURLDao shortedURLDao = new ShortedURLDao();

        Set<ShortedURL> shortedURLS = shortedURLDao.findAll();

        ShortedURLManager.shortedURLS.addAll(shortedURLS);
    }

    public static List<ShortedURL> getShortedURLS() {
        return ShortedURLManager.shortedURLS;
    }

    public static ShortedURL getShortedURL(Integer id) {
        return ShortedURLManager.shortedURLS
                .stream()
                .filter(shortedURL -> shortedURL.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static ShortedURL getShortedURL(String name) {
        return ShortedURLManager.shortedURLS
                .stream()
                .filter(shortedURL -> shortedURL.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static ShortedURL toShortedURL(ResultSet resultSet) throws SQLException {
        return new ShortedURL(
                resultSet.getInt("id"),
                resultSet.getString("link"),
                resultSet.getString("name"),
                resultSet.getInt("user_id"),
                resultSet.getLong("time"),
                resultSet.getBoolean("active")
        );
    }
}
