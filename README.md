# PointsKeepInventory (PKI)

[![GitHub License](https://img.shields.io/github/license/willSleep/PointsKeepInventory)](LICENSE)
[![SpigotMC API](https://img.shields.io/badge/Paper-1.20.1-blue)](https://papermc.io)

一个用于 Minecraft Paper 服务端的插件，实现积分死亡不掉落功能。

## 安装
1. 下载对应版本的构建
2. 将 `.jar` 文件放入服务器 `plugins/` 目录
3. 重启服务器

## 依赖
本插件没有任何必要依赖。

如果打开了配置文件中others.exclude-afk则需要安装Essentials插件。若打开该项但没有安装Essentials，它将被当作false处理。

手动编译时请将CompileOnly的内容放入libs/下。

## 配置说明
配置文件位于 plugins/PointsKeepInventory/config.yml，按照注释进行配置即可。

## License
MIT License © 2025 [willSleep] (2982185926@qq.com)
