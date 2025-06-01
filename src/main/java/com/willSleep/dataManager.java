package com.willSleep;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 使用PDC管理积分存储
 */
public class dataManager {

    private final PointsKeepInventory plugin;
    private final NamespacedKey pointsKey;   // 用于标识数据唯一性
    private final NamespacedKey onlineMinutesKey;   // 在线时长
    private final NamespacedKey lastRewardTimeKey;   // 上一次获得奖励时间
    private final NamespacedKey statusKey;   // 是否开启死亡不掉落状态
    private final NamespacedKey todayEarnedPointsKey;   // 今日获得的奖励

    public dataManager(PointsKeepInventory plugin) {
        this.plugin = plugin;
        this.pointsKey = new NamespacedKey(plugin, "points");   // 使用插件id作为命名空间
        this.onlineMinutesKey = new NamespacedKey(plugin, "onlineMinutes");
        this.lastRewardTimeKey = new NamespacedKey(plugin, "lastRewardTime");
        this.statusKey = new NamespacedKey(plugin, "status");
        this.todayEarnedPointsKey = new NamespacedKey(plugin, "todayEarnedPoints");
    }

    /**
     * 获取玩家的积分数
     * @param player 玩家对象
     * @return 玩家积分数
     */
    public int getPoints(Player player) {
        // 如果未存储过数据,默认返回0
        return player.getPersistentDataContainer().getOrDefault(
                pointsKey,
                PersistentDataType.INTEGER,
                0
        );
    }

    /**
     * 为玩家积分数添加一个增量
     * @param player 玩家对象
     * @param amount 增量
     */
    public void addPoints(Player player, int amount) {
        int currentPoints = getPoints(player);
        player.getPersistentDataContainer().set(
                pointsKey,
                PersistentDataType.INTEGER,
                currentPoints + amount
        );
    }

    /**
     * 获取玩家在线时长
     * @param player 玩家对象
     * @return 玩家在线时长(minutes)
     */
    public int getOnlineMinutes(Player player) {
        return player.getPersistentDataContainer().getOrDefault(
                onlineMinutesKey,
                PersistentDataType.INTEGER,
                0
        );
    }

    /**
     * 增加玩家在线时长
     * @param player 玩家对象
     * @param amount 增量(minutes)
     */
    public void addOnlineMinutes(Player player, int amount) {
        int currentOnlineMinutes = getOnlineMinutes(player);
        setOnlineMinutes(player, currentOnlineMinutes + amount);
    }

    /**
     * 设置玩家在线时长
     * @param player 玩家对象
     * @param amount 目标数量
     */
    public void setOnlineMinutes(Player player, int amount) {
        player.getPersistentDataContainer().set(
                onlineMinutesKey,
                PersistentDataType.INTEGER,
                amount
        );
    }

    /** 更新玩家获得奖励时间
     * @param player 玩家对象
     */
    public void updateLastRewardTime(Player player) {
        player.getPersistentDataContainer().set(
                lastRewardTimeKey,
                PersistentDataType.LONG,
                System.currentTimeMillis()
        );
    }

    /**
     * 获取上次获得奖励的时间戳(ms)
     * @param player 玩家对象
     * @return 上次登录时间戳
     */
    public long getLastRewardTime(Player player) {
        return player.getPersistentDataContainer().getOrDefault(
                lastRewardTimeKey,
                PersistentDataType.LONG,
                System.currentTimeMillis()   // 当前时间(ms)
        );
    }

    /**
     * 获取是否启用死亡不掉落
     * @param player 玩家对象
     * @return 布尔值
     */
    public boolean getPKStatus(Player player) {
        return player.getPersistentDataContainer().getOrDefault(
                statusKey,
                PersistentDataType.BOOLEAN,
                plugin.default_status
        );
    }

    /**
     * 设置玩家状态
     * @param player 玩家对象
     * @param value 修改后的值
     */
    public void setPKIStatus(Player player, boolean value) {
        player.getPersistentDataContainer().set(
                statusKey,
                PersistentDataType.BOOLEAN,
                value
        );
    }

    /**
     * 初始化排除列表
     */
    public void initExclusionList() {
        File file = new File(plugin.getDataFolder(), "exclusion_list.yml");

        // 若文件已经存在则忽略
        if (file.exists()) {
            return;
        }

        try {
            // 确保数据目录存在
            if(!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            // 创建文件
            if(file.createNewFile()) {
                // 写入初始内容
                try(FileWriter writer = new FileWriter(file)) {
                    writer.write("# 排除列表(依据玩家名)\n");
                    writer.write("exclusion-list: \n");
                }

                plugin.getLogger().info("成功创建 exclusion_list.yml");
            }
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建 exclusion_list.yml: " + e.getMessage());
        }
    }

    /**
     * 判断玩家对象是否在排除列表中
     * @param player 玩家对象
     * @return 是否在排除列表中
     */
    public boolean isInExclusionList(Player player) {
        return false;   // TODO: 写完
    }

    /**
     * 获取玩家今日获得的奖励
     * @param player 玩家对象
     * @return 玩家今日获得的奖励
     */
    public int getTodayEarned(Player player) {
        return player.getPersistentDataContainer().getOrDefault(
                todayEarnedPointsKey,
                PersistentDataType.INTEGER,
                0
        );
    }

    /**
     * 增加玩家今日获得的奖励
     * @param player 玩家对象
     * @param amount 增量
     */
    public void addTodayEarned(Player player, int amount) {
        int currentTodayEarned = getTodayEarned(player);
        player.getPersistentDataContainer().set(
                todayEarnedPointsKey,
                PersistentDataType.INTEGER,
                currentTodayEarned + amount
        );
    }

    /**
     * 设置玩家J今日在线时长
     * @param player 玩家对象
     * @param amount 目标量
     */
    public void setTodayEarned(Player player, int amount) {
        player.getPersistentDataContainer().set(
                todayEarnedPointsKey,
                PersistentDataType.INTEGER,
                amount
        );
    }

}
