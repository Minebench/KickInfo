package de.themoep.KickInfo.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;

/**
 * Created by Phoenix616 on 18.03.2015.
 */
public class KickInfo extends Plugin implements Listener {

    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        getLogger().log(Level.INFO, "Kicked " + event.getPlayer().getName() + " from " + event.getKickedFrom().getName() + ": " + event.getKickReason());
        if(event.getKickReason().toLowerCase().startsWith("outdated")) {
            ListenerInfo l = event.getPlayer().getPendingConnection().getListener();
            if(l.getServerPriority().size() <= 1) {
                return;
            }
            int i = l.getServerPriority().indexOf(event.getKickedFrom().getName());
            if(i != -1 && l.getServerPriority().size() > i + 1) {
                ServerInfo s = getProxy().getServerInfo(l.getServerPriority().get(i + 1));
                event.setCancelServer(s);
                event.setCancelled(true);
            }
            return;
        }
        String msg;
        if(event.getPlayer().getPendingConnection().getListener().getServerPriority().contains(event.getKickedFrom().getName())) {
            event.getPlayer().disconnect(event.getKickReasonComponent());
            msg = "From proxy";
        } else {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Du wurdest von " + event.getKickedFrom().getName() + " gekickt! Grund: " + event.getKickReason());
            Title title = getProxy().createTitle();
            title.title(TextComponent.fromLegacyText(ChatColor.GOLD + "Auf Lobby gekickt!"));
            title.subTitle(new ComponentBuilder("Grund: ").color(ChatColor.RED).append(event.getKickReason()).color(ChatColor.YELLOW).create());
            title.fadeIn(20).stay(100).fadeOut(20);
            event.getPlayer().sendTitle(title);
            msg = "To fallback server";
        }
        getLogger().log(Level.INFO, msg);
    }
}
