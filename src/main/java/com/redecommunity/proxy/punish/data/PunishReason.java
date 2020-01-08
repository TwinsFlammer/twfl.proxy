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
public class PunishReason {
    private final Integer id;
    private final String name, displayName, description;
    private final Group group;
    private final List<Duration> durations;

    public Boolean isSimilar(PunishReason punishReason) {
        return punishReason.getName().equalsIgnoreCase(this.name);
    }

    public String getDescription() {
        if (this.description.contains("\n")) {
            String[] descriptions = this.description.split("\n");

            StringBuilder stringBuilder = new StringBuilder();

            for (String s : descriptions)
                stringBuilder.append(s)
                        .append("\n");

            return stringBuilder.toString();
        }
        return this.description;
    }
}
