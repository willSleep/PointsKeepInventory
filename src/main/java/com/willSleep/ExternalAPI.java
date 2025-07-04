package com.willSleep;

import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * 调用外部API
 */
public class ExternalAPI {
    public static boolean isPlayerAFK(Player player, JavaPlugin plugin) {
        try {
            IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
            IUser user = essentials.getUser(player);
            return user.isAfk();
        } catch (Exception e) {
            plugin.getLogger().info("[PKI] exclude-afk项已开启, 但无法调用essentialsx. 请检查是否已安装essentialsx插件, 或关闭exclude-afk项");
        }
        return false;
    }

}
