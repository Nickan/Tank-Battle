An alpha stage of Battle City clone multiplayer!

Tested working by opening to games on same laptop. Create a server then
start the game, then join and enter ip 127.0.0.1 on client. The runnable jar
is in the TankBattle-desktop/bin. Later on Android, as I want to play it with
my brothers!:D

Server: - Has the update (collision and entity state)
		- Processes the clients request through MessagingSystem
		- Currently has basic bullet collision and block destruction
			functionality.
		- As of now, only positions and directions of entities are sent
			to client
			
Client: - No real major update, just getting updates from server and minor
			updates just to mimic or create the illusion of having its own
			major update (like animation update)
		- Sends request from server.
		

Powered by: 
LibGDX: www.libgdx.com and 
Kryonet: https://github.com/EsotericSoftware/kryonet
Many thanks to Libgdx community specially to Mr. Zechner and and Mr. Sweet.


Last note:
	Only for educational purposes, the original game idea are all from Namco.
http://en.wikipedia.org/wiki/Battle_City_(video_game).
