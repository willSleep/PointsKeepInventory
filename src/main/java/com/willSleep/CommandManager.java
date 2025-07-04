package com.willSleep;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public class CommandManager implements CommandExecutor {
    private final PointsKeepInventory plugin;

    public CommandManager(PointsKeepInventory plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch(command.getName().toLowerCase()) {
            // 根据指令分发逻辑
            case "pki":
                return executePKI(sender, args);
            case "pki-manage":
                return executePKIM(sender, args);
            default:
                return false;
        }
    }

    private boolean executePKI(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0 || (args.length == 1 && args[0].equals("help"))) {
            p.sendMessage(ChatColor.GRAY + "pki指令集帮助");
            p.sendMessage(ChatColor.YELLOW + "enable    " + ChatColor.GREEN + "启用死亡不掉落");
            p.sendMessage(ChatColor.YELLOW + "disable   " + ChatColor.GREEN + "禁用死亡不掉落");
            p.sendMessage(ChatColor.YELLOW + "info      " + ChatColor.GREEN + "查询积分信息");
            p.sendMessage(ChatColor.YELLOW + "help      " + ChatColor.GREEN + "显示此帮助");
        }

        if (args.length == 1 && args[0].equals("enable")) {
            plugin.dataManager.setPKIStatus(p, true);
            p.sendMessage(String.format(
                    "§3[PKI]死亡不掉落已启用 (剩余积分: %d)",
                    plugin.dataManager.getPoints(p)
            ));
            return true;
        }

        if (args.length == 1 && args[0].equals("disable")) {
            plugin.dataManager.setPKIStatus(p, false);
            p.sendMessage("§3[PKI]死亡不掉落已禁用");
            return true;
        }

        if (args.length == 1 && args[0].equals("info")) {
            p.sendMessage("§8§ 积分信息 §");
            p.sendMessage(String.format(
                    "§3剩余积分: §n%d",
                    plugin.dataManager.getPoints(p)
            ));
            p.sendMessage(String.format(
                    "§3当日获取积分: §n%d",
                    plugin.dataManager.getTodayEarned(p)
            ));
            p.sendMessage(String.format(
                    "§3死亡不掉落价格: §n%d 积分/次",
                    plugin.points_per_keep_inventory
            ));
            p.sendMessage(String.format(
                    "§3死亡不掉落开启状态: §n%s",
                    plugin.dataManager.getPKStatus(p)?"开启":"关闭"
            ));
            if(plugin.dataManager.getTodayEarned(p) >= plugin.dailyLimit) {
                p.sendMessage(ChatColor.RED + "积分已达每日赚取上限");
            }
        }

        return false;
    }

    private boolean executePKIM(CommandSender sender, String[] args) {
        if (args.length == 0 || (args.length == 1 && args[0].equals("help"))) {
            sender.sendMessage(ChatColor.GRAY + "pkim指令集帮助");
            sender.sendMessage(ChatColor.YELLOW + "reload " + ChatColor.GREEN + "热重载配置文件");
        }

        if (args.length == 1 && args[0].equals("reload")) {
            plugin.reloadConfig();
            plugin.initializeConfig();
            plugin.rewardTask.start();   // 重启任务
            plugin.rewardTask.isFirstRule = true;
            sender.sendMessage(ChatColor.GREEN + "配置文件已重载 :)");

            return true;

        }

        return false;
    }

}
