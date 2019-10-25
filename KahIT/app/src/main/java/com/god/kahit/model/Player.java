package com.god.kahit.model;

/**
 * @responsibility: This class is responsible for the player.
 * It holds all the information a player needs such as score and name.
 * It holds information that the host and network manager needs such as id and ready state.
 * @used-by: This class is used in the following classes:
 * QuizGame, PlayerManager, Repository, AfterQuestionScorePageViewModel, CategoryViewModel,
 * HotSwapAddPlayersView, HotSwapAddPlayersViewModel, HotSwapRecyclerAdapter,
 * LobbyNetRecyclerAdapter, LobbyNetView, LobbyNetViewModel, LotteryDrawEvent LotteryView,
 * QuestionViewModel, ScorePageAdapter, Team and TeamContainerRecyclerAdapter
 * @author: Anas Alkoutli, Johan Ek, Oussama Anadani, Jakob Ewerstrand, Mats Cedervall
 */
public class Player { //todo revise with better use of access-modifiers. e.i not public everywhere
    private String id;
    private String name;
    private int score;
    private double scoreMultiplier = 1;
    private int amountOfTime;
    private int amountOfAlternatives;
    private boolean autoAnswer;
    private VanityItem vanityItem;
    private boolean isReady; //TODO check if this really is needed in the model since it should prob be in lobby


    public Player(String name, String id) {
        this.name = name;
        this.id = id;
        this.score = 0;
        this.isReady = false;
    }


    /**
     * A method that sets the values of effects of a Buff to players own values
     *
     * @param buff: Which buff to be set to the player.
     */
    public void setBuff(Buff buff) {
        scoreMultiplier = buff.getScoreMultiplier() * scoreMultiplier;
        amountOfTime = buff.getAmountOfTime() + amountOfTime;
        this.amountOfAlternatives = buff.getAmountOfAlternatives();
    }

    /**
     * A method that sets the values of effects of a Debuff to players own values
     *
     * @param debuff: Which buff to be set to the player.
     */
    public void setDebuff(Debuff debuff) {
        scoreMultiplier = debuff.getScoreMultiplier() * scoreMultiplier;
        amountOfTime = debuff.getAmountOfTime() + amountOfTime;
        autoAnswer = debuff.getAutoAnswer();
    }

    /**
     * A method that clears the effect of a Modifier after  it has been used
     */
    void clearModifier() {
        this.scoreMultiplier = 1;
        this.amountOfTime = 0;
        this.amountOfAlternatives = 0;
        this.autoAnswer = false;
    }

    /**
     * A method that calculates the new score after a player has answered a question.
     * Each question can give 500 points as maximum points. This method calculates if the player
     * has buff which give extra time or extra points.
     *
     * @param time:         How mush time it took the player to answer.
     * @param questionTime: How much time each question has.
     */
    void updateScore(long time, int questionTime) {
        double playerTime = (((double)time) +((double)amountOfTime))/((double)questionTime);
        if (playerTime > 1){ //Prevents the player from getting more score that they should had they answered instantly
            playerTime = (double) 1;
        }
        this.score += (500 * (playerTime) * scoreMultiplier);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public int getAmountOfTime() {
        return amountOfTime;
    }

    public void setAmountOfTime(int amountOfTime) {
        this.amountOfTime = amountOfTime;
    }

    public int getAmountOfAlternatives() {
        return amountOfAlternatives;
    }

    public void setAmountOfAlternatives(int amountOfAlternatives) {
        this.amountOfAlternatives = amountOfAlternatives;
    }

    public boolean isAutoAnswer() {
        return autoAnswer;
    }

    public void setAutoAnswer(boolean autoAnswer) {
        this.autoAnswer = autoAnswer;
    }

    public VanityItem getVanityItem() {
        return vanityItem;
    }

    public void setVanityItem(VanityItem vanityItem) {
        this.vanityItem = vanityItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


