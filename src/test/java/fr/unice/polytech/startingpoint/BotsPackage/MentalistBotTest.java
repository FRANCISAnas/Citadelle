package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MentalistBotTest {
    private BotsType bot = BotsType.MentalistBot;
    private MentalistBot mentalist;
    private Player joueurMockTest, joueurMock1, joueurMock2, joueurMock3, joueurMock4, joueurMock5, joueurMock6;
    ArrayList<BaseCharacter> roles = new ArrayList<>();
    private CharacterEnum voleur = CharacterEnum.Voleur;
    private CharacterEnum assassin = CharacterEnum.Assassin;
    private CharacterEnum magicien = CharacterEnum.Magicien;
    private CharacterEnum roi = CharacterEnum.Roi;
    private CharacterEnum eveque = CharacterEnum.Eveque;
    private CharacterEnum marchand = CharacterEnum.Marchand;
    private CharacterEnum architecte = CharacterEnum.Architecte;
    private CharacterEnum condottiere = CharacterEnum.Condottiere;
    private ArrayList<Player> otherPlayers = new ArrayList<>();
    private ArrayList<BaseBuildings> smallBuildingList = new ArrayList<>();
    private ArrayList<BaseBuildings> mediumBuildingList = new ArrayList<>();
    private ArrayList<BaseBuildings> largeBuildingList = new ArrayList<>();
    BaseCharacter charCon, charVol, charAss, charMag, charRoi, charArchi, charMar, charEve;

    @BeforeEach
    void setUp() {
        mentalist = (MentalistBot) bot.createBot(5);
        joueurMockTest = mock(Player.class);
        mentalist.updateCurrentPlayer(joueurMockTest);

        //Mock joueurs
        joueurMock1 = mock(Player.class);
        otherPlayers.add(joueurMock1);
        joueurMock2 = mock(Player.class);
        otherPlayers.add(joueurMock2);
        joueurMock3 = mock(Player.class);
        otherPlayers.add(joueurMock3);
        joueurMock4 = mock(Player.class);
        otherPlayers.add(joueurMock4);
        joueurMock5 = mock(Player.class);
        otherPlayers.add(joueurMock5);
        joueurMock6 = mock(Player.class);
        otherPlayers.add(joueurMock6);

        //Roles
        charCon = condottiere.createCharacter();
        roles.add(charCon);
        charVol = voleur.createCharacter();
        roles.add(charVol);
        charAss = assassin.createCharacter();
        roles.add(charAss);
        charMag = magicien.createCharacter();
        roles.add(charMag);
        charRoi = roi.createCharacter();
        roles.add(charRoi);
        charArchi = architecte.createCharacter();
        roles.add(charArchi);
        charMar = marchand.createCharacter();
        roles.add(charMar);
        charEve = eveque.createCharacter();
        roles.add(charEve);

        //Listes de bâtiments
        for (int i = 1; i< 2; i ++){
            smallBuildingList.add(new BaseBuildings("Bat" + i,i,i,""));
        }
        for (int i = 1; i< 5; i ++){
            mediumBuildingList.add(new BaseBuildings("Bat" + i,i,i,""));
        }
        for (int i = 1; i< 8; i ++){
            largeBuildingList.add(new BaseBuildings("Bat" + i,i,i,""));
        }

        //Joueur testé
        when(joueurMockTest.getBuildingsBuilt()).thenReturn(mediumBuildingList);
        when(joueurMockTest.getGold()).thenReturn(2);
        when(joueurMockTest.getBuildingsOfThisPlayer()).thenReturn(mediumBuildingList);
        when(joueurMockTest.getGamePlayers()).thenReturn(otherPlayers);

        //Joueur1
        when(joueurMock1.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock1.getGold()).thenReturn(2);
        when(joueurMock1.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);

        //Joueur2
        when(joueurMock2.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock2.getGold()).thenReturn(2);
        when(joueurMock2.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);

        //Joueur3
        when(joueurMock3.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock3.getGold()).thenReturn(2);
        when(joueurMock3.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);

        //Joueur4
        when(joueurMock4.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock4.getGold()).thenReturn(2);
        when(joueurMock4.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);

        //Joueur5
        when(joueurMock5.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock5.getGold()).thenReturn(2);
        when(joueurMock5.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);

        //Joueur6
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(2);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);


    }

    @Test
    void getTargetTest() {
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(4);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(joueurMock6, mentalist.getTarget());
    }

    @Test
    void getScenarioTest_1(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(1, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_2(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(2, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_3(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(3, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_4(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(4, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_5(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(5, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_6(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(6, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_7(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(7, mentalist.getScenario());
    }

    @Test
    void getScenarioTest_8(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(8, mentalist.getScenario());
    }

    @Test
    void wannaBuildTest_1(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(true, mentalist.wannaBuild());
    }

    @Test
    void wannaBuildTest_2(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(true, mentalist.wannaBuild());
    }

    @Test
    void wannaBuildTest_3(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(true, mentalist.wannaBuild());;
    }

    @Test
    void wannaBuildTest_4(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(true, mentalist.wannaBuild());
    }

    @Test
    void wannaBuildTest_5(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(false, mentalist.wannaBuild());
    }

    @Test
    void wannaBuildTest_6(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(false, mentalist.wannaBuild());
    }

    @Test
    void wannaBuildTest_7(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(true, mentalist.wannaBuild());
    }

    @Test
    void wannaBuildTest_8(){
        when(joueurMockTest.canBuildABuilding()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(true, mentalist.wannaBuild());
    }

    @Test
    void chooseCharacter_3(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(charMag, mentalist.chooseCharacter(roles));
        roles.remove(charMag);
        assertEquals(charArchi, mentalist.chooseCharacter(roles));
    }

    @Test
    void chooseCharacter_4(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        roles.remove(charArchi);
        assertEquals(charCon, mentalist.chooseCharacter(roles));
    }

    @Test
    void chooseCharacter_5(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(charVol, mentalist.chooseCharacter(roles));
    }

    @Test
    void drawOrGold_1(){
        when(joueurMockTest.imposableBuildings()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(true, mentalist.drawOrGold());
    }

    @Test
    void drawOrGold_2(){
        when(joueurMockTest.imposableBuildings()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(true, mentalist.drawOrGold());
    }

    @Test
    void drawOrGold_3(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(false, mentalist.drawOrGold());
    }

    @Test
    void drawOrGold_4(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(false, mentalist.drawOrGold());
    }

    @Test
    void drawOrGold_5(){
        when(joueurMockTest.imposableBuildings()).thenReturn(true);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(true, mentalist.drawOrGold());
    }

    @Test
    void drawOrGold_6(){
        when(joueurMockTest.imposableBuildings()).thenReturn(false);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(false, mentalist.wannaBuild());
    }

    @Test
    void drawOrGold_7(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(false, mentalist.drawOrGold());
    }

    @Test
    void drawOrGold_8(){
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(false, mentalist.drawOrGold());
    }

    @Test
    void chooseBuildingsTest_1() {
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(1, mentalist.chooseBuildings(smallBuildingList).getPrice());
    }

    @Test
    void chooseBuildingsTest_7() {
        when(joueurMockTest.getGold()).thenReturn(100);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(smallBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(4, mentalist.chooseBuildings(mediumBuildingList).getPrice());
    }

    @Test
    void chooseBuildingsToBuild_lowest() {
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(3);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(largeBuildingList);
        assertEquals(1, mentalist.chooseBuildingsToBuild(smallBuildingList).getPrice());
    }

    @Test
    void chooseBuildingsToBuild_highest() {
        when(joueurMockTest.getGold()).thenReturn(100);
        when(joueurMock6.getBuildingsBuilt()).thenReturn(largeBuildingList);
        when(joueurMock6.getGold()).thenReturn(1);
        when(joueurMock6.getBuildingsOfThisPlayer()).thenReturn(smallBuildingList);
        assertEquals(4, mentalist.chooseBuildingsToBuild(mediumBuildingList).getPrice());
    }

    @Test
    void architecteTurn() {
        ArrayList<String> listOfTurns = mentalist.architecteTurn();
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(1));
    }

    @Test
    void assassinTurn() {
        ArrayList<String> listOfTurns = mentalist.assassinTurn();
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test
    void condottiereTurn() {
        ArrayList<String> listOfTurns = mentalist.assassinTurn();
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test
    void evequeTurn() {
        ArrayList<String> listOfTurns = mentalist.assassinTurn();
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test
    void magicienTurn() {
    }

    @Test
    void marchandTurn() {
        ArrayList<String> listOfTurns = mentalist.assassinTurn();
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test
    void roiTurn() {
        ArrayList<String> listOfTurns = mentalist.assassinTurn();
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test
    void voleurTurn() {
        ArrayList<String> listOfTurns = mentalist.assassinTurn();
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }
}