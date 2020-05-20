package br.com.twinsflammer.proxy.listeners.general.tablist.manager;

import br.com.twinsflammer.proxy.listeners.general.tablist.data.TabList;
import com.google.common.collect.Lists;
import br.com.twinsflammer.proxy.listeners.general.tablist.dao.TabListDao;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.permissions.group.manager.GroupManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
public class TabListManager {
    private static List<TabList> tabLists = Lists.newArrayList();

    public TabListManager() {
        TabListDao tabListDao = new TabListDao();

        List<TabList> tabLists = Lists.newArrayList(tabListDao.findAll());

        TabListManager.setTabLists(tabLists);

        Common.getInstance().getScheduler().scheduleAtFixedRate(
                () -> {
                    List<TabList> tabLists1 = Lists.newArrayList(tabListDao.findAll());

                    TabListManager.setTabLists(tabLists1);
                },
                0,
                20,
                TimeUnit.SECONDS
        );
    }

    private static List<TabList> setTabLists(List<TabList> tabLists) {
        return TabListManager.tabLists = tabLists;
    }

    public static TabList getCurrentTabList(User user) {
        return TabListManager.tabLists
                .stream()
                .filter(TabList::isActive)
                .filter(tabList -> user.hasGroup(tabList.getGroup()))
                .min((tabList1, tabList2) -> tabList2.getTime().compareTo(tabList1.getTime()))
                .orElse(null);
    }

    public static TabList getStaffTabList(User user) {
        return TabListManager.tabLists
                .stream()
                .filter(TabList::isActive)
                .filter(tabList -> tabList.getGroup().getPriority() > 80)
                .min((tabList1, tabList2) -> tabList2.getTime().compareTo(tabList1.getTime()))
                .orElse(
                        TabListManager.getCurrentTabList(user)
                );
    }

    public static TabList toTabList(ResultSet resultSet) throws SQLException {
        return new TabList(
                resultSet.getInt("id"),
                resultSet.getString("header"),
                resultSet.getString("footer"),
                GroupManager.getGroup(resultSet.getInt("group_id")),
                resultSet.getLong("time"),
                resultSet.getBoolean("active")
        );
    }
}
