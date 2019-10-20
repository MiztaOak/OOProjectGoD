package com.god.kahit.networkManager.Callbacks;

import androidx.annotation.NonNull;

public abstract class HostEventCallback {
    public HostEventCallback() {
    }

    public abstract void onReceivedMyConnectionId(@NonNull String playerId);

    public abstract void onPlayerNameChangeEvent(@NonNull String targetPlayerId,
                                                 @NonNull String newName);

    public abstract void onPlayerReadyChangeEvent(@NonNull String targetPlayerId,
                                                  @NonNull boolean newState);

    public abstract void onTeamNameChangeEvent(@NonNull String teamId,
                                               @NonNull String newTeamName);

    public abstract void onPlayerJoinedEvent(@NonNull String playerId,
                                             @NonNull String playerName);

    public abstract void onPlayerLeftEvent(@NonNull String playerId);

    public abstract void onPlayerChangeTeamEvent(@NonNull String targetPlayerId,
                                                 @NonNull String newTeamId);

    public abstract void onTeamCreatedEvent(@NonNull String newTeamId,
                                            @NonNull String newTeamName);

    public abstract void onTeamDeletedEvent(@NonNull String teamId);

    public abstract void onGameStartedEvent();

    public abstract void onLobbySyncStartEvent(@NonNull String targetPlayerId,
                                               @NonNull String roomName,
                                               @NonNull String gameModeId);

    public abstract void onLobbySyncEndEvent();

    public abstract void onShowQuestionEvent(@NonNull String categoryId,
                                             @NonNull String questionId);

    public abstract void onShowRoundStatsEvent();

    public abstract void onShowCategorySelectionEvent(@NonNull String[] categoryIds);

    public abstract void onShowLotteryEvent(@NonNull String[][] playersWonItemsMatrix);

    public abstract void onShowGameResultsEvent();

    public abstract void onCategoryPlayerVoteEvent(@NonNull String targetPlayerId,
                                                   @NonNull String categoryId);

    public abstract void onPlayerAnsweredQuestionEvent(@NonNull String targetPlayerId,
                                                       @NonNull String categoryId,
                                                       @NonNull String questionId,
                                                       @NonNull String givenAnswer,
                                                       long timeLeft);
}
