package br.com.twinsflammer.proxy.authentication.manager;

import br.com.twinsflammer.proxy.authentication.data.Password;
import com.google.common.collect.Maps;
import br.com.twinsflammer.common.shared.permissions.user.data.User;

import java.util.HashMap;

/**
 * Created by @SrGutyerrez
 */
public class PasswordManager {
    private static HashMap<Integer, Password> passwords = Maps.newHashMap();

    public static HashMap<Integer, Password> getPasswords() {
        return PasswordManager.passwords;
    }

    public static Password getPasswords(Integer id) {
        return PasswordManager.passwords.get(id);
    }

    public static Password getPasswords(User user) {
        return PasswordManager.getPasswords(user.getId());
    }
}
