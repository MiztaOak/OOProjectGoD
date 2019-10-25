package com.god.kahit.networkManager.callbacks;

import androidx.annotation.NonNull;

/**
 * Interface used to create callback methods for each of the packets that the client expects
 * <p>
 * used by: PacketHandler, Repository
 *
 * @author Mats Cedervall
 */
public interface HostEventCallback {

    void onReceivedMyConnectionId(@NonNull String playerId);

    void onPlayerNameChangeEvent(@NonNull String targetPlayerId,
                                 @NonNull String newName);

    void onPlayerReadyChangeEvent(@NonNull String targetPlayerId,
                                  boolean newState);

    void onTeamNameChangeEvent(@NonNull String teamId,
                               @NonNull String newTeamName);

    void onPlayerJoinedEvent(@NonNull String playerId,
                             @NonNull String playerName);

    void onPlayerLeftEvent(@NonNull String playerId);

    void onPlayerChangeTeamEvent(@NonNull String targetPlayerId,
                                 @NonNull String newTeamId);

    void onTeamCreatedEvent(@NonNull String newTeamId,
                            @NonNull String newTeamName);

    void onTeamDeletedEvent(@NonNull String teamId);

    void onGameStartedEvent();

    void onLobbySyncStartEvent(@NonNull String targetPlayerId,
                               @NonNull String roomName,
                               @NonNull String gameModeId);

    void onLobbySyncEndEvent();

    void onShowQuestionEvent(@NonNull String categoryId,
                             @NonNull String questionId);

    void onShowRoundStatsEvent();

    void onShowCategorySelectionEvent(@NonNull String[] categoryIds);

    void onShowLotteryEvent(@NonNull String[][] playersWonItemsMatrix);

    void onShowGameResultsEvent();

    void onCategoryPlayerVoteEvent(@NonNull String targetPlayerId,
                                   @NonNull String categoryId);

    void onCategoryVoteResultEvent(@NonNull String categoryId);

    void onPlayerAnsweredQuestionEvent(@NonNull String targetPlayerId,
                                       @NonNull String categoryId,
                                       @NonNull String questionId,
                                       @NonNull String givenAnswer,
                                       long timeLeft);
}
