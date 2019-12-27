package com.redecommunity.proxy.account.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class Password {
    private final String password;
    private final Long time;

    public static Password toPassword(ResultSet resultSet) throws SQLException {
        return new Password(
                resultSet.getString("password"),
                resultSet.getLong("ime")
        );
    }
}
