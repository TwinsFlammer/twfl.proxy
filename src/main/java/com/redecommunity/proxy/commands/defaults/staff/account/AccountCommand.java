package com.redecommunity.proxy.commands.defaults.staff.account;

import com.google.common.collect.Lists;
import com.redecommunity.api.bungeecord.commands.CustomCommand;
import com.redecommunity.api.bungeecord.commands.enums.CommandRestriction;
import com.redecommunity.api.bungeecord.util.JSONText;
import com.redecommunity.common.shared.language.enums.Language;
import com.redecommunity.common.shared.permissions.group.data.Group;
import com.redecommunity.common.shared.permissions.user.data.User;
import com.redecommunity.common.shared.permissions.user.group.data.UserGroup;
import com.redecommunity.common.shared.permissions.user.manager.UserManager;
import com.redecommunity.common.shared.server.data.Server;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by @SrGutyerrez
 */
public class AccountCommand extends CustomCommand {
    public AccountCommand() {
        super("account", CommandRestriction.ALL, "helper");
    }

    @Override
    public void onCommand(User user, String[] args) {
        Language language = user.getLanguage();

        if (args.length != 1) {
            user.sendMessage(
                    String.format(
                            language.getMessage("messages.default_commands.invalid_usage"),
                            this.getName(),
                            "<usuário>"
                    )
            );
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            user.sendMessage(
                    language.getMessage("messages.player.invalid_player")
            );
            return;
        }

        Group group = user1.getHighestGroup();

        JSONText jsonText = new JSONText();

        jsonText.next()
                .text("\n\n")
                .next()
                .text("§eInformações sobre o usuário ")
                .next()
                .text(group.getColor() + user1.getDisplayName())
                .next()
                .text("§e:")
                .next()
                .text("\n\n")
                .next()
                .text(" §eInformações básicas")
                .next()
                .text("\n\n")
                .next()
                .text("   Id: §7" + user1.getId())
                .next()
                .text("\n")
                .next()
                .text("   Data de registro: §7" + this.format(user1.getCreatedAt()))
                .next()
                .text("\n")
                .next()
                .text("   Última autenticação: §7" + (user1.getLastLogin() == null ? "Indisponível" : this.format(user1.getLastLogin())))
                .next()
                .text("\n")
                .next()
                .text("   Linguagem: §7" + user1.getLanguage().getDisplayName())
                .next()
                .text("\n")
                .next()
                .text("   Punido: §c--/--")
                .next()
                .text("\n\n")
                .next()
                .text(" §eInformações avançadas:")
                .next()
                .text("\n\n")
                .next()
                .text("   Endereço conectado: §7" + (user1.isOnline() ? user1.getConnectedAddress() : "Indisponível"))
                .next()
                .text("\n")
                .next()
                .text("   Último IP: §7" + (user.hasGroup("manager") ? user1.getLastAddress() : "§cSem permissão"))
                .next()
                .text("\n\n")
                .next()
                .text(" §eAssociações:")
                .next()
                .text("\n\n")
                .next()
                .text("   E-mail: §7" + (user1.getEmail() == null ? "Indisponível" : user1.getEmail()))
                .next()
                .text("\n")
                .next()
                .text("   Discord: §7")
                .next();

        if (user.hasGroup("manager") && user1.getDiscordId() == 0) {
            jsonText.text("§7Indisponível");
        } else if (user1.getDiscordId() != 0) {
            jsonText.text("§7[Clique para copiar]")
                    .clickSuggest(user1.getDiscordId().toString());
        } else if (!user.hasGroup("manager")) jsonText.text("§cSem permissão");

        jsonText.next()
                .text("\n\n")
                .next()
                .text(" §eServidores:")
                .next()
                .text("\n\n");

        user1.getGroups()
                .stream()
                .map(UserGroup::getServer)
                .distinct()
                .forEach(server -> {
                    jsonText.next()
                            .text("   " + (server == null ? "Rede: " : server.getDisplayName() + ": "))
                            .next();

                    List<Group> groups = user1.getGroups()
                            .stream()
                            .filter(userGroup -> server == null ? userGroup.getServer() == null : userGroup.getServer().isSimilar(server))
                            .map(UserGroup::getGroup)
                            .sorted((group1, group2) -> group2.getPriority().compareTo(group1.getPriority()))
                            .collect(Collectors.toList());

                    IntStream.range(0, groups.size())
                            .forEach(i -> {
                                Group group1 = groups.get(i);

                                jsonText.text(group1.getColor() + group1.getName())
                                        .next()
                                        .text((i + 1 == groups.size() ? "" : ", "))
                                        .next();
                            });

                    jsonText.text("\n");
                });

        if (user1.getGroups().isEmpty()) jsonText.next()
                .text("   --/--")
                .next()
                .text("\n");

        jsonText.next()
                .text("\n")
                .next()
                .text(" §eConexão:")
                .next()
                .text("\n\n")
                .next()
                .text("   Conectado: " + (user1.isOnline() ? "§aSim" : "§cNão"))
                .next()
                .text("\n")
                .next()
                .text("   Proxy: §7" + (user1.isOnline() ? user1.getProxyId() : "Indisponível"))
                .next()
                .text("\n")
                .next()
                .text("   Servidor: §7" + (user1.isOnline() ? user1.getServer().getDisplayName() : "Indisponível"))
                .next()
                .text("\n")
                .next()
                .send(user);
    }

    private String format(Long millis) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return format.format(millis);
    }
}
