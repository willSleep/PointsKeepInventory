package com.willSleep;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class RewardTask {
    private BukkitTask task;
    private final PointsKeepInventory plugin;

    public RewardTask(PointsKeepInventory plugin) {
        this.plugin = plugin;

    }

    // 启动/重启任务
    public void start() {
        cancel();   // 取消旧任务
        task = Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::run,
                0L,
                20L * 60 * plugin.listen_frequency);   // 没有初始延迟, 20tick * sec
    }

    // 主业务逻辑
    private void run() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (plugin.excludeAfk && ExternalAPI.isPlayerAFK(player)) continue;

            plugin.dataManager.addOnlineMinutes(player, plugin.listen_frequency);
            if (plugin.dataManager.getOnlineMinutes(player) >= plugin.price) {
                int earnedPoints = plugin.dataManager.getOnlineMinutes(player) / plugin.price;   // 兑换
                if (earnedPoints > 0) {
                    plugin.dataManager.addPoints(player, earnedPoints);   // 加分
                    plugin.dataManager.setOnlineMinutes(
                            player,
                            plugin.dataManager.getOnlineMinutes(player) % plugin.price
                    );

                    player.sendMessage(String.format(
                            "§a[PKI]+%d 积分 (余额: %d)",
                            earnedPoints,
                            plugin.dataManager.getPoints(player)
                    ));
                }
            }

        }
    }

    // 安全取消任务
    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

}
