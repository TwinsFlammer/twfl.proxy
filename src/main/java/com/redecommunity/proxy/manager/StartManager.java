package com.redecommunity.proxy.manager;

import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.registry.CommandRegistry;
import com.redecommunity.common.shared.Common;
import com.redecommunity.common.shared.databases.mysql.dao.Table;
import com.redecommunity.common.shared.util.ClassGetter;
import com.redecommunity.proxy.Proxy;
import com.redecommunity.proxy.connection.manager.ProxyServerManager;
import com.redecommunity.proxy.connection.runnable.ProxyServerRefreshRunnable;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class StartManager {
    public StartManager() {
        new DaoManager();
        new ListenerManager();
        new CommandManager();

        new DataManager();

        new RunnableManager();
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
                    CustomCommand customCommand = (CustomCommand) clazz.newInstance();

                    CommandRegistry.registerCommand(
                            Proxy.getInstance(),
                            customCommand
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class DataManager {
    DataManager() {
        new ProxyServerManager();
    }
}

class RunnableManager {
    RunnableManager() {
        Common.getInstance().getScheduler().scheduleWithFixedDelay(
                new ProxyServerRefreshRunnable(),
                0,
                1,
                TimeUnit.SECONDS
        );
    }
}