package com.willSleep;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.awt.*;
import java.util.concurrent.TimeUnit;

// TODO: 写完“排除列表”功能
// TODO: 在RewardTask.java的run函数中添加新的一天开始的检测

public final class PointsKeepInventory extends JavaPlugin {

    public RewardTask rewardTask;

    public int price;   // 在线时长和积分的等价交换系数
    public int listen_frequency;   // 周期侦听延迟(分钟)
    public int  points_per_keep_inventory;   // 每次死亡不掉落消耗的积分数
    public boolean keepLevel;   // 保留等级
    public boolean default_status;   // 是否开启死亡不掉落的缺省值
    public boolean isExcludeAfk;   // 是否排除afk玩家
    public boolean isEnableExclusionList;   // 是否开启排除列表
    public boolean isEnableDailyLimit;   // 是否开启每日积分上限
    public int dailyLimit;

    public dataManager dataManager;

    public void initializeConfig() {
        // 获取配置文件内容
        price = this.getConfig().getInt("economic-system.price");
        listen_frequency = this.getConfig().getInt("economic-system.listen-frequency");
        points_per_keep_inventory = this.getConfig().getInt("economic-system.points-per-keep-inventory");
        keepLevel = this.getConfig().getBoolean("item-type.keepLevel");
        default_status = this.getConfig().getBoolean("others.default-status");
        isExcludeAfk = this.getConfig().getBoolean("others.exclude-afk");
        isEnableExclusionList = this.getConfig().getBoolean("others.exclusion-list.enable");
        isEnableExclusionList = this.getConfig().getBoolean("economic-system.daily-limit.enable");
        isEnableDailyLimit = this.getConfig().getBoolean("economic-system.daily-limit.enable");
        dailyLimit = this.getConfig().getInt("economic-system.daily-limit.limit");
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

        // 周期性运行业务逻辑
        rewardTask = new RewardTask(this);
        rewardTask.isFirstRule = true;
        rewardTask.start();

        // 为指令注册执行
        getServer().getPluginCommand("pki-manage").setExecutor(new CommandManager(this));
        getServer().getPluginCommand("pki").setExecutor(new CommandManager(this));

        getLogger().info("积分死亡不掉落已启动.");

    }

    @Override
    public void onDisable() {
       if (rewardTask != null) {
           rewardTask.cancel();
       }

        getLogger().info("积分死亡不掉落已禁用.");

    }

}
