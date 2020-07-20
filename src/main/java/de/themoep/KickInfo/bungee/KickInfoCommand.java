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

import de.themoep.bungeeplugin.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.io.IOException;

public class KickInfoCommand extends PluginCommand<KickInfo> {

    public KickInfoCommand(KickInfo plugin) {
        super(plugin, "kickinfo");
    }

    @Override
    protected boolean run(CommandSender sender, String[] args) {
        if (args.length > 0 && "reload".equalsIgnoreCase(args[0])) {
            try {
                plugin.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "Plugin reloaded.");
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + "Error while reloading! " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
