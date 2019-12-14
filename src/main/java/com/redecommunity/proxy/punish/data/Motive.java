package com.redecommunity.proxy.punish.data;

import com.redecommunity.common.shared.permissions.group.data.Group;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class Motive {
    private final Integer id;
    private final String name, displayName, description;
    private final Group group;
    private final List<Duration> durations;
}
