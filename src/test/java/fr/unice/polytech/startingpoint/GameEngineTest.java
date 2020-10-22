package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;


class GameEngineTest {
    private ArrayList<Player> players;
    private GameEngine gameEngine1;
    private ArrayList<BaseBot> botsListe;
    private ArrayList<BaseBuildings> cardsOfBuildings, listedeBatiment;
    private Player joueur1 = mock(Player.class),joueur2 = mock(Player.class),joueur3 = mock(Player.class),joueur4 = mock(Player.class);
    private ArrayList<Player> listeplayers = new ArrayList<>();


    @BeforeEach
    void setUp() throws IOException {
        GameLoader gameLoader;
        BotsType[] bots = new BotsType[]{BotsType.UncleSamBot};
        gameLoader = new GameLoader(bots,1);
        botsListe = gameLoader.getBotfromTypes(bots);
        players = gameLoader.createPlayers(botsListe);
        gameEngine1 = new GameEngine(players);
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        Collections.shuffle(cardsOfBuildings);
        listedeBatiment = new ArrayList<>();
        gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().clear();
        gameEngine1.setListOfGameCards(cardsOfBuildings);
        when(joueur1.getID()).thenReturn(1);when(joueur2.getID()).thenReturn(2);when(joueur3.getID()).thenReturn(3);when(joueur4.getID()).thenReturn(4);
        listeplayers.clear();
        listeplayers.add(joueur1);listeplayers.add(joueur2);listeplayers.add(joueur3);listeplayers.add(joueur4);

    }
    @Test void nbOfCharactersWhenChoiceBetween1and7Players(){//alias remixCharacterTest
        Player playerMock = mock(Player.class);
        players.clear();
        assertEquals(0,gameEngine1.getAvailableCharacters().size());
        for(int i = 1;i<8;i++){
            players.add(playerMock);
            gameEngine1.remixCharacterDeck();
            assertEquals(i+1,gameEngine1.getAvailableCharacters().size());
        }
    }
    @Test
    void characterDistributionBetween1And8Players(){
        players.clear();
        for(int i = 1;i<8;i++) {
            players.add(new Player(botsListe.get(0), i));
            players.get(0).crown();
            gameEngine1.remixCharacterDeck();
            gameEngine1.characterDistribution(players);
            assertEquals(1, gameEngine1.getAvailableCharacters().size());
            for (Player player : players) {
                assertNotEquals(null, player.getCharacter());
            }
        }
    }
    @Test void pickBuilding1BuildingInDeck(){
        listedeBatiment = gameEngine1.getActivePlayer().drawNBuildings(cardsOfBuildings, 1); // on ajoute les batiments
        gameEngine1.setListOfGameCards(listedeBatiment);
        gameEngine1.pickBuilding(gameEngine1.getCardsOfBuildings()); // on pioche une carte
        assertEquals(1, gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().size()); //test si le joueur a bien reçu sa carte
        assertEquals(0, gameEngine1.getCardsOfBuildings().size()); // test si la carte est bien partie du deck
        gameEngine1.pickBuilding(gameEngine1.getCardsOfBuildings()); // on pioche une autre carte
        assertEquals(1, gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().size());
        assertEquals(0, gameEngine1.getCardsOfBuildings().size());
        }

    @Test void pickBuilding2BuildingsInDeck(){
        listedeBatiment = gameEngine1.getActivePlayer().drawNBuildings(cardsOfBuildings, 2); // on ajoute les batiments
        gameEngine1.setListOfGameCards(listedeBatiment);
        gameEngine1.pickBuilding(gameEngine1.getCardsOfBuildings()); // on pioche une carte
        assertEquals(1, gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().size());
        assertEquals(1, gameEngine1.getCardsOfBuildings().size());
        gameEngine1.pickBuilding(gameEngine1.getCardsOfBuildings());
        assertEquals(2, gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().size());
        assertEquals(0, gameEngine1.getCardsOfBuildings().size());
    }

