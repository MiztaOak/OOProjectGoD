### OOProjectGoD TDA367 - Objektsorienterat programmeringsprojekt
<p align="center">
	<img src="https://raw.githubusercontent.com/MiztaOak/OOProjectGoD/master/Resources/KahIT-logo-vanligSmurf.png" alt="Harmony" width="512" />
</p>

<p align="center">
  <a href="../../issues">
    <img src="https://img.shields.io/github/issues/MiztaOak/OOProjectGoD"/>
  </a>
  <a href="../../pulls">
    <img src="https://img.shields.io/github/issues-pr/MiztaOak/OOProjectGoD"/>
  </a>
  <a href="../../pulls">
    <img src="https://img.shields.io/github/issues-pr-closed/MiztaOak/OOProjectGoD"/>
  </a>
</p>

<p align="center">
	<img src="https://img.shields.io/travis/MiztaOak/OOProjectGoD/master?label=build%20master"/>
	<img src="https://img.shields.io/travis/MiztaOak/OOProjectGoD/dev?label=build%20dev"/>
</p>

<p align="center">
  <a href="../../releases">
    <img src="https://img.shields.io/github/release/MiztaOak/OOProjectGoD"/>
  </a>
</p>

<p align="center">
  GitInspector report - 
	<a href="https://miztaoak.github.io/OOProjectGoD/">
		dev
	</a>
</p>

<p align="center">
  JavaDoc - 
	<a href="https://miztaoak.github.io/OOProjectGoD/JavaDoc/">
		dev
	</a>
</p>

#### How to use
Due to the size of the app one must increase the amount of memory Android Studio can use.
This is because of the large amount of images and soundtracks that the app holds.

##### How to increase Android Studio memory
Goto Android studio -> Help -> Edit Custom VM Options...
Add the following to the file:

`-Xms128m
-Xmx4096m
-XX:MaxPermSize=1024m
-XX:ReservedCodeCacheSize=200m
-XX:+UseCompressedOops`

Restart Android Studio.

#### About
KahIt is a quiz-game app with a twist that was developed by 5 IT students of Chalmers university during the course TDA367 Object-oriented programming project.

How to setup a game: First you need to choose between host, join or hotswap game. The game allows a maximum of 8 players.
- To Host: You have to fill in your info and create a game lobby then let other players join you.
- To Join: You have to fill in your info and join a game someone is hosting.
- To use HotSwap: You play localy so you only have to choose a game mode and then add players in the lobby.

How to play:
The game consists of rounds. A round is as follows: A vote for the next theme/category of that round. A series of questions, each followed by a quick score update. When x-amount of questions have been answered a lottery is run where random items are distributed. After x-amout of rounds the final score page will show up.
- If in HotSwap: every player has his/her turn to answer/vote then you pass on the device.
- If in multiplayer: everyone answers/votes at the same time.
- Score is based on answering right and also on speed.
- Items can either be a buff, debuff or a Vanity item. Buff -positive item, Debuff -negative item, Vanity -only cosmetic. Items can be aquired through the lottery(unlimited supply) or purchased in the store(limited supply).
- During the course of the game you have the option to spend credit(score) in the store where you can buy all items. Beware the supply is limited and shared by all players. Be quick!

Good luck and have fun!

#### Contributors
- Anas Alkoutli, @anasqw
- Oussama Anadani, @ousama123
- Mats Cedervall, @maatss
- Johan Ek, @MiztaOak
- Jakob Ewerstrand, @JakobEwerstrand
