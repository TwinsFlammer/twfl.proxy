package com.redefocus.proxy.punish.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public enum PunishType {
    IP_BAN("§cBan eterno", "eternamente banido"),
    BAN("§cBan eterno", "banido"),
    TEMP_BAN("§eBan temporário", "temporariamente banido"),
    MUTE("§aMute temporário", "silenciado");

    private final String name, displayName;
}
