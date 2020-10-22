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

class BuilderBotTest {
    private BotsType bots = BotsType.BuilderBot;
    private BaseBot builder;
    private Player joueurMock;
    private ArrayList<BaseCharacter> availableChars;
    private BaseCharacter charRoi, charArch, charEveque,charVoleur,charAssassin,charCondottiere,charMagicien;
    @BeforeEach
    void setUp() {
        builder = bots.createBot(1);
        joueurMock = mock(Player.class);
        builder.updateCurrentPlayer(joueurMock);
        availableChars = new ArrayList<>();
        charRoi = CharacterEnum.Roi.createCharacter();
        charArch = CharacterEnum.Architecte.createCharacter();
        charEveque = CharacterEnum.Eveque.createCharacter();
        charVoleur = CharacterEnum.Voleur.createCharacter();
        charAssassin = CharacterEnum.Assassin.createCharacter();
        charCondottiere = CharacterEnum.Condottiere.createCharacter();
        charMagicien = CharacterEnum.Magicien.createCharacter();
        builder.getCurrentPlayer().setGold(5);
    }

    @Test
    void chooseCharacterArchitecte() {
        //Quand il y a un architecte
        availableChars.add(charRoi);
        availableChars.add(charEveque);
        availableChars.add(charArch);
        availableChars.add(charVoleur);
        availableChars.add(charCondottiere);
        availableChars.add(charMagicien);
        assertEquals(charArch, builder.chooseCharacter(availableChars));
    }
    @Test void chooseCharacterNotArchiButEveque(){
        availableChars.add(charRoi);
        availableChars.add(charEveque);
        availableChars.add(charVoleur);
        availableChars.add(charCondottiere);
        availableChars.add(charMagicien);
        assertEquals(charEveque, builder.chooseCharacter(availableChars));
    }
    @Test void chooseCharacterNothingWanted(){
        availableChars.add(charVoleur);
        availableChars.add(charRoi);
        availableChars.add(charCondottiere);
        availableChars.add(charMagicien);
        assertEquals(charVoleur, builder.chooseCharacter(availableChars));
    }

    @Test
    void chooseBuildingsTest() {
        ArrayList<BaseBuildings> pioche = new ArrayList<>();
        BaseBuildings cheapest = new BaseBuildings("Bat1",3,3,"");
        pioche.add(cheapest);
        pioche.add(new BaseBuildings("Bat1",10,10,""));
        //Le Builderbot choisit toujours le bâtiment le moins cher
        assertEquals(cheapest, builder.chooseBuildings(pioche));
    }

    @Test void chooseBuildingsToBuildLowerCostLotGold() {
        when(joueurMock.getGold()).thenReturn(30);
        when(joueurMock.canBuildABuilding()).thenReturn(true);
        ArrayList<BaseBuildings> main = new ArrayList<>();
        main.add(new BaseBuildings("Bat1",10,10,""));
        BaseBuildings cheapest = new BaseBuildings("Bat2",3,3,"");
        main.add(cheapest);
        main.add(new BaseBuildings("Bat3",20,10,""));
        main.add(new BaseBuildings("Bat4",4,10,""));
        assertEquals(cheapest,builder.chooseBuildingsToBuild(main));
    }
    @Test void chooseBuildingsToBuildLowerCostFewGold() {
        when(joueurMock.getGold()).thenReturn(4);
        when(joueurMock.canBuildABuilding()).thenReturn(true);
        ArrayList<BaseBuildings> main = new ArrayList<>();
        main.add(new BaseBuildings("Bat1",10,10,""));
        BaseBuildings cheapest = new BaseBuildings("Bat2",3,3,"");
        main.add(cheapest);
        main.add(new BaseBuildings("Bat3",20,10,""));
        main.add(new BaseBuildings("Bat4",4,10,""));
        assertEquals(cheapest,builder.chooseBuildingsToBuild(main));
    }

    @Test void wannaBuildTest() {
        when(joueurMock.canBuildABuilding()).thenReturn(true);
        assertTrue(builder.wannaBuild());
        when(joueurMock.canBuildABuilding()).thenReturn(false);
        assertFalse(builder.wannaBuild());
    }

