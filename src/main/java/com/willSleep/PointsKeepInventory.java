package com.willSleep;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.awt.*;
import java.util.concurrent.TimeUnit;

// TODO: 添加“每日积分上限”功能

public final class PointsKeepInventory extends JavaPlugin {

    private BukkitTask checkTask;

    public int price;   // 在线时长和积分的等价交换系数
    public int listen_frequency;   // 周期侦听延迟(分钟)
    public int  points_per_keep_inventory;   // 每次死亡不掉落消耗的积分数
    public boolean keepInventory;   // 保留物品栏
    public boolean keepLevel;   // 保留等级
    public boolean default_status;   // 是否开启死亡不掉落的缺省值
    public boolean excludeAfk;

    public dataManager dataManager;

    public void initializeConfig() {
        // 获取配置文件内容
        price = this.getConfig().getInt("economic-system.price");
        listen_frequency = this.getConfig().getInt("economic-system.listen-frequency");
        points_per_keep_inventory = this.getConfig().getInt("economic-system.points-per-keep-inventory");
        keepInventory = this.getConfig().getBoolean("item-type.keepInventory");
        keepLevel = this.getConfig().getBoolean("item-type.keepLevel");
        default_status = this.getConfig().getBoolean("others.default-status");
        excludeAfk = this.getConfig().getBoolean("others.exclude-afk");
    }


    @Override
    public void onEnable() {
        // 初始化配置文件
        initializeConfig();

        // 初始化变量
        dataManager = new dataManager(this);

        getServer().getPluginManager().registerEvents(new Listeners(this), this);

        // 生成配置文件
        saveDefaultConfig();

        // 周期性刷新数据
        startRewardTask();

        // 为指令注册执行
        getServer().getPluginCommand("pki-manage").setExecutor(new CommandManager(this));
        getServer().getPluginCommand("pki").setExecutor(new CommandManager(this));

        getLogger().info("积分死亡不掉落已启动.");

    }

    @Override
    public void onDisable() {
        if (checkTask != null) checkTask.cancel();   // 安全退出侦听任务

        getLogger().info("积分死亡不掉落已禁用.");

    }

    /**
     * 周期性刷新数据
     */
    // HACK: 后续将startRewardTask隔离到单独的类并安全重写cancel等方法
    private void startRewardTask() {
        checkTask = Bukkit.getScheduler().runTaskTimer(this,() -> {    // 使用Lambda表达式启动runTaskTimer
            for (Player player: Bukkit.getOnlinePlayers()) {
                if (excludeAfk) {
                    if (ExternalAPI.isPlayerAFK(player)) continue;
                }
                dataManager.setOnlineMinutes(player, dataManager.getOnlineMinutes(player) + listen_frequency);
                // TODO: 考虑增加一个addOnlineMinutes专门和setOnlineMinutes区别开
                if (dataManager.getOnlineMinutes(player) >= price) {
                    int earnedPoints = dataManager.getOnlineMinutes(player) / price;
                    if (earnedPoints > 0) {
                        dataManager.addPoints(player, earnedPoints);
                        dataManager.setOnlineMinutes(player,
                                dataManager.getOnlineMinutes(player) % price);

                        player.sendMessage(String.format(
                                "§a[PKI]+%d 积分 (余额: %d)",
                                earnedPoints,
                                dataManager.getPoints(player)
                                ));
                    }
                }


            }

            getLogger().info("DEBUG: UPDATE SUCCESSFULLY");
        }, 0L, 20L * 60 * listen_frequency);   // 没有初始延迟(Long类型), 20tick * 秒数
        // 批注: 后期可尝试移入多线程;
    }

    /**
     * 检查两个时间戳是否是同一天
     * @param time1 时间戳1
     * @param time2 时间戳2
     * @return 比较结果
     */
    private boolean isSameDay(long time1, long time2) {
        return TimeUnit.MILLISECONDS.toDays(time1) == TimeUnit.MILLISECONDS.toDays(time2);
    }

}
