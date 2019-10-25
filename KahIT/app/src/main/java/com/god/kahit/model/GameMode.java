package com.god.kahit.model;

/**
 * @responsibility: This class is responsible for declaring which game mode
 * a player is currently in.
 * @used-by: This class is used in the following classes:
 * CreateLobbyNetView, Repository, Quizgame, HotSwapGameModeModelView,
 * JoinLobbyNetView and playerManager
 * @author: Anas Alkoutli
 */

public enum GameMode {
    HOT_SWAP,
    HOST,
    CLIENT
}
