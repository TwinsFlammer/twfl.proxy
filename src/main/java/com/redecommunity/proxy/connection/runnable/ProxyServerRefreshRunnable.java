package com.redecommunity.proxy.connection.runnable;

import com.redecommunity.proxy.connection.dao.ProxyDao;
import com.redecommunity.proxy.connection.data.ProxyServer;
import com.redecommunity.proxy.connection.manager.ProxyServerManager;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerRefreshRunnable implements Runnable {
    @Override
    public void run() {
        ProxyDao proxyDao = new ProxyDao();

        proxyDao.findAll().forEach(proxyServer -> {
            ProxyServer proxyServer1 = ProxyServerManager.getProxyServer(proxyServer.getId());

            if (proxyServer1 != null) proxyServer1.setUsersId(proxyServer.getUsersId());
            else ProxyServerManager.addProxyServer(proxyServer);
        });
    }
}
