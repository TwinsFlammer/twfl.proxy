package br.com.twinsflammer.proxy.user.data;

import br.com.twinsflammer.api.bungeecord.user.data.BungeeUser;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

/**
 * @author SrGutyerrez
 */
public class ProxyUser extends BungeeUser {
    public ProxyUser(User user) {
        super(user);
    }
}
