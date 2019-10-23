package com.god.kahit.networkManager.Callbacks;

public abstract class HostEventCallback {
    public HostEventCallback() {
    }

    public abstract void onReceivedMyConnectionId( String playerId);

    public abstract void onPlayerNameChangeEvent( String targetPlayerId,
                                                  String newName);

    public abstract void onPlayerReadyChangeEvent( String targetPlayerId,
                                                  boolean newState);

    public abstract void onTeamNameChangeEvent( String teamId,
                                                String newTeamName);

    public abstract void onPlayerJoinedEvent( String playerId,
                                              String playerName);

    public abstract void onPlayerLeftEvent( String playerId);

    public abstract void onPlayerChangeTeamEvent( String targetPlayerId,
                                                  String newTeamId);

    public abstract void onTeamCreatedEvent( String newTeamId,
                                             String newTeamName);

    public abstract void onTeamDeletedEvent( String teamId);

    public abstract void onGameStartedEvent();

    public abstract void onLobbySyncStartEvent( String targetPlayerId,
                                                String roomName,
                                                String gameModeId);

    public abstract void onLobbySyncEndEvent();

    public abstract void onShowQuestionEvent( String categoryId,
                                              String questionId);

    public abstract void onShowRoundStatsEvent();

    public abstract void onShowCategorySelectionEvent( String[] categoryIds);

    public abstract void onShowLotteryEvent( String[][] playersWonItemsMatrix);

    public abstract void onShowGameResultsEvent();

    public abstract void onCategoryPlayerVoteEvent( String targetPlayerId,
                                                    String categoryId);

    public abstract void onCategoryVoteResultEvent( String categoryId);

    public abstract void onPlayerAnsweredQuestionEvent( String targetPlayerId,
                                                        String categoryId,
                                                        String questionId,
                                                        String givenAnswer,
                                                       long timeLeft);
}
