package de.themoep.KickInfo.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
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
        getLogger().log(Level.INFO, event.getPlayer().getName() + " was kicked from " + event.getKickedFrom().getName() + ": " + event.getKickReason());
        List<String> priority = event.getPlayer().getPendingConnection().getListener().getServerPriority();
        if(!priority.contains(event.getKickedFrom().getName())) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Du wurdest von " + event.getKickedFrom().getName() + " gekickt! Grund: " + event.getKickReason());
            Title title = getProxy().createTitle();
            title.title(TextComponent.fromLegacyText(ChatColor.GOLD + "Auf Lobby gekickt!"));
            title.subTitle(new ComponentBuilder("Grund: ").color(ChatColor.RED).append(event.getKickReason()).color(ChatColor.YELLOW).create());
            title.fadeIn(20).stay(100).fadeOut(20);
            event.getPlayer().sendTitle(title);
            getLogger().log(Level.INFO, "To fallback server");
        } else if(priority.indexOf(event.getKickedFrom().getName()) == priority.size() - 1) {
            event.getPlayer().disconnect(event.getKickReasonComponent());
        } else {
            getLogger().log(Level.INFO, "To another fallback server");
        }
    }
}
