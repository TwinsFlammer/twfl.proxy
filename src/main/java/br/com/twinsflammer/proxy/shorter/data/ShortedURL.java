package br.com.twinsflammer.proxy.shorter.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class ShortedURL {
    @Getter
    private final Integer id;
    @Getter
    private final String url, name;
    @Getter
    private final Integer userId;
    @Getter
    private final Long time;
    @Setter
    private Boolean active;

    public Boolean isActive() {
        return this.active;
    }
}
