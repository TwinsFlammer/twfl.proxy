package com.redecommunity.proxy.punish.manager;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.common.shared.util.ClassGetter;
import com.redecommunity.proxy.Proxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;

/**
 * Created by @SrGutyerrez
 */
public class StartManager {
    public StartManager() {
        new DaoManager();
        new ListenerManager();
        new CommandManager();
    }
}

class DaoManager {
    DaoManager() {
        ClassGetter.getClassesForPackage(Proxy.class).forEach(clazz -> {
            if (Table.class.isAssignableFrom(clazz)) {
                try {
                    Table table = (Table) clazz.newInstance();

                    table.createTable();
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class ListenerManager {
    ListenerManager() {
        ClassGetter.getClassesForPackage(Proxy.class).forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();

                    ProxyServer.getInstance().getPluginManager().registerListener(
                            Proxy.getInstance(),
                            listener
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class CommandManager {
    CommandManager() {
        ClassGetter.getClassesForPackage(Proxy.class).forEach(clazz -> {
            if (CustomCommand.class.isAssignableFrom(clazz)) {
                try {
                    CustomCommand listener = (CustomCommand) clazz.newInstance();

                    // TODO not implemented yet
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}