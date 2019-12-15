package com.redecommunity.proxy.connection.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class ProxyServer {
    @Getter
    private final Integer id;
    @Getter
    @Setter
    private Integer playerCount;
    @Getter
    private final String name;

    @Setter
    private Boolean status;

    public Boolean isOnline() {
        return this.status;
    }

    public void broadcastMessage(String message) {
        // TODO not implemented yet
    }

    public String toJSONString() {
        JSONObject object = new JSONObject();

        object.put("proxy_id", this.id);
        object.put("player_count", this.playerCount);
        object.put("name", this.name);
        object.put("status", this.status);

        return object.toString();
    }
}
