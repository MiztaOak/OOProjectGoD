package com.god.kahit.model;

import com.god.kahit.model.modelEvents.LotteryDrawEvent;
import com.god.kahit.model.modelEvents.QuestionEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @responsibility: This class is a aggregate class for game play specific classes. Basically
 * handles everything model related apart from players and teams.
 * @used-by: This class is used in the following classes:
 * Repository
 * @author: Anas Alkoutli, Johan Ek, Oussama Anadani, Jakob Ewerstrand, Mats Cedervall
 */

public class QuizGame {
    private IEventBus eventBus;
    private PlayerManager playerManager;
    private GameMode gameMode;

    private Map<Category, List<Question>> questionMap;
    private Map<Category, List<Integer>> indexMap;
    private Deque<Question> roundQuestions;
    private int numOfQuestions;
    private Category currentCategory;
    private Category[] categorySelectionArray; //todo find a better way

    private Store store;
    private Lottery lottery;

    private boolean gameIsStarted;

    public QuizGame(IEventBus eventBus, PlayerManager playerManager, GameMode gameMode) {
        this.eventBus = eventBus;
        this.playerManager = playerManager;
        this.gameMode = gameMode;

        numOfQuestions = 2; //TODO replace with more "dynamic" way to set this
        gameIsStarted = false;
    }

    public void startGame() {
        if (!gameIsStarted) {
            questionMap = QuestionFactory.getFullQuestionMap();
            indexMap = new HashMap<>();
            currentCategory = Category.Mix;
            loadIndexMap();

            store = new Store(eventBus);
            lottery = new Lottery();
            gameIsStarted = true;
        }
    }

    public void endGame() {
        gameIsStarted = false;
    }

    public boolean hasGameStarted() {
        return gameIsStarted;
    }

    /**
     * Method that fills the map of questionIndexes that is used to determine the order in which questions
     * are asked
     */
    private void loadIndexMap() {
        for (Category category : questionMap.keySet()) {
            loadIndexList(category);
        }
    }

