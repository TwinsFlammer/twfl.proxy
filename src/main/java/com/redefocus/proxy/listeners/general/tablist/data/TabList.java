package com.redefocus.proxy.listeners.general.tablist.data;

import com.redefocus.api.shared.connection.manager.ProxyServerManager;
import com.redefocus.common.shared.permissions.group.data.Group;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.proxy.Proxy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class TabList {
    @Getter
    private final Integer id;

    private final String header, footer;
    @Getter
    private final Group group;
    @Getter
    private final Long time;
    @Setter
    private Boolean active;

    public Boolean isActive() {
        return this.active;
    }

    public String getHeader() {
        return StringUtils.replaceEach(
                Helper.colorize(this.header),
                new String[]{
                        "<nl>"
                },
                new String[]{
                        "\n"
                }
        );
    }

    public String getFooter() {
        return StringUtils.replaceEach(
                Helper.colorize(this.footer),
                new String[] {
                        "{proxy_count}",
                        "{proxy_count_player}",
                        "{proxy_count_online}",
                        "{proxy_count_players}",
                        "\\\\n"
                },
                new String[] {
                        String.valueOf(ProxyServerManager.getProxyCount()),
                        String.valueOf(Proxy.getCurrentProxyPlayerCount()),
                        String.valueOf(ProxyServerManager.getProxyCountOnline()),
                        String.valueOf(ProxyServerManager.getUsers().size()),
                        "\n"
                }
        );
    }
}
