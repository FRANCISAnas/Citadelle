package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.ReadJSONFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ArchitecteTest {
    private CharacterEnum character = CharacterEnum.Architecte;
    private CharacterEnum hacker = CharacterEnum.Voleur;
    private BaseCharacter architecte;
    private BaseCharacter voleur;
    private Player activePlayer;
    private ArrayList<BaseBuildings> cardsOfBuildings;
    private ArrayList<Object> buildingstoBuild = new ArrayList<>();

    @BeforeEach
    void setUp() {
        architecte = character.createCharacter();
        voleur = hacker.createCharacter();
        BaseBot bot = BotsType.BasicBot.createBot(1);
        assert bot != null;
        activePlayer = new Player(bot,1);
        activePlayer.setCharacter(architecte);
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        Collections.shuffle(cardsOfBuildings);
        activePlayer.setGold(1000);
    }
    @AfterEach void clear(){
        buildingstoBuild.clear();
        activePlayer.getBuildingsOfThisPlayer().clear();
        activePlayer.getBuildingsBuilt().clear();
    }

    @Test
    void goodPriority(){
        assertEquals(CharacterEnum.ARCHITECTEPRIORITY,activePlayer.getCharacter().getPriority());
    }

    @Test void ArchitectDrawPower() {
        int tailleDeck = cardsOfBuildings.size();
        int tailleMain = activePlayer.getBuildingsOfThisPlayer().size();
        architecte.passivePower(null, null, cardsOfBuildings, activePlayer);//l'architecte pioche 2 cartes en plus
        assertEquals(tailleDeck - 2, cardsOfBuildings.size());//elles sont bien enlevée du deck
        assertEquals(tailleMain + 2, activePlayer.getBuildingsOfThisPlayer().size());//elles sont bien ajoutées dans sa main
    }
    @Test void oneCardInDraw() {
        cardsOfBuildings.clear();
        cardsOfBuildings.add(new BaseBuildings("1",1,1, BuildingsTypeEnum.MILITAIRE.toString()));
        architecte.passivePower(null, null, cardsOfBuildings, activePlayer);// si il y a plus qu'une carte dans le deck
        assertEquals(0, cardsOfBuildings.size());//il en pioche qu'une
        assertEquals(1, activePlayer.getBuildingsOfThisPlayer().size());
    }
    @Test void emptyDraw() {
        cardsOfBuildings.clear();
        architecte.passivePower(null, null, cardsOfBuildings, activePlayer);// si il n'y a plus de cartes dans le deck
        assertEquals(0, cardsOfBuildings.size());//il n'en pioche pas
        assertEquals(0, activePlayer.getBuildingsOfThisPlayer().size());
    }
    @Test void notArchitectTryToActive() {
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        int tailleDeck = cardsOfBuildings.size();
        activePlayer.setCharacter(voleur);//si c'est un autre personnage, la fonction ne fait rien.
        architecte.passivePower(null, null, cardsOfBuildings, activePlayer);
        assertEquals(tailleDeck, cardsOfBuildings.size());//il n'en pioche pas
        assertEquals(0, activePlayer.getBuildingsOfThisPlayer().size());
    }
    @Test void ArchitectBuilding2Buildings() {
        buildingstoBuild.add(new BaseBuildings("1",1,1,BuildingsTypeEnum.NOBLE.toString()));
        buildingstoBuild.add(new BaseBuildings("2",1,1,BuildingsTypeEnum.NOBLE.toString()));
        activePlayer.setBuildingsOfThisPlayer(cardsOfBuildings);
        architecte.activePower(buildingstoBuild, null, null, activePlayer);
        assertEquals(2, activePlayer.getBuildingsBuilt().size());//construction de 2 bat
    }
    @Test void ArchitectCantBuild3Buildings() {
        buildingstoBuild.addAll(activePlayer.drawNBuildings(cardsOfBuildings,3));//veut construire 3 bat en plus -> erreur
        architecte.activePower(buildingstoBuild, null, null, activePlayer);
        assertEquals(0, activePlayer.getBuildingsBuilt().size());//construction de 0 bat car 3>2
    }
    @Test void wantToBuild1more() {
        activePlayer.setBuildingsOfThisPlayer(cardsOfBuildings);
        buildingstoBuild.addAll(activePlayer.drawNBuildings(cardsOfBuildings, 1));//veut construire 1 bat en plus
        architecte.activePower(buildingstoBuild, null, null, activePlayer);
        assertEquals(1, activePlayer.getBuildingsBuilt().size());//construction de 1 bat
    }
    @Test void dontWantToBuildMore() {
        buildingstoBuild.addAll(activePlayer.drawNBuildings(cardsOfBuildings, 0));//veut construire 0 bat en plus -> ne fait rien
        architecte.activePower(buildingstoBuild, null, null, activePlayer);
        assertEquals(0, activePlayer.getBuildingsBuilt().size());//construction de 0 bat
    }
    @Test void notEnoughGold() {
        activePlayer.setGold(1);//s'il a pas assez d'argent
        //veut construire 0 bat en plus -> ne fait rien
        activePlayer.setBuildingsOfThisPlayer(cardsOfBuildings);
        buildingstoBuild.add(new BaseBuildings("",1,1,""));
        buildingstoBuild.add(new BaseBuildings("",1,1,""));
        architecte.activePower(buildingstoBuild, null, null, activePlayer);
        assertEquals(1, activePlayer.getBuildingsBuilt().size());// il en construit 1 mais pas 2
    }
    @Test void notArchitectTryPassive() {
        buildingstoBuild.addAll(activePlayer.drawNBuildings(cardsOfBuildings, 1));//veut construire 1 bat en plus
        activePlayer.setCharacter(voleur);//si c'est pas l'architecte
        architecte.activePower(buildingstoBuild, null, null, activePlayer);
        assertEquals(0, activePlayer.getBuildingsBuilt().size());// il ne fait rien
    }

}