# Minecraft Moderation Tools Plugin
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
![GitHub stars](https://img.shields.io/github/stars/enes-th/minecraft-moderation-tools?label=Stars)
![GitHub last commit](https://img.shields.io/github/last-commit/enes-th/minecraft-moderation-tools?label=Last%20Update)
![GitHub open issues](https://img.shields.io/github/issues/enes-th/minecraft-moderation-tools?label=Issues)

This repository contains a Minecraft plugin written in Java using Gradle that provides moderation tools for Minecraft server administrators. The plugin is compatible with version 1.19+ of Minecraft servers.

## Features
* Full report system that allows players to report other players with a reason.
* Moderation mode for staff members with the following features: 
  - Freeze a player
  - View inventory of a player
  - Test player knockback
  - Teleport to a random player
  - Activate/desactivate vanish mode
* Commands:
  - `/report <player> <reason>` to report a player
  - `/report-check <player>` to check the last report of a player (Staff only)
  - `/moderator` to enable moderator mode (Staff only)
  - `/player-check <player>` to check information of a user (Staff only)

## Installation
To install the plugin, follow these steps:
1. Download the plugin jar file from the `releases` section.
2. Copy the jar file into the `plugins` folder of your Minecraft server.
3. Start the server.

## Usage
Once the plugin is installed and the server is running, players can use the `/report <player> <reason>` command to report other players. Staff members can use the `/moderator` command to enable moderation mode and access the moderation tools. They can also use the `/report-check <player>` and `/player-check <player>` commands to check information about players.

## License
This plugin is licensed under the MIT License. See the LICENSE file for details.
