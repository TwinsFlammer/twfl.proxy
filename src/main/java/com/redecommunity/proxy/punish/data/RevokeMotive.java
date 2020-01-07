package com.redecommunity.proxy.punish.data;

import com.redecommunity.common.shared.permissions.group.data.Group;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class RevokeMotive {
    private final Integer id;
    private final String name, displayName, description;
    private final Group group;
}
