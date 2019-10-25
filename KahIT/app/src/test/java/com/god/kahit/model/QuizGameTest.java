package com.god.kahit.model;

import com.god.kahit.EventBussMock;
import com.god.kahit.ItemDataLoaderMock;
import com.god.kahit.QuestionDataLoaderMock;
import com.god.kahit.model.modelEvents.LotteryDrawEvent;
import com.god.kahit.model.modelEvents.QuestionEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class QuizGameTest {
    private EventBussMock eventBuss = new EventBussMock();
    private QuizGame quizGame = new QuizGame(eventBuss, new PlayerManager(eventBuss, GameMode.HOT_SWAP), GameMode.HOT_SWAP);


    @Before
    public void setUp() throws Exception {
        ItemFactory.setDataLoader(new ItemDataLoaderMock());
        QuestionFactory.setDataLoader(new QuestionDataLoaderMock());
        //IEventBus eventBus, PlayerManager playerManager, GameMode gameMode
    }

    @Test
    public void startGame() {
        assertFalse(quizGame.hasGameStarted());
        quizGame.startGame();
        assertTrue(quizGame.hasGameStarted());
        assertNotNull(quizGame.getLottery());
        assertNotNull(quizGame.getStore());
        assertEquals(Category.Mix, quizGame.getCurrentCategory());
    }

    @Test
    public void endGame() {
        quizGame.startGame();
        assertTrue(quizGame.hasGameStarted());
        quizGame.endGame();
        assertFalse(quizGame.hasGameStarted());
    }

    @Test
    public void startRound() {
        quizGame.startGame();
        assertTrue(quizGame.hasGameStarted());
        quizGame.startRound();
        assertFalse(quizGame.isRoundOver());
        quizGame.endGame();
        assertFalse(quizGame.hasGameStarted());
        quizGame.startGame();
        quizGame.setCurrentCategory(Category.Science);
        quizGame.startRound();
        assertTrue(quizGame.hasGameStarted());
        // assertTrue(quizGame.isRoundOver());


    }

    @Test
    public void nextQuestion() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.nextQuestion();
        assertFalse(quizGame.getRoundQuestions().isEmpty());
        System.out.println(quizGame.getRoundQuestions());
        quizGame.endGame();
        assertFalse(quizGame.hasGameStarted());
        quizGame.nextQuestion();
        quizGame.nextQuestion();
        //  quizGame.nextQuestion();
        //  assertTrue(quizGame.isRoundOver());
        //  assertTrue(quizGame.getRoundQuestions().isEmpty());
        //  quizGame.nextQuestion();
        // assertFalse(quizGame.isRoundOver());

    }


    @Test
    public void enterAnswer() {
        // quizGame.startGame();
        //  quizGame.startRound();
        List<String> stringList = new ArrayList<>();
        Category category = Category.Science;
        stringList.add("HELLO");
        stringList.add("WORLD");
        stringList.add("SCRAMBLE");
        stringList.add("JOKE");
        Player player = new Player("yo", "1");
        Question question = new Question(category, "Kiki?", "yes?", stringList, 10);
        //wrong answer
        quizGame.enterAnswer(player, "hi", question, 10);
        assertEquals(0, player.getAmountOfTime());
        assertEquals(0, player.getAmountOfAlternatives());
        assertFalse(player.isAutoAnswer());
        assertEquals(0, player.getScore());
        //right answer
        Question question2 = new Question(category, "Kiki?", "yes?", stringList, 10);
        quizGame.enterAnswer(player, "yes?", question2, 4);
        // System.out.println(question2.getTime());
        assertEquals(200, player.getScore());

    }

    @Test
    public void isRoundOver() {
        quizGame.startGame();
        assertTrue(quizGame.isRoundOver());
        quizGame.startRound();
        assertFalse(quizGame.isRoundOver());
    }

    @Test
    public void getAllItems() {
        List<Item> item;
        quizGame.startGame();
        quizGame.startRound();
        //Lottery lottery = mock(Lottery.class);
        //  lottery.setItemList(item);
        //  assertEquals(lottery.getItemList(),quizGame.getAllItems());
    }

    @Test
    public void generateRandomCategoryArray() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.setCurrentCategory(Category.Science);
        assertEquals(quizGame.getCurrentCategory(), Category.Science);
        quizGame.generateRandomCategoryArray(4);
        assertEquals(4, quizGame.getCategorySelectionArray().length);
    }

    @Test
    public void buyItem() {

        startGame();
        startRound();
        Player player = new Player("yo", "1");
        Store store = mock(Store.class);

        // depends on number 1 is buff or debuff
        int item = 1;
        player.setScoreMultiplier(5);
        quizGame.buyItem(item, player);
        assertTrue(quizGame.isStoreItemBought(1));

        assertEquals(1, quizGame.getStore().getBoughtItems().size());

        // assertEquals(,player.getAmountOfAlternatives());
        // assertEquals(25,player.getScoreMultiplier());
        // assertEquals(,player.getAmountOfTime());

    }

    @Test
    public void drawLottery() {
        quizGame.startGame();
        List<Item> lottery2 = quizGame.getAllItems();
        quizGame.startRound();
        quizGame.getPlayerManager().addNewPlayerToEmptyTeam("yo", "1");
        quizGame.getPlayerManager().addNewPlayerToEmptyTeam("yoyo", "2");
        //  System.out.println(lottery.getWinnings().get("0"));
        quizGame.drawLottery();
        LotteryDrawEvent lotteryDrawEvent = (LotteryDrawEvent) EventBussMock.getEventList().get(EventBussMock.getEventList().size() - 1);
        assertEquals(2, lotteryDrawEvent.getWinnings().keySet().size());
        for (int i = 0; i < quizGame.getPlayerManager().getPlayers().size(); i++) {
            assertNotNull(lotteryDrawEvent.getWinnings().get(quizGame.getPlayerManager().getPlayers().get(i)));
            assertTrue(lottery2.contains(lotteryDrawEvent.getWinnings().get(quizGame.getPlayerManager().getPlayers().get(i))));
        }
        assertEquals(lottery2.size(), quizGame.getAllItems().size());
    }

    @Test
    public void broadCastQuestion() {
        List<String> stringList = new ArrayList<>();
        Category category = Category.Science;
        stringList.add("HELLO");
        stringList.add("WORLD");
        stringList.add("SCRAMBLE");
        stringList.add("JOKE");
        Question question = new Question(category, "Kiki?", "yes?", stringList, 10);
        quizGame.startGame();
        quizGame.startRound();
        quizGame.broadCastQuestion(question);
        QuestionEvent questionEvent = (QuestionEvent) EventBussMock.getEventList().get(EventBussMock.getEventList().size() - 1);
        assertEquals(question, questionEvent.getQuestion());
        QuizGame quizGame = new QuizGame(eventBuss, new PlayerManager(eventBuss, GameMode.HOT_SWAP), GameMode.HOST);
        assertEquals(question, questionEvent.getQuestion());

    }

    @Test
    public void addQuestion() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.addQuestion(Category.Celebrities);
        quizGame.endGame();
        for (int i = 0; i < 100; i++) {
            quizGame.addQuestion(Category.Celebrities);
        }
    }


    @Test

    public void getNextQuestionCategoryId() {
        // quizGame.startGame();

        //  assertNotNull(quizGame.getNextQuestionCategoryId());

        quizGame.startGame();
        //  quizGame.startRound();
        assertNotNull(quizGame.getNextQuestionCategoryId());
    }

    @Test
    public void getNextQuestionId() {

    }

    @Test
    public void setNextQuestion() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.setNextQuestion("c1", "1");
        assertEquals(Category.Science, quizGame.getCurrentCategory());
        assertEquals("Question text" + " " + "1", quizGame.getRoundQuestions().getFirst().getQuestionText());
    }

    @Test
    public void getQuestionIndex() {
        quizGame.startGame();
        quizGame.startRound();
        List<String> stringList = new ArrayList<>();
        Category category = Category.Science;
        stringList.add("HELLO");
        stringList.add("WORLD");
        stringList.add("SCRAMBLE");
        stringList.add("JOKE");
        Question question = new Question(category, "Kiki?", "yes?", stringList, 10);
        assertEquals(-1, quizGame.getQuestionIndex(Category.Test, question));
        assertEquals(-1, quizGame.getQuestionIndex(Category.Gaming, question));
    }

    @Test
    public void getQuestion() {
        quizGame.startGame();
        quizGame.startRound();
        assertNotNull(quizGame.getQuestion(Category.History, 1));
        assertNull(quizGame.getQuestion(Category.Test, 0));
        assertNull(quizGame.getQuestion(Category.Science, 100));

    }

    @Test
    public void getCurrentCategory() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.setCurrentCategory(Category.Mix);
        assertEquals(Category.Mix, quizGame.getCurrentCategory());
    }

    @Test
    public void setCurrentCategory() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.setCurrentCategory("random");
        assertNotNull(quizGame.getCurrentCategory());
    }


    @Test
    public void setCategorySelectionArray() {
    }


    @Test
    public void isStoreItemBuyable() {
        quizGame.startGame();
        quizGame.startRound();
        Player player = new Player("yo", "1");
        player.setScore(10);
        assertFalse(quizGame.isStoreItemBuyable(1, player));
    }


    @Test
    public void getStoreItem() {
        quizGame.startGame();
        quizGame.startRound();
        quizGame.getStoreItem(1);
        assertNotNull(quizGame.getStore().getStoreItems().size());

    }

    @Test
    public void isHotSwap() {
        quizGame.startGame();
        quizGame.startRound();
        QuizGame quizGame = new QuizGame(eventBuss, new PlayerManager(eventBuss, GameMode.HOT_SWAP), GameMode.HOST);
        assertFalse(quizGame.isHotSwap());
    }


}