    @Test void pickBuildingDeckEmpty(){
        listedeBatiment = gameEngine1.getActivePlayer().drawNBuildings(cardsOfBuildings, 0); // on ajoute les batiments dans une liste secondaire
        gameEngine1.setListOfGameCards(listedeBatiment);
        gameEngine1.pickBuilding(gameEngine1.getCardsOfBuildings());
        assertEquals(0,gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().size());
        assertEquals(0,gameEngine1.getCardsOfBuildings().size());
    }
    @Test void pickBuildingPickInRow(){
        gameEngine1.setListOfGameCards(cardsOfBuildings);
        int len = cardsOfBuildings.size();
        int nbexpected = 0;
        for (int i = 0; i<=len+2;i++){
            if(i<= len) {
                nbexpected = i;
            }
            assertEquals(nbexpected,gameEngine1.getActivePlayer().getBuildingsOfThisPlayer().size());
            assertEquals(len-nbexpected, gameEngine1.getCardsOfBuildings().size());
            gameEngine1.pickBuilding(gameEngine1.getCardsOfBuildings());
        }
    }
    @Test void gamePreparationTest7Players(){
        players.clear();
        for(int i =0;i<7;i++) {
            players.add(new Player(botsListe.get(0), i));
        }
        gameEngine1.gamePreparation();
        for(Player player : gameEngine1.getPlayers()){
            assertEquals(2,player.getGold());
            assertEquals(4, player.getBuildingsOfThisPlayer().size());
            assertNotEquals(null,player.getCharacter());
            assertEquals(7,player.getGamePlayers().size());
            assertTrue(gameEngine1.searchCrown().getCrowned());
            }

         }
    @Test void nbCharacterToRemoveTestBetween0and10Players(){
        players.clear();
        for(int i =0;i<10;i++) {
            if(i<7)assertEquals(8 - (i +1), gameEngine1.calculateNbrOfCharacterToRemove());
            else assertEquals(0,gameEngine1.calculateNbrOfCharacterToRemove());
            players.add(new Player(botsListe.get(0), i));
        }
    }
    @Test void remixCharacterTest(){
        players.clear();
        for(int i =0;i<7;i++) {
            gameEngine1.remixCharacterDeck();
            assertEquals(i+1, gameEngine1.getAvailableCharacters().size());
            players.add(new Player(botsListe.get(0), i));
        }
    }
    @Test void newTurnTestGoodDistributionAllAlive(){
        players.clear();
        for(int i = 1;i<8;i++) {
            players.add(new Player(botsListe.get(0), i));
            gameEngine1.gamePreparation();
            players.get(0).kill();
            gameEngine1.newTurn();
            assertEquals(1, gameEngine1.getAvailableCharacters().size());
            for (Player player : players) {
                assertNotEquals(null, player.getCharacter());
                assertFalse(players.get(0).getKilled());
            }
        }
    }

    @Test void setWinners1Winner(){
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getScore()).thenReturn(10);when(joueur2.getScore()).thenReturn(12);when(joueur3.getScore()).thenReturn(20);when(joueur4.getScore()).thenReturn(21);
        gameEngine1.setWinners();
        assertEquals(1,gameEngine1.getWinners().size());
        assertEquals(4, gameEngine1.getWinners().get(0).getID());
    }

    @Test void setWinners3Winners(){
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getScore()).thenReturn(21);when(joueur2.getScore()).thenReturn(21);when(joueur3.getScore()).thenReturn(20);when(joueur4.getScore()).thenReturn(21);
        gameEngine1.setWinners();
        assertEquals(3,gameEngine1.getWinners().size());
        assertEquals(1, gameEngine1.getWinners().get(0).getID());
        assertEquals(2, gameEngine1.getWinners().get(1).getID());
        assertEquals(4, gameEngine1.getWinners().get(2).getID());
    }
    @Test void setWinnersAll0(){
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getScore()).thenReturn(0);when(joueur2.getScore()).thenReturn(0);when(joueur3.getScore()).thenReturn(0);when(joueur4.getScore()).thenReturn(0);
        gameEngine1.setWinners();
        assertEquals(4,gameEngine1.getWinners().size());
        assertEquals(1, gameEngine1.getWinners().get(0).getID());
        assertEquals(2, gameEngine1.getWinners().get(1).getID());
        assertEquals(3, gameEngine1.getWinners().get(2).getID());
        assertEquals(4, gameEngine1.getWinners().get(3).getID());
    }
    @Test void searchCrownTest() {
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getCrowned()).thenReturn(false);
        when(joueur2.getCrowned()).thenReturn(false);
        when(joueur3.getCrowned()).thenReturn(true);
        when(joueur4.getCrowned()).thenReturn(false);
        assertEquals(3, gameEngine1.searchCrown().getID());
    }
    @Test void searchCrownNobody(){
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getCrowned()).thenReturn(false);when(joueur2.getCrowned()).thenReturn(false);when(joueur3.getCrowned()).thenReturn(false);when(joueur4.getCrowned()).thenReturn(false);
        assertNull(gameEngine1.searchCrown());
    }

    @Test void searchVoleurTest(){
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getCharacter()).thenReturn(CharacterEnum.Magicien.createCharacter());
        when(joueur2.getCharacter()).thenReturn(CharacterEnum.Assassin.createCharacter());
        when(joueur3.getCharacter()).thenReturn(CharacterEnum.Voleur.createCharacter());
        when(joueur4.getCharacter()).thenReturn(CharacterEnum.Condottiere.createCharacter());
        assertEquals(3,gameEngine1.searchVoleur().getID());
    }
    @Test void searchVoleurNobody(){
        gameEngine1.setPlayers(listeplayers);
        when(joueur1.getCharacter()).thenReturn(CharacterEnum.Magicien.createCharacter());
        when(joueur2.getCharacter()).thenReturn(CharacterEnum.Assassin.createCharacter());
        when(joueur3.getCharacter()).thenReturn(CharacterEnum.Architecte.createCharacter());
        when(joueur4.getCharacter()).thenReturn(CharacterEnum.Condottiere.createCharacter());
        assertNull(gameEngine1.searchVoleur());
    }


}
