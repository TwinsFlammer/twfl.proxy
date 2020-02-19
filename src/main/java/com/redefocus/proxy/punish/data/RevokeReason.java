package com.redefocus.proxy.punish.data;

import com.redefocus.common.shared.permissions.group.data.Group;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class RevokeReason {
    private final Integer id;
    private final String name, displayName, description;
    private final Group group;

    public String getDescription() {
        if (this.description.contains("\\n")) {
            String[] descriptions = this.description.split("\\\\n");

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < descriptions.length; i++) {
                String description = descriptions[i];

                stringBuilder.append(description)
                        .append(i+1 >= descriptions.length ? "" : "\n");
            }

            return stringBuilder.toString();
        }
        return this.description;
    }
}