package com.willSleep;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class RewardTask {
    private BukkitTask task;
    private final PointsKeepInventory plugin;

    public boolean isFirstRule;   // 是否是任务被创建(或者重新创建)后的第一轮
    // 如果isFirstRule为真则忽略一次加计时(防止热重载后或者服务器重启后直接加一轮时间的现象)

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
            if (isFirstRule) {
                // 忽略一次加计时
                isFirstRule = false;
                continue;
            }

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
