package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BasicBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerTest {
    private Player player;
    private Player p1, p2, p3, p4;
    private ArrayList<Player> otherPlayers = new ArrayList<>();

    private CharacterEnum condottiere = CharacterEnum.Condottiere;
    private CharacterEnum voleur = CharacterEnum.Voleur;
    private CharacterEnum assassin = CharacterEnum.Assassin;
    private CharacterEnum magicien = CharacterEnum.Magicien;
    private CharacterEnum roi = CharacterEnum.Roi;
    private CharacterEnum eveque = CharacterEnum.Eveque;
    private CharacterEnum marchand = CharacterEnum.Marchand;
    private CharacterEnum architecte = CharacterEnum.Architecte;
    private ArrayList<BaseBuildings> cardsOfBuildings;
    private BaseCharacter charCon, charVol, charAss, charMag, charRoi, charEveq, charMarch, charArchit;

    private City cityMock1, cityMock2, cityMock3, cityMock4;

    @BeforeEach
    void setUp(){
        BaseBot bot = BotsType.BasicBot.createBot(1),
                bot1 = BotsType.JealousBot.createBot(2),
                bot2 = BotsType.BasicBot.createBot(3),
                bot3 = BotsType.UncleSamBot.createBot(4),
                bot4 = BotsType.BuilderBot.createBot(5);
        player = new Player(bot,1);
        p1 =  new Player(bot1,2);
        p2 =  new Player(bot2,3);
        p3 =  new Player(bot3,4);
        p4 =  new Player(bot4,5);


        charCon = condottiere.createCharacter();
        charVol = voleur.createCharacter();
        charAss = assassin.createCharacter();
        charMag = magicien.createCharacter();
        charRoi = roi.createCharacter();
        charEveq = eveque.createCharacter();
        charArchit = architecte.createCharacter();
        charMarch = marchand.createCharacter();

        cityMock1 = mock(City.class);
        cityMock2 = mock(City.class);
        cityMock3 = mock(City.class);
        cityMock4 = mock(City.class);

        p1.setCity(cityMock1);p2.setCity(cityMock2);
        p3.setCity(cityMock3);p4.setCity(cityMock4);

        otherPlayers.add(player);otherPlayers.add(p1);otherPlayers.add(p2);otherPlayers.add(p3);otherPlayers.add(p4);
        player.setGamePlayers(otherPlayers);p1.setGamePlayers(otherPlayers);p2.setGamePlayers(otherPlayers);
        p3.setGamePlayers(otherPlayers);p4.setGamePlayers(otherPlayers);
        player.getBuildingsBuilt().clear();
        player.getBuildingsOfThisPlayer().clear();
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        Collections.shuffle(cardsOfBuildings);
    }


    @Test void setScoreNormal(){
        player.getCity().build(new BaseBuildings("1",1,5, BuildingsTypeEnum.RELIGIEUX.toString()));
        player.getCity().build(new BaseBuildings("2",1,3,BuildingsTypeEnum.MILITAIRE.toString()));
        player.getCity().build(new BaseBuildings("3",1,1,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("4",1,3,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("5",1,2,BuildingsTypeEnum.MERVEILLE.toString()));
        player.setScore();
        assertEquals(14,player.getScore());

    }
    @Test void setScoreBonus8Batiments(){
        player.finishCitadel(false);
        player.getCity().build(new BaseBuildings("1",1,5, BuildingsTypeEnum.RELIGIEUX.toString()));
        player.getCity().build(new BaseBuildings("2",1,3,BuildingsTypeEnum.MILITAIRE.toString()));
        player.getCity().build(new BaseBuildings("3",1,1,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("4",1,3,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("5",1,2,BuildingsTypeEnum.MERVEILLE.toString()));
        player.setScore();
        assertEquals(16,player.getScore());
    }
    @Test void setScoreBonus1erAFinir(){
        player.finishCitadel(true);
        player.getCity().build(new BaseBuildings("1",1,5, BuildingsTypeEnum.RELIGIEUX.toString()));
        player.getCity().build(new BaseBuildings("2",1,3,BuildingsTypeEnum.MILITAIRE.toString()));
        player.getCity().build(new BaseBuildings("3",1,1,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("4",1,3,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("5",1,2,BuildingsTypeEnum.MERVEILLE.toString()));
        player.setScore();
        assertEquals(18,player.getScore());
    }
    @Test void setScoreBonusToutLesTypes(){
        player.getCity().build(new BaseBuildings("1",1,5, BuildingsTypeEnum.RELIGIEUX.toString()));
        player.getCity().build(new BaseBuildings("2",1,3,BuildingsTypeEnum.MILITAIRE.toString()));
        player.getCity().build(new BaseBuildings("3",1,1,BuildingsTypeEnum.MARCHAND.toString()));
        player.getCity().build(new BaseBuildings("4",1,3,BuildingsTypeEnum.NOBLE.toString()));
        player.getCity().build(new BaseBuildings("5",1,2,BuildingsTypeEnum.MERVEILLE.toString()));
        player.setScore();
        assertEquals(17,player.getScore());}
    @Test void setScoreCourDesMiracles(){
        BasicBot botMock = mock(BasicBot.class);
        Player playerbis = new Player(botMock,1);
        when(botMock.courDesMiraclesDecision()).thenReturn(BuildingsTypeEnum.NOBLE.toString());
        playerbis.getCity().build(new BaseBuildings("1",1,5, BuildingsTypeEnum.RELIGIEUX.toString()));
        playerbis.getCity().build(new BaseBuildings("2",1,3,BuildingsTypeEnum.MILITAIRE.toString()));
        playerbis.getCity().build(new BaseBuildings("3",1,1,BuildingsTypeEnum.MARCHAND.toString()));
        playerbis.getCity().build(new BaseBuildings("4",1,3,BuildingsTypeEnum.MARCHAND.toString()));
        playerbis.getCity().build(new BaseBuildings("5",1,2,BuildingsTypeEnum.MERVEILLE.toString()));
        playerbis.getCity().build(new BaseBuildings(MerveilleEnum.COURDESMIRACLES.toString(),2,2,BuildingsTypeEnum.MERVEILLE.toString()));
        playerbis.setScore();
        assertEquals(19,playerbis.getScore());
    }


    @Test void canBuildABuildingMainVide() {
        ArrayList<BaseBuildings> mainDuJoueur = new ArrayList<>();
        player.setBuildingsOfThisPlayer(mainDuJoueur);
        assertFalse(player.canBuildABuilding()); // il ne peut pas car la main est vide
    }

    @Test void canBuildABuildingNormal() {
        ArrayList<BaseBuildings> mainDuJoueur = new ArrayList<>(player.drawNBuildings(cardsOfBuildings, 10)); //liste de batiment de coût croissant mélangée
        player.setBuildingsOfThisPlayer(mainDuJoueur);
        player.setGold(200); // si il a 200 po
        assertTrue(player.canBuildABuilding());// il peut car tout les cout sont < 200
    }
    @Test void canBuildABuildingPasAssezDeSous(){
        ArrayList<BaseBuildings> mainDuJoueur = new ArrayList<>(player.drawNBuildings(cardsOfBuildings, 10)); //liste de batiment de coût croissant mélangée
        player.setBuildingsOfThisPlayer(mainDuJoueur);
        player.setGold(0);// s'il a 0 po
        assertFalse(player.canBuildABuilding());// il ne peut rien construire
    }
    @Test void canBuildABuildingBuildingDejaConstruit() {
        ArrayList<BaseBuildings> mainDuJoueur = new ArrayList<>(player.drawNBuildings(cardsOfBuildings, 10)); //liste de batiment de coût croissant mélangée
        for (BaseBuildings building : mainDuJoueur) {
            player.getCity().build(building);
        }
        mainDuJoueur.subList(0, 5).clear();
        player.setBuildingsOfThisPlayer(mainDuJoueur);
        player.setGold(200);// s'il a 0 po
        assertFalse(player.canBuildABuilding());// il ne peut rien construire car ils sont tous construits
    }
    @Test void drawNbuildingsNormal(){
        ArrayList<BaseBuildings> listedeBatiment;
        int cardsOfBuildingsSize = cardsOfBuildings.size();
        listedeBatiment = player.drawNBuildings(cardsOfBuildings,4);
        assertEquals(4,listedeBatiment.size());
        assertEquals(cardsOfBuildingsSize-4,cardsOfBuildings.size());
    }
    @Test void drawNbuildingsPiocherTout() {
        ArrayList<BaseBuildings> listedeBatiment;
        int cardsOfBuildingsSize = cardsOfBuildings.size();
        listedeBatiment = player.drawNBuildings(cardsOfBuildings,cardsOfBuildingsSize);
        assertEquals(cardsOfBuildingsSize,listedeBatiment.size());
        assertEquals(0,cardsOfBuildings.size());
    }
    @Test void drawNbuildings0Cartes() {
        ArrayList<BaseBuildings> listedeBatiment;
        int cardsOfBuildingsSize = cardsOfBuildings.size();
        listedeBatiment = player.drawNBuildings(cardsOfBuildings,0);
        assertEquals(0,listedeBatiment.size());
        assertEquals(cardsOfBuildingsSize,cardsOfBuildings.size());
    }
    @Test void drawNbuildingsPiochesTropDeCartes() {
        ArrayList<BaseBuildings> listedeBatiment;
        int cardsOfBuildingsSize = cardsOfBuildings.size();
        listedeBatiment = player.drawNBuildings(cardsOfBuildings,cardsOfBuildingsSize*2);
        assertEquals(cardsOfBuildingsSize,listedeBatiment.size());
        assertEquals(0,cardsOfBuildings.size());
    }
    @Test void drawNbuildingsPiocheVide() {
        ArrayList<BaseBuildings> listedeBatiment;
        cardsOfBuildings.clear();
        listedeBatiment = player.drawNBuildings(cardsOfBuildings,3);
        assertEquals(0,listedeBatiment.size());
        assertEquals(0,cardsOfBuildings.size());
    }

    /**
     * @author FRANCIS Anas
     * \brief tester si la liste retourner par la méthode 'sortPlayersByNbOFBuildings' sont classé par ordre croissant des nombre des bâtiments dans leurs cités
     */
    @Test
    void getPlayerMaxBuildingsWithTrueValueTest(){

        p1.setCharacter(charCon);
        p2.setCharacter(charArchit);
        p3.setCharacter(charEveq);
        p4.setCharacter(charRoi);
        player.setCharacter(charAss);

        when(cityMock3.getNbrBuildings()).thenReturn(10);
        when(cityMock2.getNbrBuildings()).thenReturn(18);
        when(cityMock1.getNbrBuildings()).thenReturn(4);
        when(cityMock4.getNbrBuildings()).thenReturn(7);

        ArrayList<Player> localPlayers = new ArrayList<>(player.getGamePlayers());
        Player maxPlayer;

        maxPlayer = player.getPlayerMaxBuildings(localPlayers, true);
        assertEquals(p2, maxPlayer);
        localPlayers.remove(p2);

        maxPlayer = player.getPlayerMaxBuildings(localPlayers, true);
        assertEquals(p3, maxPlayer);
        localPlayers.remove(p3);

        maxPlayer = player.getPlayerMaxBuildings(localPlayers, true);
        assertEquals(p4, maxPlayer);
        localPlayers.remove(p4);

        maxPlayer = player.getPlayerMaxBuildings(localPlayers, true);
        assertEquals(p1, maxPlayer);
        localPlayers.remove(p1);

        maxPlayer = player.getPlayerMaxBuildings(localPlayers, true);
        assertEquals(player, maxPlayer);
        localPlayers.remove(player);

        maxPlayer = player.getPlayerMaxBuildings(localPlayers, true);
        assertNull(maxPlayer);
    }

    @Test
    void getPlayerMaxBuildingsWithFalseValueTest(){

        Player maxPlayer;
        Player playerMocked1 = mock(Player.class);
        Player playerMocked2 = mock(Player.class);
        Player playerMocked3 = mock(Player.class);
        Player playerMocked4 = mock(Player.class);

        player.setCharacter(charAss);
        when(playerMocked1.getCharacter()).thenReturn(charCon);
        when(playerMocked2.getCharacter()).thenReturn(charVol);
        when(playerMocked3.getCharacter()).thenReturn(charEveq);
        when(playerMocked4.getCharacter()).thenReturn(charRoi);

        ArrayList<Player> mockedPlayers = new ArrayList<>();
        mockedPlayers.add(playerMocked1);mockedPlayers.add(playerMocked2);mockedPlayers.add(playerMocked3);mockedPlayers.add(playerMocked4);
        mockedPlayers.add(player);

        player.setGamePlayers(mockedPlayers);

        when(playerMocked1.getNbOfbuildingsInHisHand()).thenReturn(15);
        when(playerMocked2.getNbOfbuildingsInHisHand()).thenReturn(20);
        when(playerMocked3.getNbOfbuildingsInHisHand()).thenReturn(1000);
        when(playerMocked4.getNbOfbuildingsInHisHand()).thenReturn(54);

        maxPlayer = player.getPlayerMaxBuildings(mockedPlayers, false);
        assertEquals(playerMocked3, maxPlayer);
        mockedPlayers.remove(playerMocked3);

        maxPlayer = player.getPlayerMaxBuildings(mockedPlayers, false);
        assertEquals(playerMocked4, maxPlayer);
        mockedPlayers.remove(playerMocked4);

        maxPlayer = player.getPlayerMaxBuildings(mockedPlayers, false);
        assertEquals(playerMocked2, maxPlayer);
        mockedPlayers.remove(playerMocked2);

        maxPlayer = player.getPlayerMaxBuildings(mockedPlayers, false);
        assertEquals(playerMocked1, maxPlayer);
        mockedPlayers.remove(playerMocked1);

        maxPlayer = player.getPlayerMaxBuildings(mockedPlayers, false);
        assertEquals(player, maxPlayer);
        mockedPlayers.remove(player);

        maxPlayer = player.getPlayerMaxBuildings(mockedPlayers, false);
        assertNull(maxPlayer);

    }

    @Test void playersButWithoutTest(){
        p1.setCharacter(charAss);p2.setCharacter(charVol);
        p3.setCharacter(charRoi);p4.setCharacter(charEveq);player.setCharacter(charCon);
        ArrayList<Player> myPlayers = player.playersButWithout(CharacterEnum.ASSASSINPRIORITY);
        for(int i = 0; i<3; i++){
            assertNotEquals(myPlayers.get(i).getCharacter(), p1);
            assertNotEquals(myPlayers.get(i).getCharacter(), player);
        }
    }

    @Test void playersButWithoutKnownPrioritiesTest(){
        p1.setCharacter(charAss);
        p2.setCharacter(charVol);p3.setCharacter(charRoi);
        p4.setCharacter(charEveq);player.setCharacter(charCon);
        ArrayList<Player> myPlayers = player.playersButWithout(9);
        assertEquals(5,myPlayers.size());
    }

    @Test void imposableBuildingsWithVariousCardsTest(){
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        player.setGold(1000);
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));

        for (int i = 0; i<5; i++)player.useBuilding(buildingsInTheHand.remove(0));

        player.setBuildingsOfThisPlayer(buildingsInTheHand);

        assertFalse(player.imposableBuildings());

    }

    @Test void imposableBuildingsWithSameCardsTest(){
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        player.setGold(1000);
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("b",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("b",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));

        for (int i = 0; i<5; i++)player.useBuilding(buildingsInTheHand.remove(0));

        player.setBuildingsOfThisPlayer(buildingsInTheHand);

        assertTrue(player.imposableBuildings());

    }

}
