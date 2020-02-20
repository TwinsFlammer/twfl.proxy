package com.redefocus.proxy.manager;

import com.redefocus.api.bungeecord.commands.CustomCommand;
import com.redefocus.api.bungeecord.commands.registry.CommandRegistry;
import com.redefocus.api.shared.connection.manager.ProxyServerManager;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.util.ClassGetter;
import com.redefocus.proxy.Proxy;
import com.redefocus.proxy.authentication.manager.AuthenticationManager;
import com.redefocus.proxy.authentication.manager.PasswordManager;
import com.redefocus.proxy.announcement.manager.AnnouncementManager;
import com.redefocus.proxy.commands.defaults.players.tell.manager.DirectMessageManager;
import com.redefocus.proxy.connection.listeners.motd.manager.MOTDManager;
import com.redefocus.proxy.listeners.general.tablist.manager.TabListManager;
import com.redefocus.proxy.punish.manager.PunishReasonManager;
import com.redefocus.proxy.punish.manager.PunishmentManager;
import com.redefocus.proxy.punish.manager.RevokeReasonManager;
import com.redefocus.proxy.report.manager.ReportManager;
import com.redefocus.proxy.shorter.manager.ShortedURLManager;
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

        new DataManager();

        new ChannelManager();

        new JedisMessageListenerManager();
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

class ChannelManager {
    ChannelManager() {
        ClassGetter.getClassesForPackage(Proxy.class).forEach(clazz -> {
            if (Channel.class.isAssignableFrom(clazz)) {
                try {
                    Channel channel = (Channel) clazz.newInstance();

                    Common.getInstance().getChannelManager().register(channel);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class JedisMessageListenerManager {
    JedisMessageListenerManager() {
        ClassGetter.getClassesForPackage(Proxy.class).forEach(clazz -> {
            if (JedisMessageListener.class.isAssignableFrom(clazz)) {
                try {
                    JedisMessageListener jedisMessageListener = (JedisMessageListener) clazz.newInstance();

                    Common.getInstance().getJedisMessageManager().registerListener(jedisMessageListener);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class DataManager {
    DataManager() {
        new ProxyServerManager(
                Proxy.getInstance().getId(),
                Proxy.getInstance().getName()
        );
        new DirectMessageManager();
        new TabListManager();
        new MOTDManager();
        new ShortedURLManager();
        new AuthenticationManager();
        new PasswordManager();
        new AnnouncementManager();
        new RevokeReasonManager();
        new PunishmentManager();
        new PunishReasonManager();
        new ReportManager();
    }
}