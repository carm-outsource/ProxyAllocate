# ProxyAllocate
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/ProxyAllocate)
[![Download](https://img.shields.io/github/downloads/CarmJos/ProxyAllocate/total)](https://github.com/CarmJos/ProxyAllocate/releases)
[![Java CI with Maven](https://github.com/CarmJos/ProxyAllocate/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/ProxyAllocate/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.8--Latest-blue)

A super lightweight [Velocity](https://papermc.io/software/velocity) plugin that
supports evenly teleporting players to different 
default or forced-host servers when login.

_No configurations and commands, just a simple function implementation._

## Example

In Velocity's `velocity.toml` configuration file, like

```toml
[servers]
server1 = "127.0.0.1:1001"
server2 = "127.0.0.1:1002"
server3 = "127.0.0.1:1003"
server4 = "127.0.0.1:1004"

game1 = "127.0.0.1:2001"
game2 = "127.0.0.1:2002"
game3 = "127.0.0.1:2003"
game4 = "127.0.0.1:2004"

try = [
    "server1", "server2", "server3", "server4"
]

[forced-hosts]
"play.your-server.net" = [
    "game1", "game2", "game3", "game4"
]
```

after configured, when a player login to the proxy,

if the player's IP is `play.your-server.net`,
the player will be teleported to the server 
with the least number of players in `game1`, `game2`, `game3`, `game4`.

if there's no any forced-hosts matched,
the player will be teleported to the tried servers
with the least number of players in `server1`, `server2`, `server3`, `server4`.


## Open source agreement

The source code of this project uses  [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0)
License.


