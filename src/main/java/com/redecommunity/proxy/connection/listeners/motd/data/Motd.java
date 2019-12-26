package com.redecommunity.proxy.connection.listeners.motd.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
public class Motd {
    @Getter
    private final Integer id;
    @Getter
    private final String line1, line2;
    private final Boolean active;

    public Boolean isActive() {
        return this.active;
    }
}
