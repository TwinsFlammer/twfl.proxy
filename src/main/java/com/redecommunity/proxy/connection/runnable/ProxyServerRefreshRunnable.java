package com.redecommunity.proxy.connection.runnable;

import com.redecommunity.proxy.connection.dao.ProxyServerDao;
import com.redecommunity.proxy.connection.data.ProxyServer;
import com.redecommunity.proxy.connection.manager.ProxyServerManager;

/**
 * Created by @SrGutyerrez
 */
public class ProxyServerRefreshRunnable implements Runnable {
    @Override
    public void run() {
        ProxyServerDao proxyServerDao = new ProxyServerDao();

        proxyServerDao.findAll().forEach(proxyServer -> {
            ProxyServer proxyServer1 = ProxyServerManager.getProxyServer(proxyServer.getId());

            if (proxyServer1 != null) {
                proxyServer1.setUsersId(proxyServer.getUsersId());
                System.out.println("Atualizado jogadores");
            } else {
                ProxyServerManager.addProxyServer(proxyServer);

                System.out.println("Adicionado aos proxies");
            }
        });
    }
}
