package com.willSleep;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

/**
 * 使用PDC管理积分存储
 */
public class dataManager {

    private final PointsKeepInventory plugin;
    private final NamespacedKey pointsKey;   // 用于标识数据唯一性
    private final NamespacedKey onlineMinutesKey;
    private final NamespacedKey lastLoginTimeKey;
    private final NamespacedKey statusKey;

    public dataManager(PointsKeepInventory plugin) {
        this.plugin = plugin;
        this.pointsKey = new NamespacedKey(plugin, "points");   // 使用插件id作为命名空间
        this.onlineMinutesKey = new NamespacedKey(plugin, "onlineMinutes");
        this.lastLoginTimeKey = new NamespacedKey(plugin, "lastLoginTime");
        this.statusKey = new NamespacedKey(plugin, "status");
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
     * 设置玩家在线时常
     * @param player 玩家对象
     * @param amount 增量
     */
    public void setOnlineMinutes(Player player, int amount) {
        player.getPersistentDataContainer().set(
                onlineMinutesKey,
                PersistentDataType.INTEGER,
                amount
        );
    }

    /**
     * 获取上次登录时间戳(ms)
     * @param player 玩家对象
     * @return 上次登录时间戳
     */
    public long getLastLoginTime(Player player) {
        return player.getPersistentDataContainer().getOrDefault(
                lastLoginTimeKey,
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
     *
     * @param player 玩家对象
     * @param value 修改后的值
     */
    public void setPKStatus(Player player, boolean value) {
        player.getPersistentDataContainer().set(
                statusKey,
                PersistentDataType.BOOLEAN,
                value
        );
    }

}
