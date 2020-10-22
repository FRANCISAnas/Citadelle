package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private BaseBot winner;

    @BeforeEach
    void setUp() throws IOException {
        BotsType[] bots = new BotsType[]{BotsType.BasicBot};
        GameLoader gameLoader = new GameLoader(bots, 1);
        GameEngine gameEngine = gameLoader.load();
        gameEngine.run();
        winner = gameEngine.getWinners().get(0).getBot();
    }

    @Test
    void helloTest() {
        BaseBot bot = BotsType.BasicBot.createBot(0);
        assert bot != null;
        assertEquals(bot.getName(), winner.getName());
    }
}