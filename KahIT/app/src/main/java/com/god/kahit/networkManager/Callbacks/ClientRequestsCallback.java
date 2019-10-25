package com.god.kahit.networkManager.Callbacks;

import androidx.annotation.NonNull;

/**
 * Interface used to create callback methods for each of the packets that the host expects
 * <p>
 * used by: PacketHandler, Repository
 *
 * @author Mats Cedervall
 */
public interface ClientRequestsCallback {

    void onReceivedMyConnectionId(@NonNull String senderId,
                                  @NonNull String playerId);

    void onPlayerNameChangeRequest(@NonNull String targetPlayerId,
                                   @NonNull String newName);

    void onPlayerReadyChangeRequest(@NonNull String targetPlayerId,
                                    boolean newState);

    void onTeamNameChangeRequest(@NonNull String teamId,
                                 @NonNull String newTeamName);

    void onPlayerTeamChangeRequest(@NonNull String targetPlayerId,
                                   @NonNull String newTeamId);

    void onCategoryPlayerVoteRequest(@NonNull String targetPlayerId,
                                     @NonNull String categoryId);

    void onPlayerAnsweredQuestionRequest(@NonNull String targetPlayerId,
                                         @NonNull String categoryId,
                                         @NonNull String questionId,
                                         @NonNull String givenAnswer,
                                         long timeLeft);
}
