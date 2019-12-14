package com.redecommunity.proxy.punish.dao;

import com.redecommunity.common.shared.databases.mysql.dao.Table;

/**
 * Created by @SrGutyerrez
 */
public class PunishmentDao extends Table {
    public PunishmentDao() {
        super("server_punishments", "general");
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
                                "`revoke_reason` VARCHAR(255)," +
                                "`proof` VARCHAR(255) NOT NULL," +
                                "`time` LONG NOT NULL," +
                                "`start_time` LONG," +
                                "`end_time` LONG NOT NULL," +
                                "`revoke_time` LONG," +
                                ")",
                        this.getTableName()
                )
        );
    }
}
