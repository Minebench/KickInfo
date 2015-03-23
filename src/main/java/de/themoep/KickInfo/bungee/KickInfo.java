package de.themoep.KickInfo.bungee;

import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        String msg;
        if(event.getKickedFrom().getName().equals(event.getPlayer().getPendingConnection().getListener().getFallbackServer())) {
            event.getPlayer().disconnect(event.getKickReasonComponent());
            msg = " from proxy";
        } else {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Du wurdest von " + event.getKickedFrom().getName() + " gekickt! Grund: " + event.getKickReason());
            Title title = new BungeeTitle();
            title.title(TextComponent.fromLegacyText(ChatColor.GOLD + "Auf Lobby gekickt!"));
            title.subTitle(new ComponentBuilder("Grund: ").color(ChatColor.RED).append(event.getKickReason()).color(ChatColor.YELLOW).create());
            title.fadeIn(20).stay(100).fadeOut(20);
            event.getPlayer().sendTitle(title);
            msg = " to fallback server";
        }
        getLogger().log(Level.INFO, "Kicked " + event.getPlayer().getName() + " from " + event.getKickedFrom().getName() + msg + ": " + event.getKickReason());
    }
}