    /**
     * Loads the index list for a single category which is then incorporated into the map
     *
     * @param category - the category serves as the key for the list in the map and is used to get the correct amount of indexes in the list
     */
    private void loadIndexList(Category category) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(questionMap.get(category)).size(); i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        indexMap.put(category, indexes);
    }

    /**
     * Method that start a round of the game. It loads the questions that will be asked during the round
     * based on the current category
     */
    public void startRound() {
        roundQuestions = new ArrayDeque<>();
        if (currentCategory != Category.Mix) {
            loadRoundQuestion();
        } else {
            loadMixQuestions();
        }
    }

    /**
     * Method that loads questions from all categories and puts them into the que
     */
    private void loadMixQuestions() {
        int i = 0;
        List<Category> categories = new ArrayList<>(questionMap.keySet());
        Collections.shuffle(categories);
        while (i < numOfQuestions) {
            for (Category category : categories) {
                addQuestion(category);
                i++;
                if (i == numOfQuestions) {
                    break;
                }
            }
        }
    }

    /**
     * Method that load questions into the que based on the current category
     */
    private void loadRoundQuestion() {
        for (int i = 0; i < numOfQuestions; i++) {
            addQuestion(currentCategory);
        }
    }

    /**
     * Method that adds a question to the round que based on the category given and the order defined
     * in the index map. If there are no more indexes of a given category it refills the index map
     * and then gets a new question based on the new order.
     *
     * @param category the category of the added question
     */
    private void addQuestion(Category category) {
        if (Objects.requireNonNull(indexMap.get(category)).size() == 0) {
            loadIndexList(category);
        }

        List<Integer> indexList = indexMap.get(category);
        List<Question> questionList = questionMap.get(category);
        if (indexList != null && questionList != null) {
            if (indexList.size() > 0) {
                int indexOfQuestion = indexList.get(0);
                roundQuestions.add(questionList.get(indexOfQuestion));
                indexList.remove(0);
            } else {
                System.out.println("Quizgame - addQuestion: indexList is empty");
            }

        } else {
            System.out.println("Quizgame - addQuestion: Either indexMap or QuestionMap are null");
        }
    }

    /**
     * Method that returns a new question if there is questions left in the question que otherwise it
     * starts a new round
     */
    public void nextQuestion() {
        if (!roundQuestions.isEmpty()) {
            broadCastQuestion(roundQuestions.pop());
        } else {
            startRound(); //TODO is this expected?
        }
    }

    public String getNextQuestionCategoryId() {
        if (!roundQuestions.isEmpty()) {
            return roundQuestions.peek().getCategory().getId();
        } else {
            startRound(); //TODO is this expected?
            return roundQuestions.peek().getCategory().getId();
        }
    }

    public String getNextQuestionId() {
        if (!roundQuestions.isEmpty()) {
            return Integer.toString(getQuestionIndex(roundQuestions.peek().getCategory(), roundQuestions.peek()));
        } else {
            startRound(); //TODO is this expected?
            return Integer.toString(getQuestionIndex(roundQuestions.peek().getCategory(), roundQuestions.peek()));
        }
    }

    public void setNextQuestion(String categoryId, String questionIndex) {
        Category category = Category.getCategoryById(categoryId);
        setCurrentCategory(categoryId);
        Question question = getQuestion(category, Integer.valueOf(questionIndex));
        roundQuestions.addFirst(question);
    }

    public int getQuestionIndex(Category category, Question question) {
        List<Question> questionList = questionMap.get(category);
        if (questionList == null) {
            System.out.println("Quizgame - getQuestionIndex: questionList == null, unable to " +
                    "find sought question. returning -1.");
            return -1;
        }

        return questionList.indexOf(question);
    }

    public Question getQuestion(Category category, int questionIndex) {
        List<Question> questionList = questionMap.get(category);
        if (questionList == null) {
            System.out.println(String.format("Quizgame - getQuestion: questionList == null, unable to " +
                    "return sought question. category.getId(): '%s', questionIndex: '%s'. returning null.", category.getId(), questionIndex));
            return null;
        }

        if (questionList.size() < questionIndex) {
            System.out.println(String.format("Quizgame - getQuestion: questionList.size < questionIndex, unable to " +
                    "return sought question. category.getId(): '%s', questionIndex: '%s'. returning null.", category.getId(), questionIndex));
            return null;
        }

        return questionList.get(questionIndex);
    }

    /**
     * Method that broadcasts the current question and the number of time it should be repeated
     * to the event bus
     *
     * @param question the question that is being broadcast
     */
    private void broadCastQuestion(Question question) {
        if (gameMode.equals(GameMode.HOT_SWAP)) {
            eventBus.post(new QuestionEvent(question, playerManager.getTotalAmountOfPlayers()));
        } else {
            eventBus.post(new QuestionEvent(question, 1));
        }
    }

    /**
     * Method called from the outside of the model to report the given answer on a question
     * It calls a method that updates the score of a player and then clears the modifier of a player
     *
     * @param givenAnswer the alternative that the user choose to provide
     * @param question    - the question that was asked
     * @param timeLeft    - the time that was left when the user answered the question
     */
    public void enterAnswer(Player player, String givenAnswer, Question question, long timeLeft) {
        if (question.isCorrectAnswer(givenAnswer)) {
            player.updateScore(timeLeft, question.getTime());
        }
        player.clearModifier();
    }

    /**
     * Method that returns true if the round is over
     *
     * @return if the questions stack is empty
     */
    public boolean isRoundOver() {
        if (roundQuestions == null) {
            return true;
        }
        return roundQuestions.isEmpty();
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String categoryId) {
        Category category = getCategory(categoryId);
        if (category != null) {
            setCurrentCategory(category);
        } else {
            System.out.println("Quizgame - setCurrentCategory: found no match to categoryId, " +
                    "unable to set current category, skipping call");
        }
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    private Category getCategory(String categoryId) {
        for (Category category : Category.values()) {
            if (category.getId().equals(categoryId)) {
                return category;
            }
        }
        System.out.println("Quizgame - getCategory: found no match to categoryId, returning null");
        return null;
    }

    public void generateRandomCategoryArray(int arraySize) {
        List<Category> categories = new ArrayList<>(Category.getRealCategories());
        categories.remove(currentCategory);
        Collections.shuffle(categories);
        categories = categories.subList(0, arraySize);
        categorySelectionArray = categories.toArray(new Category[0]);
    }

    public Category[] getCategorySelectionArray() {
        return categorySelectionArray;
    }

    public void setCategorySelectionArray(String[] categoryIdSelectionArray) {
        Category[] categories = new Category[categoryIdSelectionArray.length];
        for (int i = 0; i < categoryIdSelectionArray.length; i++) {
            categories[i] = getCategory(categoryIdSelectionArray[i]);
        }

        categorySelectionArray = categories;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public void drawLottery() {
        Map<Player, Item> winnings = lottery.drawItem(playerManager.getPlayers());
        eventBus.post(new LotteryDrawEvent(winnings));
    }

    /**
     * Method returns all items available in the game.
     *
     * @return
     */
    public List<Item> getAllItems() {
        return lottery.getItemList();
    }

    public boolean isStoreItemBuyable(int itemIndex, Player player) {
        return store.isItemBuyable(itemIndex, player);
    }

    public void buyItem(int itemIndex, Player player) {
        store.buyItem(itemIndex, player);
    }

    public boolean isStoreItemBought(int itemIndex) {
        return store.isItemBought(itemIndex);
    }

    public Item getStoreItem(int itemIndex) {
        return store.getStoreItems().get(itemIndex);
    }

    public boolean isHotSwap() {
        return gameMode.equals(GameMode.HOT_SWAP);
    }

    public Store getStore() {
        return store;
    }

    public Lottery getLottery() {
        return lottery;
    }
    public boolean isGameIsStarted() {
        return gameIsStarted;
    }
}
