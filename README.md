# Minecraft Moderation Tools Plugin
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
![GitHub stars](https://img.shields.io/github/stars/enes-th/minecraft-moderation-tools?label=Stars)
![GitHub last commit](https://img.shields.io/github/last-commit/enes-th/minecraft-moderation-tools?label=Last%20Update)
![GitHub open issues](https://img.shields.io/github/issues/enes-th/minecraft-moderation-tools?label=Issues)

This repository contains a Minecraft plugin written in Java using Gradle that provides moderation tools for Minecraft server administrators. The plugin is compatible with version 1.19+ of Minecraft servers.

## Features
* Moderation mode for staff members with the following features: 
  - Freeze a player
  - View inventory of a player
  - Test player knockback
  - Teleport to a random player
  - Activate/desactivate vanish mode
* Commands:
  - `/report <check | [player]> <player>` - Report a player
  - `/moderator` - Enter moderator mode
  - `/player <check | purge> <player>` - Watch/Purge a player's information
  - `/ban <check | [player]> <[time]> <[reason]>` - Ban a player from the server
  - `/mute <check | [player]> <[time]> <[reason]>` - Mute a player, preventing them from chatting
  - `/kick <player>` - Kick a player from the server
  - `/unban <player>` - Unban a player, allowing them to join the server again
  - `/unmute <player>` - Unmute a player, allowing them to chat again
  - `/freeze <player>` - Freeze a player, preventing them from moving or taking any action

## Installation
To install the plugin, follow these steps:
1. Download the plugin jar file from the `releases` section.
2. Copy the jar file into the `plugins` folder of your Minecraft server.
3. Start the server.

## License
This plugin is licensed under the MIT License. See the LICENSE file for details.
