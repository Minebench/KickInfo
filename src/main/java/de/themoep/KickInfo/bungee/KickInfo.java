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
 * KickInfo is licensed under the Nietzsche Public License v0.6
 *
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
 *
 * Copyright, like God, is dead.  Let its corpse serve only to guard against its
 * resurrection.  You may do anything with this work that copyright law would
 * normally restrict so long as you retain the above notice(s), this license, and
 * the following misquote and disclaimer of warranty with all redistributed
 * copies, modified or verbatim.  You may also replace this license with the Open
 * Works License, available at the http://owl.apotheon.org website.
 *
 *    Copyright is dead.  Copyright remains dead, and we have killed it.  How
 *    shall we comfort ourselves, the murderers of all murderers?  What was
 *    holiest and mightiest of all that the world of censorship has yet owned has
 *    bled to death under our knives: who will wipe this blood off us?  What
 *    water is there for us to clean ourselves?  What festivals of atonement,
 *    what sacred games shall we have to invent?  Is not the greatness of this
 *    deed too great for us?  Must we ourselves not become authors simply to
 *    appear worthy of it?
 *                                     - apologies to Friedrich Wilhelm Nietzsche
 *
 * No warranty is implied by distribution under the terms of this license.
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
            int i = priority.indexOf(event.getKickedFrom().getName());
            try {
                event.setCancelServer(getProxy().getServerInfo(priority.get(i + 1)));
                getLogger().log(Level.INFO, "To another fallback server (" + event.getCancelServer().getName() + ")");
            } catch(IndexOutOfBoundsException e) {
                event.getPlayer().disconnect(event.getKickReasonComponent());
            }
        }
    }
}
