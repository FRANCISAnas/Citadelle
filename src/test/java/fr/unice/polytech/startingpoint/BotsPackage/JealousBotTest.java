package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.ReadJSONFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JealousBotTest {
    private BotsType bots = BotsType.JealousBot;
    private BaseBot jealous,jealous2,jealous3,jealous4,jealous5;
    private Player joueurMock;
    private Player activePlayer;

    //Import des rôles pour tester le choix des rôles,
    //sera efficace lorsque le rôle de l'architecte sera disponible.
    private CharacterEnum condottiere = CharacterEnum.Condottiere;
    private CharacterEnum voleur = CharacterEnum.Voleur;
    private CharacterEnum assassin = CharacterEnum.Assassin;
    private CharacterEnum magicien = CharacterEnum.Magicien;
    private CharacterEnum roi = CharacterEnum.Roi;
    private CharacterEnum eveque = CharacterEnum.Eveque;
    private CharacterEnum marchand = CharacterEnum.Marchand;
    private CharacterEnum architecte = CharacterEnum.Architecte;
    private ArrayList<BaseCharacter> availableChars;
    private BaseCharacter charCon, charVol, charAss, charMag, charRoi, charEveq, charMarch, charArchi;
    private ArrayList<BaseBuildings> cardsOfBuildings;
    private ArrayList<BaseBuildings> buildingsInTheHand;
    private Player p2,p3,p4,p5;

    @BeforeEach
    void setUp() {
        jealous = bots.createBot(1);
        jealous2 = bots.createBot(2);
        jealous3 = bots.createBot(3);
        jealous4 = bots.createBot(4);
        jealous5 = bots.createBot(5);
        joueurMock = mock(Player.class);
        activePlayer = new Player(jealous,1);
        jealous.updateCurrentPlayer(activePlayer);
        availableChars = new ArrayList<>();
        charCon = condottiere.createCharacter();
        charVol = voleur.createCharacter();
        charAss = assassin.createCharacter();
        charMag = magicien.createCharacter();
        charRoi = roi.createCharacter();
        charArchi = architecte.createCharacter();
        jealous.getCurrentPlayer().setGold(100);
        p2 =  new Player(jealous2,2);
        p3 =  new Player(jealous3,3);
        p4 =  new Player(jealous4,4);
        p5 =  new Player(jealous5,5);

        availableChars = new ArrayList<>();

        buildingsInTheHand = new ArrayList<>();
        Collections.shuffle(buildingsInTheHand);

        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        charCon = condottiere.createCharacter();
        charVol = voleur.createCharacter();
        charAss = assassin.createCharacter();
        charMag = magicien.createCharacter();
        charRoi = roi.createCharacter();
        charEveq = eveque.createCharacter();
        charArchi = architecte.createCharacter();
        charMarch = marchand.createCharacter();

        Collections.shuffle(cardsOfBuildings);
        activePlayer.setGold(1000);

        availableChars.clear();
        availableChars.add(charArchi);availableChars.add(charAss);availableChars.add(charCon);availableChars.add(charEveq);
        availableChars.add(charMag);availableChars.add(charMarch);availableChars.add(charRoi);availableChars.add(charVol);

        ArrayList<Player> otherPlayersNotMocked = new ArrayList<>();
        otherPlayersNotMocked.add(activePlayer);
        otherPlayersNotMocked.add(p2);
        otherPlayersNotMocked.add(p3);
        otherPlayersNotMocked.add(p4);
        otherPlayersNotMocked.add(p5);
        activePlayer.setGamePlayers(otherPlayersNotMocked);
        buildingsInTheHand.clear();
    }

    @Test void chooseCharacterOneGot6BuildingsBuilt() {
        p2.setGold(100);
        for(int i = 0;i<6;i++) {
            p2.useBuilding(new BaseBuildings(""+i, 2, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        }
        assertEquals(charCon, jealous.chooseCharacter(availableChars));
    }

    @Test void chooseCharacterOneGotA1CostBuildingBuilt() {
        p2.setGold(100);
        for(int i = 1;i<3;i++) {
            p2.useBuilding(new BaseBuildings(""+i, i, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        }
        assertEquals(charCon, jealous.chooseCharacter(availableChars));
    }
    @Test void chooseCharacterLessThan1CardsInHand() {
        assertEquals(charMag, jealous.chooseCharacter(availableChars));
    }

    @Test void chooseCharacterLotOfMoneyOnBoard() {
        activePlayer.setGold(8);
        buildingsInTheHand.add(new BaseBuildings("1",3,3,""));
        buildingsInTheHand.add(new BaseBuildings("2",3,3,""));
        activePlayer.setBuildingsOfThisPlayer(buildingsInTheHand);
        p2.setGold(2);p3.setGold(5);p4.setGold(1);p5.setGold(1);
        assertEquals(charVol, jealous.chooseCharacter(availableChars));
    }
    @Test
    void chooseCharacterTestPriorityOrderIfNothingParticular() {
        activePlayer.setGold(100);//setup
        buildingsInTheHand.add(new BaseBuildings("1",3,3,""));
        buildingsInTheHand.add(new BaseBuildings("2",3,3,""));
        activePlayer.setBuildingsOfThisPlayer(buildingsInTheHand);
        p2.setGold(0);p3.setGold(0);p4.setGold(0);p5.setGold(0);

        //Par ordre croissant de priorité
        assertEquals(charAss, jealous.chooseCharacter(availableChars));
        availableChars.remove(charAss);
        assertEquals(charCon, jealous.chooseCharacter(availableChars));
        availableChars.remove(charCon);
        assertEquals(charVol, jealous.chooseCharacter(availableChars));
        availableChars.remove(charVol);
        assertEquals(charMag, jealous.chooseCharacter(availableChars));
        availableChars.remove(charMag);
        assertEquals(charArchi, jealous.chooseCharacter(availableChars));
        availableChars.remove(charArchi);
        assertEquals(charRoi, jealous.chooseCharacter(availableChars));
        availableChars.remove(charRoi);
        assertEquals(charMarch, jealous.chooseCharacter(availableChars));
        availableChars.remove(charMarch);
        assertEquals(charEveq, jealous.chooseCharacter(availableChars));
    }
    @Test void chooseBuildingToPickHigherNonConstructible(){
        activePlayer.setGold(0);
        ArrayList<BaseBuildings> buildingsChoice = activePlayer.drawNBuildings(cardsOfBuildings,2);
        BaseBuildings buildingChosen = jealous.chooseBuildings(buildingsChoice);
        boolean isMax = true;
        for (BaseBuildings building : buildingsChoice){
            if (building.getPrice() > buildingChosen.getPrice()){
                isMax = false;break;
            }
        }
        assertTrue(isMax);
    }
    @Test void chooseBuildingToPickConstructible(){
        activePlayer.setGold(1000);
        ArrayList<BaseBuildings> buildingsChoice = activePlayer.drawNBuildings(cardsOfBuildings,2);
        BaseBuildings buildingChosen = jealous.chooseBuildings(buildingsChoice);
        boolean isMax = true;
        for (BaseBuildings building : buildingsChoice){
            if (building.getPrice() > buildingChosen.getPrice() && building.getPrice()<=activePlayer.getGold()){
                isMax = false;break;
            }
        }
        assertTrue(isMax);
        assertTrue(buildingChosen.getPrice()<=activePlayer.getGold());
    }
    @Test void chooseBuildingToPickObservatoire(){
        activePlayer.setGold(0);
        ArrayList<BaseBuildings> buildingsChoice = activePlayer.drawNBuildings(cardsOfBuildings,2);
        BaseBuildings buildingChosen = jealous.chooseBuildings(buildingsChoice);
        boolean isMax = true;
        for (BaseBuildings building : buildingsChoice){
            if (building.getPrice() > buildingChosen.getPrice()){
                isMax = false;break;
            }
        }
        assertTrue(isMax);
    }

    @Test
    void chooseBuildingsToBuildLowerCost() {
        when(joueurMock.getGold()).thenReturn(302);
        ArrayList<BaseBuildings> main = new ArrayList<>();
        main.add(new BaseBuildings("Bat1",10,10,""));
        BaseBuildings best = new BaseBuildings("Bat1",2,2,"");
        main.add(best);
        main.add(new BaseBuildings("Bat1",20,10,""));
        main.add(new BaseBuildings("Bat1",4,10,""));
        assertEquals(best,jealous.chooseBuildingsToBuild(main));
    }

    @Test
    void wannaBuildTest() {
        when(joueurMock.canBuildABuilding()).thenReturn(true);
        jealous.updateCurrentPlayer(joueurMock);
        assertTrue(jealous.wannaBuild());
        when(joueurMock.canBuildABuilding()).thenReturn(false);
        assertFalse(jealous.wannaBuild());
    }

    @Test void drawOrGoldHaveNotMagicienAndHandEmpty() {
        //sa main est vide
        jealous.updateCurrentPlayer(joueurMock);
        when(joueurMock.getCharacter()).thenReturn(charAss);//il n'a pas eu le magicien donc il pioche
        assertTrue(jealous.drawOrGold());
    }
    @Test void drawOrGoldHaveMagicienAndHandEmpty() {
        jealous.updateCurrentPlayer(joueurMock);
        when(joueurMock.getCharacter()).thenReturn(charMag);//il n'a pas eu le magicien donc il pioche
        assertFalse(jealous.drawOrGold());
    }
    @Test void drawOrGoldHaveMagAndBuildingAlreadyBuilt() {
        activePlayer.setCharacter(charMag);
        buildingsInTheHand.add(new BaseBuildings("Bat1",20,10,""));
        buildingsInTheHand.add(new BaseBuildings("Bat1",10,10,""));
        activePlayer.setBuildingsOfThisPlayer(buildingsInTheHand);
        activePlayer.useBuilding(activePlayer.getBuildingsOfThisPlayer().get(0));
        assertFalse(jealous.drawOrGold());
    }
    @Test void characterTurnTest(){
        ArrayList<String> turn1 = new ArrayList<>();
        ArrayList<String> turn2 = new ArrayList<>();
        ArrayList<String> turn3 = new ArrayList<>();
        ArrayList<String> turn4 = new ArrayList<>();
        turn1.add(CharacterEnum.ACTIVEPOWER);turn1.add(CharacterEnum.LABORATOIREABILITY);turn1.add(CharacterEnum.BUILD);
        turn2.add(CharacterEnum.ACTIVEPOWER);turn2.add(CharacterEnum.LABORATOIREABILITY);turn2.add(CharacterEnum.BUILD);turn2.add(CharacterEnum.PASSIVEPOWER);
        turn3.add(CharacterEnum.PASSIVEPOWER);turn3.add(CharacterEnum.LABORATOIREABILITY);turn3.add(CharacterEnum.BUILD);
        turn4.add(CharacterEnum.LABORATOIREABILITY);turn4.add(CharacterEnum.BUILD);
        assertEquals(turn1,jealous.assassinTurn());
        assertEquals(turn1,jealous.voleurTurn());
        assertEquals(turn3,jealous.magicienTurn());
        assertEquals(turn1,jealous.roiTurn());
        assertEquals(turn1,jealous.evequeTurn());
        assertEquals(turn1,jealous.marchandTurn());
        assertEquals(turn2,jealous.condottiereTurn());
        assertEquals(turn4,jealous.architecteTurn());
    }
    @Test void magicienPassivePower(){
        activePlayer.setCharacter(charMag);
        ArrayList<Object> autresParametres = new ArrayList<>();
        autresParametres.add(p3);
        buildingsInTheHand.add(new BaseBuildings("Bat1",20,10,""));
        buildingsInTheHand.add(new BaseBuildings("Bat2",10,10,""));
        p3.setBuildingsOfThisPlayer(buildingsInTheHand);
        assertEquals(autresParametres,jealous.doPassivePower());
    }
    @Test void CondottierePassivePower(){
        activePlayer.setCharacter(charCon);
        ArrayList<Object> autresParametres = new ArrayList<>();
        BaseBuildings buildingToDestroy = new BaseBuildings("Bat3",1,10,"");
        autresParametres.add(p3);
        p3.setGold(100);
        p3.setCharacter(charArchi);
        p2.setCharacter(charMag);
        p4.setCharacter(charEveq);
        p5.setCharacter(charVol);
        autresParametres.add(buildingToDestroy);
        buildingsInTheHand.add(new BaseBuildings("Bat1",20,10,""));
        buildingsInTheHand.add(new BaseBuildings("Bat2",10,10,""));
        buildingsInTheHand.add(buildingToDestroy);
        buildingsInTheHand.add(new BaseBuildings("Bat4",10,10,""));
        p3.setBuildingsOfThisPlayer(buildingsInTheHand);
        p3.useBuilding(buildingsInTheHand.get(0));p3.useBuilding(buildingsInTheHand.get(0));
        p3.useBuilding(buildingsInTheHand.get(0));p3.useBuilding(buildingsInTheHand.get(0));
        assertEquals(autresParametres,jealous.doPassivePower());
    }
    @Test void laboratoryDecisionTest(){
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        activePlayer.setGold(41);// ou 40 ça marche aussi l'ideal c'est qu'il lui restra moins de 2 pièce après la construction
        activePlayer.getCity().getBuildings().clear();
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d1",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        activePlayer.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        for (int i = 0; i<3;i++)activePlayer.useBuilding(activePlayer.getBuildingsOfThisPlayer().get(0));
        BaseBuildings theUnWantedBuilding = activePlayer.laboratoryDecision();
        assertEquals(buildingsInTheHand.get(3),theUnWantedBuilding);// car c'est le plus chère parmis les deux cartes qui sont restés dans sa main
    }
}