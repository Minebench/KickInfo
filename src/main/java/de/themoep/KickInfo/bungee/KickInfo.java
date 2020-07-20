package de.themoep.KickInfo.bungee;

/*
 * KickInfo
 * Copyright (C) 2020. Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.themoep.bungeeplugin.BungeePlugin;
import de.themoep.minedown.MineDown;
import de.themoep.minedown.MineDownParser;
import de.themoep.utils.lang.bungee.LanguageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class KickInfo extends BungeePlugin implements Listener {

    private Map<UUID, Integer> kickCounts = new HashMap<>();

    private LanguageManager lang;

    public void onEnable() {
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new KickInfoCommand(this));
    }

    public void loadConfig() throws IOException {
        getConfig().loadConfig();
        lang = new LanguageManager(this, getConfig().getString("default-lang"));
        lang.loadConfigs();
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        getLogger().log(Level.INFO, event.getPlayer().getName() + " disconnected from " + event.getKickedFrom().getName() + ": " + event.getKickReason());
        if (event.getKickReason().contains("[Proxy]")) {
            event.getPlayer().disconnect(event.getKickReasonComponent());
            return;
        }
        List<String> priorities = event.getPlayer().getPendingConnection().getListener().getServerPriority();
        int kickCount = kickCounts.getOrDefault(event.getPlayer().getUniqueId(), 0);
        if (kickCount >= priorities.size()) {
            event.getPlayer().disconnect(event.getKickReasonComponent());
            return;
        }
        kickCounts.put(event.getPlayer().getUniqueId(), kickCount + 1);
        int fromIndex = priorities.indexOf(event.getKickedFrom().getName());
        ServerInfo target = getProxy().getServerInfo(fromIndex >= priorities.size() - 1 ? priorities.get(0) : priorities.get(fromIndex + 1));
        event.setCancelServer(target);
        event.setCancelled(true);

        String[] replacements = {
                "server", event.getKickedFrom().getName(),
                "target", target.getName(),
                "reason", event.getKickReason()
        };
        event.getPlayer().sendMessage(getMessage(event.getPlayer(),"chat-message", replacements));
        Title title = getProxy().createTitle();
        title.title(getMessage(event.getPlayer(), "title.main", replacements));
        title.subTitle(getMessage(event.getPlayer(), "title.sub", replacements));
        title.fadeIn(20).stay(100).fadeOut(20);
        event.getPlayer().sendTitle(title);
        getLogger().log(Level.INFO, "To fallback server");
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        kickCounts.remove(event.getPlayer().getUniqueId());
    }

    private BaseComponent[] getMessage(CommandSender sender, String key, String... replacements) {
        return MineDown.parse(lang.getConfig(sender).get(key), replacements);
    }
}
