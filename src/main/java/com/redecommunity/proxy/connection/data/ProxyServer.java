package com.redecommunity.proxy.connection.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collection;

/**
 * Created by @SrGutyerrez
 */
@AllArgsConstructor
public class ProxyServer {
    @Getter
    private final Integer id;
    @Getter
    private final String name;

    @Setter
    private Boolean status;
    @Getter
    @Setter
    private Collection<Integer> usersId;

    public Boolean isOnline() {
        return this.status;
    }

    public Integer getPlayerCount() {
        return this.usersId.size();
    }

    public void broadcastMessage(String message) {
        // TODO not implemented yet
    }

    public String toJSONString() {
        JSONObject object = new JSONObject();

        object.put("proxy_id", this.id);

        JSONArray players = new JSONArray();

        players.addAll(this.usersId);

        object.put("players_id", players);
        object.put("name", this.name);
        object.put("status", this.status);

        return object.toString();
    }
}
