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
            p.sendMessage(ChatColor.GRAY + "pki指令集帮助(暂时啥都木有)");
            // TODO: 写完帮助
        }

        if (args.length == 1 && args[0].equals("enable")) {
            plugin.dataManager.setPKStatus(p, true);
            p.sendMessage(String.format(
                    "§a[PKI]死亡不掉落已启用 (剩余积分: %d)",
                    plugin.dataManager.getPoints(p)
            ));
        }

        if (args.length == 1 && args[0].equals("disable")) {
            plugin.dataManager.setPKStatus(p, false);
            p.sendMessage("§a[PKI]死亡不掉落已禁用");
        }

        return true;
    }

    // FIXME: 主要问题是startRewardTask()创建的对象并没有被正确重载，导致了一些问题
    private boolean executePKIM(CommandSender sender, String[] args) {
        if (args.length == 0 || (args.length == 1 && args[0].equals("help"))) {
            Player p = (Player) sender;
            p.sendMessage(ChatColor.GRAY + "pki-manage指令集帮助(暂时啥都木有)");
            // TODO: 写完帮助
        }

        if (args.length == 1 && args[0].equals("reload")) {
//            plugin.reloadConfig();
//            plugin.initializeConfig();
//            Player p = (Player) sender;
//            p.sendMessage(ChatColor.GREEN + "[PKIM]重新加载配置文件: Done.");
            Player p = (Player) sender;
            p.sendMessage(ChatColor.RED + "在此版本的PKI上, 这个功能暂时不可用.");

        }

        return true;
    }

}
