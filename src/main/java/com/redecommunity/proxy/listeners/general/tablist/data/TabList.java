package com.redecommunity.proxy.listeners.general.tablist.data;

import com.redecommunity.common.shared.permissions.group.data.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class TabList {
    @Getter
    private final Integer id;
    @Getter
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
}