    @Test void drawOrGoldTest() {
        //Le builderBot ne choisit les batiments que si sa main est vide
        assertTrue(builder.drawOrGold());
        ArrayList<BaseBuildings> main = new ArrayList<>();
        main.add(new BaseBuildings("Bat1",10,10,""));
        when(joueurMock.getBuildingsOfThisPlayer()).thenReturn(main);
        assertFalse(builder.drawOrGold());
    }
    @Test void characterTurnTest(){
        ArrayList<String> turn = new ArrayList<>();
        turn.add(CharacterEnum.ACTIVEPOWER);turn.add(CharacterEnum.LABORATOIREABILITY);turn.add(CharacterEnum.BUILD);
        assertEquals(turn,builder.assassinTurn());
        assertEquals(turn,builder.voleurTurn());
        assertEquals(turn,builder.magicienTurn());
        assertEquals(turn,builder.roiTurn());
        assertEquals(turn,builder.evequeTurn());
        assertEquals(turn,builder.marchandTurn());
        assertEquals(turn,builder.condottiereTurn());
        turn.clear();
        turn.add(CharacterEnum.BUILD);turn.add(CharacterEnum.ACTIVEPOWER);turn.add(CharacterEnum.LABORATOIREABILITY);
        assertEquals(turn,builder.architecteTurn());
    }
    @Test void activePowerTestAssassin(){
        ArrayList<Object> autresParametres = new ArrayList<>();
        when(joueurMock.getCharacter()).thenReturn(charAssassin);
        autresParametres.add(CharacterEnum.CONDOTTIEREPRIORITY);
        assertEquals(autresParametres,builder.doActivePower());
    }
    @Test void activePowerTestVoleur(){
        ArrayList<Object> autresParametres = new ArrayList<>();
        when(joueurMock.getCharacter()).thenReturn(charVoleur);
        autresParametres.add(CharacterEnum.MAGICIENPRIORITY);
        assertEquals(autresParametres,builder.doActivePower());
    }

    @Test void activePowerTestArchitecte2MoreBuildings(){
        Player currentPlayer = new Player(builder,1);
        builder.updateCurrentPlayer(currentPlayer);
        ArrayList<BaseBuildings> main = new ArrayList<>();
        ArrayList<BaseBuildings> architecteChoice = new ArrayList<>();
        currentPlayer.setCharacter(charArch);
        currentPlayer.setGold(8);
        main.add(new BaseBuildings("Bat1",10,10,""));
        BaseBuildings building1 = new BaseBuildings("Bat2",3,3,"");main.add(building1);
        main.add(new BaseBuildings("Bat3",20,10,""));
        BaseBuildings building2 = new BaseBuildings("Bat4",4,10,"");main.add(building2);
        currentPlayer.setBuildingsOfThisPlayer(main);
        architecteChoice.add(building1);architecteChoice.add(building2);
        assertEquals(architecteChoice,((BuilderBot) builder).architecteBuildingschoice());
        ArrayList<Object> autresParametres = new ArrayList<>(architecteChoice);
        assertEquals(autresParametres,builder.doActivePower());
    }
    @Test void activePowerTestArchitecte1MoreBuildings(){
        Player currentPlayer = new Player(builder,1);
        builder.updateCurrentPlayer(currentPlayer);
        ArrayList<BaseBuildings> main = new ArrayList<>();
        ArrayList<BaseBuildings> architecteChoice = new ArrayList<>();
        currentPlayer.setCharacter(charArch);
        currentPlayer.setGold(3);
        main.add(new BaseBuildings("Bat1",10,10,""));
        BaseBuildings building1 = new BaseBuildings("Bat2",3,3,"");main.add(building1);
        main.add(new BaseBuildings("Bat3",20,10,""));
        BaseBuildings building2 = new BaseBuildings("Bat4",4,10,"");main.add(building2);
        currentPlayer.setBuildingsOfThisPlayer(main);
        architecteChoice.add(building1);
        assertEquals(architecteChoice,((BuilderBot) builder).architecteBuildingschoice());
        ArrayList<Object> autresParametres = new ArrayList<>(architecteChoice);
        assertEquals(autresParametres,builder.doActivePower());
    }
    @Test void passivePowerTestDoNothing(){
        when(joueurMock.getCharacter()).thenReturn(charMagicien);
        assertNull(builder.doPassivePower());
        when(joueurMock.getCharacter()).thenReturn(charCondottiere);
        assertNull(builder.doPassivePower());
    }

    @Test void laboratoryDecisionTest(){
        Player currentPlayer = new Player(builder,1);
        builder.updateCurrentPlayer(currentPlayer);
        ArrayList<BaseBuildings> main = new ArrayList<>();
        main.add(new BaseBuildings("Bat1",10,10,""));
        main.add(new BaseBuildings("Bat2",3,3,""));
        BaseBuildings higherCost = new BaseBuildings("Bat3",20,10,"");main.add(higherCost);
        main.add(new BaseBuildings("Bat4",4,10,""));
        currentPlayer.setBuildingsOfThisPlayer(main);
        assertEquals(higherCost,builder.laboratoryDecision());
    }
}