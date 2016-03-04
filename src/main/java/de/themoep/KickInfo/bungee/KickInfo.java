package de.themoep.KickInfo.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
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
        String msg;
        boolean onDefaultServer = event.getKickedFrom().getName().equals(event.getPlayer().getPendingConnection().getListener().getDefaultServer());
        boolean onFallbackServer = event.getKickedFrom().getName().equals(event.getPlayer().getPendingConnection().getListener().getFallbackServer());
        int serverVersion = event.getKickedFrom().getPlayers().size() > 0 ? new ArrayList<ProxiedPlayer>(event.getKickedFrom().getPlayers()).get(0).getPendingConnection().getVersion() : 0;
        int cancelServerVersion = event.getCancelServer().getPlayers().size() > 0 ? new ArrayList<ProxiedPlayer>(event.getCancelServer().getPlayers()).get(0).getPendingConnection().getVersion() : 0;
        if((onDefaultServer || onFallbackServer) && cancelServerVersion == event.getPlayer().getPendingConnection().getVersion()) {
            event.getPlayer().disconnect(event.getKickReasonComponent());
            msg = " from proxy";
        } else if(serverVersion == event.getPlayer().getPendingConnection().getVersion()){
            event.getPlayer().sendMessage(ChatColor.GOLD + "Du wurdest von " + event.getKickedFrom().getName() + " gekickt! Grund: " + event.getKickReason());
            Title title = getProxy().createTitle();
            title.title(TextComponent.fromLegacyText(ChatColor.GOLD + "Auf Lobby gekickt!"));
            title.subTitle(new ComponentBuilder("Grund: ").color(ChatColor.RED).append(event.getKickReason()).color(ChatColor.YELLOW).create());
            title.fadeIn(20).stay(100).fadeOut(20);
            event.getPlayer().sendTitle(title);
            msg = " to lobby";
        } else {
            msg = "";
        }
        getLogger().log(Level.INFO, "Kicked " + event.getPlayer().getName() + " from " + event.getKickedFrom().getName() + msg + ": " + event.getKickReason());
    }
}
