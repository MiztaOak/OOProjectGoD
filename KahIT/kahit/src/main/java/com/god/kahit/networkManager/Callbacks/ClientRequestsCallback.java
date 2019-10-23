package com.god.kahit.networkManager.Callbacks;


public abstract class ClientRequestsCallback {
    public ClientRequestsCallback() {
    }

    public abstract void onReceivedMyConnectionId( String senderId,
                                                   String playerId);

    public abstract void onPlayerNameChangeRequest( String targetPlayerId,
                                                    String newName);

    public abstract void onPlayerReadyChangeRequest( String targetPlayerId,
                                                    boolean newState);

    public abstract void onTeamNameChangeRequest( String teamId,
                                                  String newTeamName);

    public abstract void onPlayerTeamChangeRequest( String targetPlayerId,
                                                    String newTeamId);

    public abstract void onCategoryPlayerVoteRequest( String targetPlayerId,
                                                      String categoryId);

    public abstract void onPlayerAnsweredQuestionRequest( String targetPlayerId,
                                                          String categoryId,
                                                          String questionId,
                                                          String givenAnswer,
                                                         long timeLeft);
}
