package com.redecommunity.proxy.punish.data;

import com.redecommunity.proxy.punish.data.enums.PunishType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class Duration {
    private final Integer duration;
    private final TimeUnit timeType;
    private final PunishType punishType;
}
