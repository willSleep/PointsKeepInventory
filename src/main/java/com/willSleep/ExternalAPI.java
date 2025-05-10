package com.willSleep;

import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * 调用外部API
 */
public class ExternalAPI {
    public static boolean isPlayerAFK(Player player) {
        IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentials == null) return false;   // EssentialsX未安装

        IUser user = essentials.getUser(player);
        return user.isAfk();
    }

}
