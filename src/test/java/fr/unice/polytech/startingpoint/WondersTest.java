package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class WondersTest {
    private Player player;
    private BaseBot bot = BotsType.UncleSamBot.createBot(1);
    private ArrayList<BaseBuildings> cardsOfBuildings;

    @BeforeEach
    void setUp(){
        assert bot != null;
        player = new Player(bot,1);
        bot.updateCurrentPlayer(player);
        player.setGold(100);
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        Collections.shuffle(cardsOfBuildings);
    }

    @Test void courDesMiraclesTest(){
        ArrayList<BaseBuildings> ville = new ArrayList<>();
        ville.add(new BaseBuildings("Bat1",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        ville.add(new BaseBuildings("Bat2",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        ville.add(new BaseBuildings("Bat3",1,1,BuildingsTypeEnum.MARCHAND.toString()));
        ville.add(new BaseBuildings("Bat4",1,1, BuildingsTypeEnum.NOBLE.toString()));
        ville.add(new BaseBuildings("Bat5",1,1, BuildingsTypeEnum.MERVEILLE.toString()));
        BaseBuildings cour = new BaseBuildings(MerveilleEnum.COURDESMIRACLES.toString(), 2, 2, BuildingsTypeEnum.MERVEILLE.toString());
        ville.add(cour);
        for (BaseBuildings building : ville){
            player.useBuilding(building);
        }
        player.setScore();
        assertEquals(BuildingsTypeEnum.RELIGIEUX.toString(), cour.getType());//choisit bien le type qui permet d'avoir le bonus allType
    }

    @Test
    void donjonTest(){
        BaseCharacter charCondottiere = CharacterEnum.Condottiere.createCharacter();
        Player player2 = new Player(bot,2);
        player2.setCharacter(charCondottiere);
        BaseBuildings donjon = new BaseBuildings(MerveilleEnum.DONJON.toString(),3,3,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(donjon);
        ArrayList<Object> autresParametres = new ArrayList<>();
        autresParametres.add(player);autresParametres.add(donjon);
        player2.getCharacter().passivePower(autresParametres,null,null,player2);
        assertEquals(donjon,player.getBuildingsBuilt().get(0));
    }

    @Test
    void laboratoireTest(){
        ArrayList<BaseBuildings> hand =new ArrayList<>();
        LaboratoireBuilding laboratoire = new LaboratoireBuilding(MerveilleEnum.LABORATOIRE.toString(),5,5,BuildingsTypeEnum.MERVEILLE.toString());
        hand.add(laboratoire);
        hand.add(new BaseBuildings("Bat1",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        hand.add(new BaseBuildings("Bat2",2,1,BuildingsTypeEnum.MILITAIRE.toString()));
        hand.add(new BaseBuildings("Bat3",3,1,BuildingsTypeEnum.MARCHAND.toString()));
        hand.add(new BaseBuildings("Bat4",4,1, BuildingsTypeEnum.NOBLE.toString()));
        hand.add(new BaseBuildings("Bat5",5,1, BuildingsTypeEnum.MERVEILLE.toString()));
        player.setBuildingsOfThisPlayer(hand);
        player.useBuilding(laboratoire);
        player.setGold(4);
        BaseBuildings buildingToDiscard = bot.laboratoryDecision();
        ArrayList<BaseBuildings> buildings = new ArrayList<>();
        buildings.add(buildingToDiscard);
        player.getLaboratoire().useWonderAbility(player,buildings);
        assertFalse(player.getBuildingsOfThisPlayer().remove(buildingToDiscard));
        assertEquals(5,player.getGold());
        assertTrue(laboratoire.getUsed());
    }

    @Test void manufactureTestNormal(){
        ManufactureBuilding manufacture = new ManufactureBuilding(MerveilleEnum.MANUFACTURE.toString(),5,5,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(manufacture);
        player.setGold(5);
        player.getManufacture().useWonderAbility(player,cardsOfBuildings);
        assertEquals(2,player.getGold());
        assertEquals(3,player.getNbOfbuildingsInHisHand());
        assertTrue(manufacture.getUsed());
    }
    @Test void manufactureTestNotEnoughGold(){
        ManufactureBuilding manufacture = new ManufactureBuilding(MerveilleEnum.MANUFACTURE.toString(),5,5,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(manufacture);
        player.setGold(2);
        player.getManufacture().useWonderAbility(player,cardsOfBuildings);
        assertEquals(2,player.getGold());
        assertEquals(0,player.getNbOfbuildingsInHisHand());
        assertFalse(manufacture.getUsed());
    }

    @Test
    void observatoireTest(){
        ObservatoireBuilding observatoire = new ObservatoireBuilding(MerveilleEnum.OBSERVATOIRE.toString(),5,5,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(observatoire);
        int cardsOfBuildingsSize = cardsOfBuildings.size();
        player.getObservatoire().useWonderAbility(player,cardsOfBuildings);
        assertEquals(1,player.getNbOfbuildingsInHisHand());
        assertEquals(cardsOfBuildingsSize - 1,cardsOfBuildings.size());
    }

    @Test
    void cimetiereTest(){
        CimetiereBuilding cimetiere = new CimetiereBuilding(MerveilleEnum.CIMETIERE.toString(),5,5,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(cimetiere);
        BaseCharacter charCondottiere = CharacterEnum.Condottiere.createCharacter();
        BaseCharacter charMarchand = CharacterEnum.Marchand.createCharacter();
        player.setCharacter(charMarchand);
        Player player2 = new Player(bot,2);
        player2.setCharacter(charCondottiere);
        ArrayList<BaseBuildings> hand = new ArrayList<>();
        BaseBuildings buildingToDestroy = new  BaseBuildings("Bat6",1,1,BuildingsTypeEnum.MILITAIRE.toString());
        hand.add(new BaseBuildings("Bat1",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        hand.add(new BaseBuildings("Bat2",2,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        hand.add(new BaseBuildings("Bat3",3,1,BuildingsTypeEnum.MARCHAND.toString()));
        hand.add(new BaseBuildings("Bat4",4,1, BuildingsTypeEnum.NOBLE.toString()));
        hand.add(new BaseBuildings("Bat5",5,1, BuildingsTypeEnum.MERVEILLE.toString()));
        hand.add(buildingToDestroy);
        player.setBuildingsOfThisPlayer(hand);
        player.useBuilding(buildingToDestroy);
        assertEquals(5,player.getNbOfbuildingsInHisHand());
        assertEquals(2,player.getNbOfbuildingsInCity());//le cimetiere et le batiment ciblé
        ArrayList<Object> autresParametres = new ArrayList<>();
        autresParametres.add(player);autresParametres.add(buildingToDestroy);
        player.setGold(3);
        player2.getCharacter().passivePower(autresParametres,null,null,player2);
        assertEquals(6,player.getNbOfbuildingsInHisHand());
        assertEquals(1,player.getNbOfbuildingsInCity());
        assertTrue(player.getBuildingsOfThisPlayer().remove(buildingToDestroy));
        assertFalse(player.getBuildingsBuilt().remove(buildingToDestroy));
        assertEquals(2,player.getGold());
    }

    @Test
    void bibliothequeTest(){
        BibliothequeBuilding bibliotheque = new BibliothequeBuilding(MerveilleEnum.BIBLIOTHEQUE.toString(),6,6,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(bibliotheque);
        int cardsOfBuildingsSize = cardsOfBuildings.size();
        player.getBibliotheque().useWonderAbility(player,cardsOfBuildings);
        assertEquals(2,player.getNbOfbuildingsInHisHand());
        assertEquals(cardsOfBuildingsSize - 2,cardsOfBuildings.size());

    }

    @Test
    void ecoleDeMagieTest(){
        WonderBuilding ecoleDeMagie = new WonderBuilding(MerveilleEnum.ECOLEMAGIE.toString(),6,6,BuildingsTypeEnum.MERVEILLE.toString());
        player.useBuilding(ecoleDeMagie);
        BaseCharacter charMarchand = CharacterEnum.Marchand.createCharacter();
        player.setCharacter(charMarchand);
        player.setGold(3);
        player.getCharacter().activePower(null,null,null,player);
        assertEquals(4,player.getGold());
    }

    @Test
    void universiteTest() {
        BaseBuildings universite = null;
        for (BaseBuildings building : cardsOfBuildings) {
            if (building.getName().equals("Universite")) {
                universite = building;
            }
            if (universite != null) {
                assertEquals(8, universite.getVictoryPoints());
                assertEquals(6, universite.getPrice());
            }
        }
    }

    @Test
    void dracoportTest(){
        BaseBuildings dracoport = null;
        for (BaseBuildings building : cardsOfBuildings) {
            if (building.getName().equals("Dracoport")) {
                dracoport = building;
            }
            if (dracoport != null) {
                assertEquals(8, dracoport.getVictoryPoints());
                assertEquals(6, dracoport.getPrice());
            }
        }
    }
    @Test void resetAllWonders(){
        ArrayList<BaseBuildings> hand =new ArrayList<>();
        ManufactureBuilding manufacture = new ManufactureBuilding(MerveilleEnum.MANUFACTURE.toString(),5,5,BuildingsTypeEnum.MERVEILLE.toString());
        hand.add(manufacture);
        hand.add(new BaseBuildings("Bat1",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        hand.add(new BaseBuildings("Bat2",2,1,BuildingsTypeEnum.MILITAIRE.toString()));
        hand.add(new BaseBuildings("Bat3",3,1,BuildingsTypeEnum.MARCHAND.toString()));
        hand.add(new BaseBuildings("Bat4",4,1, BuildingsTypeEnum.NOBLE.toString()));
        hand.add(new BaseBuildings("Bat5",5,1, BuildingsTypeEnum.MERVEILLE.toString()));
        player.setBuildingsOfThisPlayer(hand);
        player.useBuilding(manufacture);
        player.useBuilding(hand.get(0));
        manufacture.useWonderAbility(player,cardsOfBuildings);
        assertTrue(manufacture.getUsed());
        player.resetAllUsedWonder();
        assertFalse(manufacture.getUsed());
    }
}
