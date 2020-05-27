package br.com.twinsflammer.proxy.user.factory;

import br.com.twinsflammer.common.shared.permissions.user.factory.AbstractUserFactory;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.proxy.user.data.ProxyUser;

import java.util.UUID;

/**
 * @author SrGutyerrez
 */
public class ProxyUserFactory<U extends ProxyUser> extends AbstractUserFactory<U> {
    @Override
    public U getUser(Integer id) {
        ProxyUser proxyUser = new ProxyUser(
                UserManager.getUser(id)
        );

        return (U) proxyUser;
    }

    @Override
    public U getUser(String username) {
        ProxyUser proxyUser = new ProxyUser(
                UserManager.getUser(username)
        );

        return (U) proxyUser;
    }

    @Override
    public U getUser(UUID uniqueId) {
        ProxyUser proxyUser = new ProxyUser(
                UserManager.getUser(uniqueId)
        );

        return (U) proxyUser;
    }
}
