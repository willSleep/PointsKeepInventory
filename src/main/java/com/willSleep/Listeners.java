package com.willSleep;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;

public class Listeners implements Listener {

    private final PointsKeepInventory plugin;

    public Listeners(PointsKeepInventory plugin) {
        this.plugin = plugin;
    }

    // 以下被注释的代码在后续可能被删除,视测试结果而定
//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent event) {
//        // TODO: 添加为新玩家初始化数据、检查日期的方法
//    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        // TODO: 添加功能: 为开启死亡不掉落但没有余额的玩家打印通知
        // TODO: 添加功能: 在余额不足时自动关闭死亡不掉落并通知玩家
        if (plugin.dataManager.getPKStatus(player) && plugin.dataManager.getPoints(player) >= plugin.points_per_keep_inventory) {
            event.setKeepInventory(true);
            if (plugin.keepLevel) event.setKeepLevel(true);
            event.getDrops().clear();
            event.setDroppedExp(0);
            plugin.dataManager.addPoints(player, -plugin.points_per_keep_inventory);

            player.sendMessage(String.format(
                    "本次死亡不掉落消耗 %d 积分, 余额: %d 积分",
                    plugin.points_per_keep_inventory,
                    plugin.dataManager.getPoints(player)
            ));
        }

    }

}
