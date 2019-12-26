package com.redecommunity.proxy.connection.data;

import com.redecommunity.common.shared.permissions.user.data.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

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
    private List<User> users;

    public Boolean isOnline() {
        return this.status;
    }

    public Integer getPlayerCount() {
        return this.users.size();
    }

    public String toJSONString() {
        JSONObject object = new JSONObject();

        object.put("proxy_id", this.id);

        JSONArray players = new JSONArray();

        players.addAll(this.users);

        object.put("users", players);
        object.put("name", this.name);
        object.put("status", this.status);

        return object.toString();
    }
}
