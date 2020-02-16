package com.redefocus.proxy.announcement.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class Announcement {
    @Getter
    private final Integer id;
    @Getter
    private final String title, message;
    @Getter
    private final URL url;
    @Setter
    private Boolean active;

    public Boolean isActive() {
        return this.active;
    }
}
