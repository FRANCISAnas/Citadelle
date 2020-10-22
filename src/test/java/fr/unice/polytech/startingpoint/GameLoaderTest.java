package fr.unice.polytech.startingpoint;
import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameLoaderTest {
    private GameLoader gameLoader1;
    private BaseBot bot1;
    private BotsType[] botsType;


    @BeforeEach
    void setUp() throws IOException {
        botsType = new BotsType[]{BotsType.BasicBot};
        gameLoader1 = new GameLoader(botsType, 1);
        bot1 = BotsType.BasicBot.createBot(0);
    }
    @Test
    void getBotfromTypesTest() {
        ArrayList<BaseBot> botsexpected = new ArrayList<>();
        botsexpected.add(bot1);
        ArrayList<BaseBot> botsreel = gameLoader1.getBotfromTypes(botsType);
        for (int i = 0; i < botsreel.size(); i++) {
            assertEquals(botsexpected.get(i).getID(), gameLoader1.getBotfromTypes(botsType).get(i).getID());
        }
    }

    @Test
    void createPlayersTest(){

    }
}
