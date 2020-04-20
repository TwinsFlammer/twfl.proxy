package com.redefocus.proxy.util;

/**
 * Created by @SrGutyerrez
 */
public class Messages {
    public static final String PREFIX = "§6§lREDE FOCUS";

    public static final String INVALID_UUID_MESSAGE = "\n" +
            Messages.PREFIX +
            "\n" +
            "\n" +
            "§cVocê foi desconectado do servidor. Sua UUID está inválida." +
            "\n" +
            "§cAcesse: §nhttps://rfcs.me/uuid §r§cpara mais informações." +
            "\n";

    public static final String INVALID_USER = "\n" +
            Messages.PREFIX +
            "\n" +
            "\n" +
            "§cOcorreu um erro ao carregar seu usuário de nosso banco de dados" +
            "\n" +
            "§cCódigo do erro: §aM4YSQL1" +
            "\n" +
            "§cCaso queira obter mais informações, utilize o código a cima" +
            "\n" +
            "§cpara conversar com algum membro de nossa equipe." +
            "\n";

    public static final String INVALID_USERNAME = "\n" +
            Messages.PREFIX +
            "\n" +
            "\n" +
            "§cOcorreu um erro ao carregar seu usuário de nosso banco de dados" +
            "\n" +
            "§cCódigo do erro: §aM4YSQL1" +
            "\n" +
            "§cCaso queira obter mais informações, utilize o código a cima" +
            "\n" +
            "§cpara conversar com algum membro de nossa equipe." +
            "\n";

    public static final String DONT_HAVE_LOBBY = "\n" +
            Messages.PREFIX +
            "\n" +
            "\n" +
            "§cVocê foi desconectado do servidor!" +
            "\n" +
            "§cMotivo: Não foi possível localizar um saguão disponível." +
            "\n" +
            "\n" +
            "§cTente novamente mais tarde." +
            "\n";

    public static final String IN_MAINTENANCE = "\n" +
            Messages.PREFIX +
            "\n" +
            "\n" +
            "§cVocê foi desconectado do servidor!" +
            "\n" +
            "§cMotivo: Estamos efetuando uma manutenção, tente novamente mais tarde." +
            "\n";
}
