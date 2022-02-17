![Logo](./image/tritonelogo.jpg)

# 🔉TRITONE
**Minecraft proximity voice chat** made possible with the [discord-game-sdk](https://discord.com/developers/docs/game-sdk/sdk-starter-guide).

Download: [Windows 1.16.5](https://github.com/Iroom-gbs/tritone/releases/download/beta/tritone-beta.zip)

**This mod is currently in beta! There may be some bugs.**

## ✔ Why use this?
* Tritone **does not** require any configuration on the server side. so, you can use Tritone on any public server in Minecraft.
* Discord's voice chat has better sound quality than other mods.
* Setting up this mod is very, very easy.

## ✖ Why not use this?
* There are other Minecraft prioximity voice chat modes.
* Due to the limitations of Discord, 3D sound is not supported.

## ❓How to use?

### 🚀 Installation
This mod currently only supports Windows Forge 1.16.5. (Linux soon. macOS one day)

Just unzip the [release](https://github.com/Iroom-gbs/tritone/releases) file on your minecraft mods directory.

**You must be logged-in to Discord on your computer.**
  
### ✏ Basic Use
After installing this mod and entering the multiplayer server, you will automatically join the prioximity voice chat.<br>

* 'I' key makes you inaudible.
* 'M' key for mute yourself.

### ✒ Advanced Use
You can customise the mod's discord `applicationID` and default `lobbyname` via editing `config.json`

This prevents voice chatting with unwanted people.
* clientID : Client ID issued by Discord developer site
* lobbyname : An alternative for those who are bothered by getting a clientID

**It must have the same clientID and same lobbyname as the person you want to talk to.**

## 🚩Roadmap

### InProgress
* GUI
* Forge 1.12 support
* Linux support

### Planned
* Forge 1.18 support
* Separation of lobbies by server
* MacOS support
* Multiple language support

### Someday...
* Fabric support
* Forge 1.19+ support
   
## For development...
 * We used Rust JNI for on-JVM use of discord-game-sdk.
 * Before building this project, you need some setup...
 * [https://github.com/ldesgoui/discord_game_sdk](https://github.com/ldesgoui/discord_game_sdk)
