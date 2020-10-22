package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.CharactersPackages.Architecte;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.City;
import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.ReadJSONFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author FRANCIS Anas
 * \brief Cette classe test le bot UncleSamBot, On vérifie si notre bot récupère le maximum d'argent possible lorsu'il en a pas et pui il prend bien
 * l'architecte pour contruire des bâtiments.
 * created 19/11/2019
 */
class UncleSamBotTest {
    private BotsType bots = BotsType.UncleSamBot;
    private BaseBot unclesambot,bot2, bot3, bot4, bot5;
    private Player activePlayer;
    private CharacterEnum condottiere = CharacterEnum.Condottiere;
    private CharacterEnum voleur = CharacterEnum.Voleur;
    private CharacterEnum assassin = CharacterEnum.Assassin;
    private CharacterEnum magicien = CharacterEnum.Magicien;
    private CharacterEnum roi = CharacterEnum.Roi;
    private CharacterEnum eveque = CharacterEnum.Eveque;
    private CharacterEnum marchand = CharacterEnum.Marchand;
    private CharacterEnum architecte = CharacterEnum.Architecte;

    private ArrayList<BaseCharacter> availableChars;
    private BaseCharacter charCon, charVol, charAss, charMag, charRoi, charEveq, charMarch, charArchit;

    private BaseCharacter characterChosen;
    private BaseBuildings buildingChosen;

    private ArrayList<BaseBuildings> buildingsInTheHand, cardsOfBuildings;
    private City cityMock;

    private ArrayList<Player> otherPlayers = new ArrayList<>(), otherPlayersNotMocked = new ArrayList<>();

    private Player p2, p3, p4, p5;
    private Player mockp2, mockp3, mockp4, mockp5;

    @BeforeEach
    void setUp() {
        unclesambot = bots.createBot(1);
        activePlayer = new Player(unclesambot,1);

        bot2 = bots.createBot(2);
        bot3 = bots.createBot(3);
        bot4 = bots.createBot(4);
        bot5 = bots.createBot(5);

        p2 =  new Player(bot2,2);
        p3 =  new Player(bot3,3);
        p4 =  new Player(bot4,4);
        p5 =  new Player(bot5,5);

        mockp2 = mock(Player.class);
        mockp3 = mock(Player.class);
        mockp4 = mock(Player.class);
        mockp5 = mock(Player.class);

        bot2.updateCurrentPlayer(p2);
        bot3.updateCurrentPlayer(p3);
        bot4.updateCurrentPlayer(p4);
        bot5.updateCurrentPlayer(p5);

        availableChars = new ArrayList<>();
        characterChosen = null;
        buildingChosen = null;

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
        charArchit = architecte.createCharacter();
        charMarch = marchand.createCharacter();

        Collections.shuffle(cardsOfBuildings);

        unclesambot.updateCurrentPlayer(activePlayer);
        cityMock = mock(City.class);
        when(cityMock.getNbrBuildings()).thenReturn(1);//nb différent de 0
        activePlayer.setCity(cityMock);
        activePlayer.setGold(1000);

        availableChars.clear();
        availableChars.add(charArchit);availableChars.add(charAss);availableChars.add(charCon);availableChars.add(charEveq);
        availableChars.add(charMag);availableChars.add(charMarch);availableChars.add(charRoi);availableChars.add(charVol);

        otherPlayers.add(activePlayer);otherPlayers.add(mockp2);otherPlayers.add(mockp3);otherPlayers.add(mockp4);otherPlayers.add(mockp5);
        activePlayer.setGamePlayers(otherPlayers);mockp2.setGamePlayers(otherPlayers);mockp3.setGamePlayers(otherPlayers);
        mockp4.setGamePlayers(otherPlayers);mockp5.setGamePlayers(otherPlayers);

        otherPlayersNotMocked.add(p2);otherPlayersNotMocked.add(p3);otherPlayersNotMocked.add(p4);otherPlayersNotMocked.add(p5);
        p2.setGamePlayers(otherPlayersNotMocked);p3.setGamePlayers(otherPlayersNotMocked);
        p4.setGamePlayers(otherPlayersNotMocked);p5.setGamePlayers(otherPlayersNotMocked);
    }

    @Test void wannaBuildIfCanBuild() {
        unclesambot.updateCurrentPlayer(mockp2);
        when(mockp2.canBuildABuilding()).thenReturn(true);
        assertTrue(unclesambot.wannaBuild());
    }
    @Test void wannaBuildSIfCantBuild() {
        unclesambot.updateCurrentPlayer(mockp2);
        when(mockp2.canBuildABuilding()).thenReturn(false);
        assertFalse(unclesambot.wannaBuild());
    }

    /**
     * @author FRANCIS Anas
     * * \brief On test si le bot n'a pas assez d'argent il va essayer d'en faire en prenant le personnage qui peut lui apporter le plus en fonction des bâtiment
     * de sa cité.
     *
     *         ArrayList<BaseCharacter> availableCharacters3= new ArrayList<>(availableChars);
     *         ArrayList<BaseCharacter> availableCharacters4= new ArrayList<>(availableChars);
     */
    @Test
    void chooseCharacterMostprofitable() {//ordre : religieux,marchand,noble,militaire
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(0);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(5);
        when(cityMock.getNbrNobleBuildings()).thenReturn(1);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(8);
        characterChosen = unclesambot.chooseCharacter(availableChars);
        assertEquals(CharacterEnum.EVEQUEPRIORITY, characterChosen.getPriority());
    }
    @Test void chooseCharacterMissingCharacter() {//ordre : militaire,religieux,noble,marchand
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(6);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);
        when(cityMock.getNbrNobleBuildings()).thenReturn(1);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(3);
        availableChars.remove(charCon); availableChars.remove(charEveq);//condottiere et eveque non disponobles donc choisit roi
        characterChosen = unclesambot.chooseCharacter(availableChars);
        assertEquals(CharacterEnum.ROIPRIORITY, characterChosen.getPriority());
    }
    @Test void chooseCharacterNoCharacterGivingGold() {//ordre : militaire,religieux
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(6);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(3);
        availableChars.remove(charCon);availableChars.remove(charEveq);
        characterChosen = unclesambot.chooseCharacter(availableChars);//n'a pas condottiere et eveque de disponible donc choisit l'architecte par défault
        assertEquals(CharacterEnum.ARCHITECTEPRIORITY, characterChosen.getPriority());
    }
    @Test void chooseCharacterNoBuildingBuilt() {
        when(cityMock.getNbrBuildings()).thenReturn(0);//n'a rien de construit: choisit le marchand par défault
        characterChosen = unclesambot.chooseCharacter(availableChars);
        assertEquals(CharacterEnum.MARCHANDPRIORITY, characterChosen.getPriority());
    }
    @Test void chooseCharacterCharacterAndBuildingMissing(){//ordre :
        availableChars.remove(charCon);availableChars.remove(charEveq);availableChars.remove(charArchit);//n'a rien de disponible donc choisit le marchand
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(1);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(1);
        characterChosen = unclesambot.chooseCharacter(availableChars);
        assertEquals(CharacterEnum.MARCHANDPRIORITY,characterChosen.getPriority());
    }

    @Test void drawOrGoldEmptyHand() {
        assertTrue(unclesambot.drawOrGold());
    }
    @Test void drawOrGoldFilledHand(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,1));
        assertFalse(unclesambot.drawOrGold());
    }
    @Test void drawOrGoldFilledHandTaxableBuilding(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,1));
        when(cityMock.isBuild(any())).thenReturn(true);
        assertTrue(unclesambot.drawOrGold());
    }

    /**
     * @author FRANCIS Anas
     * \brief tester le choix de construction de bâtiment, On construit le bâtiment de même type que le nombre de bâtiments max dans notre cité.
     */

    @Test void chooseMostProfitableBuildingToBuild() {
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        Collections.shuffle(buildingsInTheHand);
        //ordre : Religieux, noble, marchand, militaire
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(5);
        when(cityMock.getNbrNobleBuildings()).thenReturn(1);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(6);//il a tout les types dans la main il choisit donc le religieux
        buildingChosen = unclesambot.chooseBuildingsToBuild(buildingsInTheHand);
        assertEquals(BuildingsTypeEnum.RELIGIEUX.toString(), buildingChosen.getType());
    }

    @Test void chooseBuildingsToBuildWithCorrespondingCharacter() {
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(0);//il a rien construit
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(0);
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        Collections.shuffle(buildingsInTheHand);
        activePlayer.setCharacter(charCon);//il a le condottiere
        buildingChosen = unclesambot.chooseBuildingsToBuild(buildingsInTheHand);//il a tout les types dans la main et il a le condottiere
        assertEquals(BuildingsTypeEnum.MILITAIRE.toString(), buildingChosen.getType());
    }
    @Test void chooseBuildingsToBuildIncompleteHand() {
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);//ordre : noble, marchand, religieux, militaire
        when(cityMock.getNbrCommercialBuildings()).thenReturn(5);
        when(cityMock.getNbrNobleBuildings()).thenReturn(6);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(4);
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MERVEILLE.toString()));
        Collections.shuffle(buildingsInTheHand);
        buildingChosen = unclesambot.chooseBuildingsToBuild(buildingsInTheHand);//il a pas de marchands ou nobles donc il prend religieux
        assertEquals(BuildingsTypeEnum.RELIGIEUX.toString(), buildingChosen.getType());
    }
    @Test void chooseBuildingsToBuildWithNotEnoughMoney(){
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);//ordre : Religieux, noble, marchand, militaire
        when(cityMock.getNbrCommercialBuildings()).thenReturn(6);
        when(cityMock.getNbrNobleBuildings()).thenReturn(5);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(4);
        buildingsInTheHand.add(new BaseBuildings("",100000,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("",1,1,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("",1,1,BuildingsTypeEnum.MERVEILLE.toString()));
        Collections.shuffle(buildingsInTheHand);
        buildingChosen = unclesambot.chooseBuildingsToBuild(buildingsInTheHand);//il a pas de marchands ni nobles et il a pas assez de sous pour religieu donc il prend militaire
        assertEquals(BuildingsTypeEnum.MILITAIRE.toString(), buildingChosen.getType());
        assertTrue(buildingChosen.getPrice()<= activePlayer.getGold());
    }


    @Test void hasTypeInHandGoodTypeVerification() {//doit renvoyer un batiment constructible d'un certain type parmis une liste.
        activePlayer.setGold(5);
        buildingsInTheHand = cardsOfBuildings;
        BaseBuildings buildingWithChosenType1 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MILITAIRE.toString(), buildingsInTheHand);
        BaseBuildings buildingWithChosenType2 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(), buildingsInTheHand);
        BaseBuildings buildingWithChosenType3 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.NOBLE.toString(), buildingsInTheHand);
        BaseBuildings buildingWithChosenType4 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.RELIGIEUX.toString(), buildingsInTheHand);
        BaseBuildings buildingWithChosenType5 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MERVEILLE.toString(), buildingsInTheHand);
        assertEquals(BuildingsTypeEnum.MILITAIRE.toString(), buildingWithChosenType1.getType());
        assertEquals(BuildingsTypeEnum.MARCHAND.toString(), buildingWithChosenType2.getType());
        assertEquals(BuildingsTypeEnum.NOBLE.toString(), buildingWithChosenType3.getType());
        assertEquals(BuildingsTypeEnum.RELIGIEUX.toString(), buildingWithChosenType4.getType());
        assertEquals(BuildingsTypeEnum.MERVEILLE.toString(), buildingWithChosenType5.getType());
        assertTrue(buildingWithChosenType1.getPrice() <= 5);
        assertTrue(buildingWithChosenType2.getPrice() <= 5);
        assertTrue(buildingWithChosenType3.getPrice() <= 5);
        assertTrue(buildingWithChosenType4.getPrice() <= 5);
        assertTrue(buildingWithChosenType5.getPrice() <= 5);
    }
    @Test void hasTypeInHandPasDargent(){
    activePlayer.setGold(0);//si il n'y a pas de batiment construisible
    buildingsInTheHand = cardsOfBuildings;
    BaseBuildings buildingWithChosenType1 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MILITAIRE.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType2= ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType3 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.NOBLE.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType4 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.RELIGIEUX.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType5 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MERVEILLE.toString(),buildingsInTheHand);
    assertNull(buildingWithChosenType1);
    assertNull(buildingWithChosenType2);
    assertNull(buildingWithChosenType3);
    assertNull(buildingWithChosenType4);
    assertNull(buildingWithChosenType5);
    }
    @Test void hasTypeInHandNotEnoughBuildingInHand(){
    activePlayer.setGold(5);//si il n'y a pas de batiment construisible
    BaseBuildings buildingWithChosenType1 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MILITAIRE.toString(),buildingsInTheHand);//main vide
    buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.RELIGIEUX.toString()));
    buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.NOBLE.toString()));
    buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
    buildingsInTheHand.add(new BaseBuildings("", 1, 1, BuildingsTypeEnum.MILITAIRE.toString()));
    BaseBuildings buildingWithChosenType2=  ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType3 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.NOBLE.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType4 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.RELIGIEUX.toString(),buildingsInTheHand);
    BaseBuildings buildingWithChosenType5 = ((UncleSamBot) unclesambot).hasTypeInHand(BuildingsTypeEnum.MERVEILLE.toString(),buildingsInTheHand);
    assertNull(buildingWithChosenType1);
    assertNull(buildingWithChosenType2);
    assertEquals(BuildingsTypeEnum.NOBLE.toString(), buildingWithChosenType3.getType());
    assertEquals(BuildingsTypeEnum.RELIGIEUX.toString(), buildingWithChosenType4.getType());
    assertNull(buildingWithChosenType5);
    }


    /**
     * @author FRANCIS Anas
     * \brief je teste si la méthode architecteBuildingschoice utilise bien le pouvoir du marchand
     */
    @Test
    void architecteBuildingsChoice(){
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("b",10,10,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("g",15,15,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("k",81,19,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("l",18,100,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m&ms",11,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("n",19,20,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("o",131,30,BuildingsTypeEnum.MILITAIRE.toString()));

        Collections.shuffle(buildingsInTheHand);
        activePlayer.setGold(1000);

        activePlayer.setCharacter(charArchit);
        activePlayer.setBuildingsOfThisPlayer(buildingsInTheHand);

        assertEquals(CharacterEnum.Architecte.name(), unclesambot.getCurrentPlayer().getCharacter().getName());//je vérifier si mon bot a bien le personnage Architecte

        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(5);
        when(cityMock.getNbrNobleBuildings()).thenReturn(6);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(4);

        ArrayList<BaseBuildings> chosenBuildings = ((UncleSamBot) unclesambot).architecteBuildingschoice();

        for(int i = 0 ;i<Architecte.NBMAXOFBUILDINGSOFARCHITECT; i++)assertEquals(BuildingsTypeEnum.NOBLE.toString(), chosenBuildings.get(i).getType());

        when(cityMock.getNbrReligiousBuildings()).thenReturn(4);
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(5);

        // on va vérifer si les deux deuxième bâtiments qu'il va prendre sont bien celle du marchand car il a a le plus des bâtiments
        chosenBuildings = ((UncleSamBot) unclesambot).architecteBuildingschoice();
        for(int i = 0; i<Architecte.NBMAXOFBUILDINGSOFARCHITECT; i++){
            assertEquals(BuildingsTypeEnum.MARCHAND.toString(), chosenBuildings.get(i).getType());
        }

        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(4);

        chosenBuildings = ((UncleSamBot) unclesambot).architecteBuildingschoice();
        for(int i = 0; i<Architecte.NBMAXOFBUILDINGSOFARCHITECT; i++){// pourquoi 2 parce que l'architecte peut bâtire 2 bâtiments de plus
            assertEquals(BuildingsTypeEnum.RELIGIEUX.toString(), chosenBuildings.get(i).getType());        }

        int length = buildingsInTheHand.size();
        //maintenant je vide sa main pour voir s'il va retourner null
        for (int i = 0; i<length; i++) {
            buildingsInTheHand.remove(0);
        }
        assertEquals(0, buildingsInTheHand.size());//je vérifie si sa main est vide
        assertNull(((UncleSamBot)unclesambot).architecteBuildingschoice());// je vérifier que le résultat retourner par la méthode architecteBuildingschoice est bien null
    }

    @Test
    void magicienPassivePowerTest(){

        unclesambot.getCurrentPlayer().setCharacter(charMag);

        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("k",81,19,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m&ms",11,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("n",19,20,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("o",131,30,BuildingsTypeEnum.MILITAIRE.toString()));

        when(cityMock.getNbrMilitaryBuildings()).thenReturn(3);
        when(cityMock.getNbrCommercialBuildings()).thenReturn(5);
        when(cityMock.getNbrNobleBuildings()).thenReturn(6);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(4);

        when(mockp2.getCharacter()).thenReturn(charCon);
        when(mockp3.getCharacter()).thenReturn(charAss);
        when(mockp4.getCharacter()).thenReturn(charArchit);
        when(mockp5.getCharacter()).thenReturn(charEveq);

        when(mockp2.getNbOfbuildingsInHisHand()).thenReturn(10);
        when(mockp3.getNbOfbuildingsInHisHand()).thenReturn(2);
        when(mockp4.getNbOfbuildingsInHisHand()).thenReturn(17);
        when(mockp5.getNbOfbuildingsInHisHand()).thenReturn(700);

        assertEquals(CharacterEnum.Magicien.toString() ,unclesambot.getCurrentPlayer().getCharacter().getName());
        Player joueurVictim = ((UncleSamBot) unclesambot).magicianPassivePower();

        assertEquals(mockp5.getID(), joueurVictim.getID());
        otherPlayers.remove(mockp5);

        assertEquals(mockp4.getID(), joueurVictim.getID());
        otherPlayers.remove(mockp4);

        assertEquals(mockp2.getID(), joueurVictim.getID());
        otherPlayers.remove(mockp2);

        assertEquals(mockp3.getID(), joueurVictim.getID());
}

    @Test
    void magicienActivePowerTest(){
        p4.setCharacter(charMag);

        p4.setGold(5000);

        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("k",81,19,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m&ms",11,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("n",19,20,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("o",131,30,BuildingsTypeEnum.MILITAIRE.toString()));

        p4.setBuildingsOfThisPlayer(buildingsInTheHand);

        for(int i = 0; i<6; i++){
            BaseBuildings building = new BaseBuildings("lol",i+5,i+1,BuildingsTypeEnum.NOBLE.toString());
            bot4.getCurrentPlayer().useBuilding(building);
        }

        assertEquals(CharacterEnum.Magicien.toString() ,p4.getCharacter().getName());

        ArrayList<BaseBuildings> unwantedBuildings = ((UncleSamBot)bot4).magicienActivePower();
        assertEquals(buildingsInTheHand.size(), unwantedBuildings.size());
        int length = unwantedBuildings.size();
        for (int i =0; i<length; i++)assertEquals(buildingsInTheHand.get(i), unwantedBuildings.get(i));

    }

    @Test
    void condottierePassivePowerTest() {
        p4.setCharacter(charCon);
        p4.setGold(5000);
        p3.setGold(5000);
        p2.setGold(5000);
        p5.setGold(5000);

        p2.setCharacter(charMag);
        p3.setCharacter(charArchit);
        p5.setCharacter(charAss);
        BaseBuildings cheapestBuildingForp2 = null, cheapestBuildingForp3 = null, cheapestBuildingForp4 = null, cheapestBuildingForp5 = null;
        int price, minimumPrice = 1000;
        for (int i = 0; i < 10; i++) {
            BaseBuildings myBuilding = new BaseBuildings("a"+i,100+i,30+i,BuildingsTypeEnum.MILITAIRE.toString());
            price = myBuilding.getPrice();
            p2.useBuilding(myBuilding);
            if (price < minimumPrice) {
                cheapestBuildingForp2 = myBuilding;
                minimumPrice = price;
            }
        }
        System.out.println();
        minimumPrice = 1000;
        for (int i = 0; i < 25; i++) {
            BaseBuildings myBuilding = new BaseBuildings("b"+i,120+i,40+i,BuildingsTypeEnum.NOBLE.toString());
            price = myBuilding.getPrice();
            p3.useBuilding(myBuilding);
            if (price < minimumPrice) {
                cheapestBuildingForp3 = myBuilding;
                minimumPrice = price;
            }
        }
        minimumPrice = 1000;
        for (int i = 0; i < 15; i++) {
            BaseBuildings myBuilding = new BaseBuildings("c"+i,90+i,10+i,BuildingsTypeEnum.MARCHAND.toString());
            price = myBuilding.getPrice();
            p5.useBuilding(myBuilding);
            if (price < minimumPrice) {
                cheapestBuildingForp5 = myBuilding;
                minimumPrice = price;
            }
        }

        assertEquals(CharacterEnum.Condottiere.toString(), bot4.getCurrentPlayer().getCharacter().getName());

        assertEquals(4, otherPlayersNotMocked.size());
        ArrayList<Object> playerAndhisBuilding = ((UncleSamBot) bot4).condottierePassivePower();
        Player victimPlayer = (Player) playerAndhisBuilding.get(0);
        assertEquals(victimPlayer, p3);
        BaseBuildings victimPlayerBuilding = (BaseBuildings) playerAndhisBuilding.get(1);
        assertEquals(cheapestBuildingForp3, victimPlayerBuilding);

        otherPlayersNotMocked.remove(p3);

        assertEquals(3, otherPlayersNotMocked.size());
        playerAndhisBuilding = ((UncleSamBot) bot4).condottierePassivePower();
        victimPlayer = (Player) playerAndhisBuilding.get(0);
        assertEquals(victimPlayer, p5);
        victimPlayerBuilding = (BaseBuildings) playerAndhisBuilding.get(1);
        assertEquals(victimPlayerBuilding, cheapestBuildingForp5);

        otherPlayersNotMocked.remove(p5);

        assertEquals(2, otherPlayersNotMocked.size());
        playerAndhisBuilding = ((UncleSamBot) bot4).condottierePassivePower();
        victimPlayer = (Player) playerAndhisBuilding.get(0);
        assertEquals(victimPlayer, p2);
        victimPlayerBuilding = (BaseBuildings) playerAndhisBuilding.get(1);
        assertEquals(victimPlayerBuilding, cheapestBuildingForp2);

    }

    @Test void chooseBuildingToPickHigherNonConstructible(){
        activePlayer.setGold(0);
        ArrayList<BaseBuildings> buildingsChoice = activePlayer.drawNBuildings(cardsOfBuildings,2);
        BaseBuildings buildingChosen = unclesambot.chooseBuildings(buildingsChoice);
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
        BaseBuildings buildingChosen = unclesambot.chooseBuildings(buildingsChoice);
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
        BaseBuildings buildingChosen = unclesambot.chooseBuildings(buildingsChoice);
        boolean isMax = true;
        for (BaseBuildings building : buildingsChoice){
            if (building.getPrice() > buildingChosen.getPrice()){
                isMax = false;break;
            }
        }
        assertTrue(isMax);
    }

    @Test void architecteTurnWithoutManufactureTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 3));
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        activePlayer.setCharacter(charArchit);
        listOfTurns = activePlayer.getBot().architecteTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
    }
    @Test void architecteTurnWithManufactureTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 1));
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        activePlayer.setCharacter(charArchit);
        listOfTurns = activePlayer.getBot().architecteTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(3));
    }

    @Test void architecteTurnWithNoCardsInHand(){
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        activePlayer.setCharacter(charArchit);
        listOfTurns = activePlayer.getBot().architecteTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
    }

    @Test void assassinTurnWithoutManufactureTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 3));
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        activePlayer.setCharacter(charAss);
        listOfTurns = activePlayer.getBot().assassinTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test void assassinTurnWittManufactureTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 1));
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        activePlayer.setCharacter(charArchit);
        listOfTurns = activePlayer.getBot().assassinTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));
    }
    @Test void assassinTurnWitNoCardsInHand(){
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        activePlayer.setCharacter(charArchit);
        listOfTurns = activePlayer.getBot().assassinTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(2));
    }

    @Test void condottiereTurnWithoutManufactureWithoutMilitaryWithoutBuildingInHandAndNoMoneyTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        for(int i =0; i<1;i++)p3.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<5;i++)p5.useBuilding(cardsOfBuildings.remove(0));
        ArrayList<String> listOfTurns;
        p2.setGold(0);
        p2.useBuilding(new BaseBuildings("a",11,5,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.useBuilding(new BaseBuildings("toto",18,40,BuildingsTypeEnum.NOBLE.toString()));
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(2,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
    }

    @Test void condottiereTurnWithoutManufactureWithoutMilitaryAndWithoutBuildingInHandTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        for(int i =0; i<1;i++)p3.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<5;i++)p5.useBuilding(cardsOfBuildings.remove(0));
        ArrayList<String> listOfTurns;
        p2.setGold(500);
        p2.useBuilding(new BaseBuildings("a",11,5,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.useBuilding(new BaseBuildings("toto",18,40,BuildingsTypeEnum.NOBLE.toString()));
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(4,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(3));
    }
    @Test void condottiereTurnWithoutManufactureAndWithoutMilitaryTest(){

        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        for(int i =0; i<1;i++)p3.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<5;i++)p5.useBuilding(cardsOfBuildings.remove(0));
        ArrayList<String> listOfTurns;
        p2.setGold(500);
        p2.useBuilding(new BaseBuildings("a",11,5,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.useBuilding(new BaseBuildings("toto",18,40,BuildingsTypeEnum.NOBLE.toString()));
        p2.setBuildingsOfThisPlayer(p2.drawNBuildings(cardsOfBuildings, 3));
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(4,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(3));

    }

    @Test void condottiereTurnWithoutManufactureAndWithMilitaryTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        p2.useBuilding(new BaseBuildings("b",10,5,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.useBuilding(new BaseBuildings("titi",18,40,BuildingsTypeEnum.MILITAIRE.toString()));
        for(int i =0; i<1;i++)p3.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<5;i++)p5.useBuilding(cardsOfBuildings.remove(0));
        ArrayList<String> listOfTurns;
        p2.setGold(500);
        p2.useBuilding(new BaseBuildings("a",11,5,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.useBuilding(new BaseBuildings("toto",18,40,BuildingsTypeEnum.NOBLE.toString()));
        p2.setBuildingsOfThisPlayer(p2.drawNBuildings(cardsOfBuildings, 3));
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(4,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(3));
    }

    @Test void condottiereTurnWithoutManufactureWithMilitaryAndNoMoneyButOnPlayerAlmostFinishTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        p2.useBuilding(new BaseBuildings("b",10,5,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.useBuilding(new BaseBuildings("titi",18,40,BuildingsTypeEnum.MILITAIRE.toString()));
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("k",81,19,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m&ms",11,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("n",19,20,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("o",131,30,BuildingsTypeEnum.MILITAIRE.toString()));
        for(int i =0; i<1;i++)p3.useBuilding(buildingsInTheHand.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(buildingsInTheHand.remove(0));
        for(int i =0; i<6;i++)p5.useBuilding(buildingsInTheHand.remove(0));//One player almost finish
        ArrayList<String> listOfTurns;
        p2.setGold(0);
        p2.setBuildingsOfThisPlayer(p2.drawNBuildings(cardsOfBuildings, 3));
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(3,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(2));
    }

    @Test void condottiereTurnWithManufactureAndWithoutMilitaryTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        for(int i =0; i<1;i++)p3.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<5;i++)p5.useBuilding(cardsOfBuildings.remove(0));
        ArrayList<String> listOfTurns;
        p2.setGold(500);
        p2.useBuilding(new BaseBuildings("a",11,5,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.useBuilding(new BaseBuildings("toto",18,40,BuildingsTypeEnum.NOBLE.toString()));
        ArrayList<BaseBuildings> buildingsNoMilitary = new ArrayList<>();
        buildingsNoMilitary.add(new BaseBuildings("nonMilitaire",15,51,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.setBuildingsOfThisPlayer(buildingsNoMilitary);
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(5,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(3));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(4));

    }

    @Test void condottiereTurnWithManufactureAndWithMilitaryTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charMag);
        p4.setCharacter(charMarch);
        p5.setCharacter(charAss);
        for(int i =0; i<1;i++)p3.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<0;i++)p4.useBuilding(cardsOfBuildings.remove(0));
        for(int i =0; i<5;i++)p5.useBuilding(cardsOfBuildings.remove(0));
        ArrayList<String> listOfTurns;
        p2.setGold(500);
        p2.useBuilding(new BaseBuildings("a",11,5,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.useBuilding(new BaseBuildings("toto",18,40,BuildingsTypeEnum.MILITAIRE.toString()));
        ArrayList<BaseBuildings> buildingsNoMilitary = new ArrayList<>();
        buildingsNoMilitary.add(new BaseBuildings("nonMilitaire",15,51,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.setBuildingsOfThisPlayer(buildingsNoMilitary);
        listOfTurns =  bot2.condottiereTurn();
        assertEquals(5,listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(4));
    }


    @Test void evequeWithManufactureAndWithReligiousTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 1));
        activePlayer.setGold(500);
        when(cityMock.getNbrReligiousBuildings()).thenReturn(5);
        ArrayList<String> listOfTurns;
        listOfTurns = activePlayer.getBot().evequeTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));
    }

    @Test void evequeTurnWithManufactureAndNoReligiousTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 1));
        activePlayer.setGold(500);
        ArrayList<String> listOfTurns;
        listOfTurns = activePlayer.getBot().evequeTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(3));
    }

    @Test void evequeTurnWithoutManufactureAndNoReligiousTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 4));
        ArrayList<String> listOfTurns;
        listOfTurns = activePlayer.getBot().evequeTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
    }

    @Test void evequeTurnWithoutManufactureAndWithReligiousTest(){
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 2));
        when(cityMock.getNbrReligiousBuildings()).thenReturn(5);
        ArrayList<String> listOfTurns;
        listOfTurns = activePlayer.getBot().evequeTurn();
        when(cityMock.getNbrReligiousBuildings()).thenReturn(2);
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test void magicienTurnPassivePowerWithManufacture(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(10);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 1));
        when(cityMock.getNbrMilitaryBuildings()).thenReturn(2);
        listOfTurns = activePlayer.getBot().magicienTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));

    }
    @Test void magicienTurnPassivePowerWithoutManufacture(){
        ArrayList<BaseBuildings> buildingsNoMilitary = new ArrayList<>();
        ArrayList<String> listOfTurns;
        p2.setGold(500);
        buildingsNoMilitary.add(new BaseBuildings("a",11,5,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("b",54,6,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("c",4,5,BuildingsTypeEnum.NOBLE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("totu",260,100,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.setBuildingsOfThisPlayer(buildingsNoMilitary);
        p2.useBuilding(p2.getBuildingsOfThisPlayer().remove(0));
        p2.useBuilding(p2.getBuildingsOfThisPlayer().remove(0));
        listOfTurns = bot2.magicienTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.PASSIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }

    @Test void magicienTurnActivePowerWithManufacture(){
        ArrayList<BaseBuildings> buildingsNoMilitary = new ArrayList<>();
        buildingsNoMilitary.add(new BaseBuildings("a",11,5,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("b",54,6,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("c",4,5,BuildingsTypeEnum.MILITAIRE.toString()));
        ArrayList<String> listOfTurns;
        p2.setBuildingsOfThisPlayer(buildingsNoMilitary);
        p2.setGold(100);
        p2.useBuilding(p2.getBuildingsOfThisPlayer().remove(0));
        p2.useBuilding(p2.getBuildingsOfThisPlayer().remove(0));

        listOfTurns = bot2.magicienTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));

    }
    @Test void magicienTurnActivePowerWithoutManufacture(){
        ArrayList<BaseBuildings> buildingsNoMilitary = new ArrayList<>();
        ArrayList<String> listOfTurns;
        buildingsNoMilitary.add(new BaseBuildings("a",11,5,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("b",54,6,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("c",4,5,BuildingsTypeEnum.NOBLE.toString()));
        buildingsNoMilitary.add(new BaseBuildings("d",34,6,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsNoMilitary.add(new BaseBuildings("e",14,5,BuildingsTypeEnum.NOBLE.toString()));
        p2.setBuildingsOfThisPlayer(buildingsNoMilitary);
        p2.setGold(1000);
        for(int i = 0; i<2; i++)p2.useBuilding(p2.getBuildingsOfThisPlayer().get(i));
        listOfTurns = p2.getBot().magicienTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));

    }

    @Test void marchandTurnWithMarchandBuildingWithoutManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,4));// il a des cartes dans sa main et il a de l'argent donc il va Build
        when(cityMock.getNbrCommercialBuildings()).thenReturn(1);// il a des bâtiments commerciaux
        listOfTurns = activePlayer.getBot().marchandTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));

    }
    @Test void marchandTurnWithMarchandBuildingWithManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,1));// il a 1 carte dans sa main et il a de l'argent donc il va Build et il va aussi utiliser le pouvoir du manufacture
        when(cityMock.getNbrCommercialBuildings()).thenReturn(1);// il a des bâtiments commerciaux
        listOfTurns = activePlayer.getBot().marchandTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));
    }

    @Test void marchandTurnWithoutMarchandBuildingWithoutManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,4));// il a des cartes dans sa main et il a de l'argent donc il va Build
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);// il n'a pas bâtiments commerciaux
        listOfTurns = activePlayer.getBot().marchandTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
    }
    @Test void marchandTurnWithoutMarchandBuildingWithManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,1));// il a 1 carte dans sa main et il a de l'argent donc il va Build et il va aussi utiliser le pouvoir du manufacture
        when(cityMock.getNbrCommercialBuildings()).thenReturn(0);// il n'a pas des bâtiments commerciaux
        listOfTurns = activePlayer.getBot().marchandTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(3));
    }

    @Test void roiTurnWithNobleBuildingWithoutManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,4));// il a des cartes dans sa main et il a de l'argent donc il va Build
        when(cityMock.getNbrNobleBuildings()).thenReturn(1);// il n'a pas bâtiments nobles
        listOfTurns = activePlayer.getBot().roiTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
    }
    @Test void roiTurnWithoutNobleBuildingWithoutManufactureNoMoneyTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(0);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,4));// il a des cartes dans sa main et il a de l'argent donc il va Build
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);// il n'a pas bâtiments nobles
        listOfTurns = activePlayer.getBot().roiTurn();
        assertEquals(2, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(1));
    }

    @Test void roiTurnWithNobleBuildingWithManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,1));// il a des cartes dans sa main et il a de l'argent donc il va Build
        when(cityMock.getNbrNobleBuildings()).thenReturn(1);// il a des bâtiments Nobles
        listOfTurns = activePlayer.getBot().roiTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(3));
    }

    @Test void roiTurnTurnWithoutNobleBuildingWithoutManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,4));// il a des cartes dans sa main et il a de l'argent donc il va Build
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);// il n'a pas des bâtiments Nobles
        listOfTurns = activePlayer.getBot().roiTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(1));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(2));
    }
    @Test void roiTurnTurnWithoutNobleBuildingWithManufactureTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(500);
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings,1));// il a 1 carte dans sa main et il a de l'argent donc il va Build et il va aussi utiliser le pouvoir du manufacture
        when(cityMock.getNbrNobleBuildings()).thenReturn(0);// il n'a pas des bâtiments Nobles
        listOfTurns = activePlayer.getBot().roiTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.LABORATOIREABILITY,listOfTurns.get(0));
        assertEquals(CharacterEnum.MANUFACTUREABILITY,listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD,listOfTurns.get(2));
        assertEquals(CharacterEnum.ACTIVEPOWER,listOfTurns.get(3));
    }
    @Test void voleurTurnWithoutManufactureNoMoneyTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(0);// il ne pourra pas choisr de build
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 4));
        listOfTurns = activePlayer.getBot().voleurTurn();
        assertEquals(2, listOfTurns.size());
        assertEquals(CharacterEnum.ACTIVEPOWER, listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY, listOfTurns.get(1));
    }

    @Test void voleurTurnWithManufactureWithmoneyTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(50);// il  pourra choisr de build
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 1));
        listOfTurns = activePlayer.getBot().voleurTurn();
        assertEquals(4, listOfTurns.size());
        assertEquals(CharacterEnum.ACTIVEPOWER, listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY, listOfTurns.get(1));
        assertEquals(CharacterEnum.MANUFACTUREABILITY, listOfTurns.get(2));
        assertEquals(CharacterEnum.BUILD, listOfTurns.get(3));
    }

    @Test void voleurTurnWithoutManufactureWithMoneyTest(){
        ArrayList<String> listOfTurns;
        activePlayer.setGold(50);// il  pourra  choisr de build
        activePlayer.setBuildingsOfThisPlayer(activePlayer.drawNBuildings(cardsOfBuildings, 4));
        listOfTurns = activePlayer.getBot().voleurTurn();
        assertEquals(3, listOfTurns.size());
        assertEquals(CharacterEnum.ACTIVEPOWER, listOfTurns.get(0));
        assertEquals(CharacterEnum.LABORATOIREABILITY, listOfTurns.get(1));
        assertEquals(CharacterEnum.BUILD, listOfTurns.get(2));
    }

    @Test void doActivePowerAssassinTest(){
        activePlayer.setCharacter(charAss);
        ArrayList<Object> killedCharacter;
        killedCharacter = activePlayer.getBot().doActivePower();
        assertEquals(1, killedCharacter.size());
        assertEquals(CharacterEnum.CONDOTTIEREPRIORITY, killedCharacter.get(0));
    }

    @Test void doActivePowerVoleurTest(){
        activePlayer.setCharacter(charVol);
        ArrayList<Object> killedCharacter;
        killedCharacter = activePlayer.getBot().doActivePower();
        assertEquals(1, killedCharacter.size());
        assertEquals(CharacterEnum.MAGICIENPRIORITY, killedCharacter.get(0));
    }

    @Test void doActivePowerMagicienTest(){
        p2.setCharacter(charMag);
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.NOBLE.toString()));
        p2.setBuildingsOfThisPlayer(buildingsInTheHand);
        for(int i = 0; i<3;i++)p2.useBuilding(p2.getBuildingsOfThisPlayer().get(0));
        ArrayList<Object> unWantedCards;
        unWantedCards = (ArrayList<Object>) bot2.doActivePower().get(0);// 1 taableau qui contient les bâtiments qui ne veux pas
        assertEquals(2, unWantedCards.size());
        assertEquals(p2.getBuildingsOfThisPlayer().get(0), (unWantedCards.get(0)));
        assertEquals(p2.getBuildingsOfThisPlayer().get(2), unWantedCards.get(1));

    }

    @Test void doActivePowerArchitechteTest(){
        p2.setCharacter(charArchit);
        p2.setGold(500);
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("d",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("i",19,97,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.setBuildingsOfThisPlayer(buildingsInTheHand);
        for(int i = 0; i<4;i++)p2.useBuilding(p2.getBuildingsOfThisPlayer().get(0));
        ArrayList<Object> wantedCards;
        wantedCards = bot2.doActivePower();
        assertNotNull(wantedCards);
        assertEquals(2, wantedCards.size());// il y a plus des bâtiment militaires donc il va préférer de construire des bâtiments militaires en priorité
        assertEquals(p2.getBuildingsOfThisPlayer().get(0).getName(), ((BaseBuildings)wantedCards.get(0)).getName());
        assertEquals(p2.getBuildingsOfThisPlayer().get(2), wantedCards.get(1));
    }

    @Test void doPassivePowerForMagicianTest(){
        p2.setCharacter(charMag);
        p3.setCharacter(charArchit);
        p4.setCharacter(charMag);
        p5.setCharacter(charVol);

        p2.setBuildingsOfThisPlayer(p2.drawNBuildings(cardsOfBuildings,15));
        p3.setBuildingsOfThisPlayer(p3.drawNBuildings(cardsOfBuildings, 12));// des joueurs qui ont des bâtiments dans leurs mains
        p4.setBuildingsOfThisPlayer(p4.drawNBuildings(cardsOfBuildings,4));
        p5.setBuildingsOfThisPlayer(p5.drawNBuildings(cardsOfBuildings,10));
        // on va essayer de voir qui a le plus
        // à l'occurence ça sera p3, p5, p4 et puis p2 lui-même car il n'y aura que lui dans le champs car le joueur ne vas pas echanger sa main avec lui même s'il y a d'autres joueurs n'est-ce pas?

        assertEquals(4, otherPlayersNotMocked.size());
        ArrayList<Object> luckyPlayer = bot2.doPassivePower();
        Player myLuckyPlayer = (Player) luckyPlayer.get(0);
        assertEquals(p3, myLuckyPlayer);
        otherPlayersNotMocked.remove(p3);

        assertEquals(3, otherPlayersNotMocked.size());
        luckyPlayer = bot2.doPassivePower();
        myLuckyPlayer = (Player) luckyPlayer.get(0);
        assertEquals(p5, myLuckyPlayer);
        otherPlayersNotMocked.remove(p5);

        assertEquals(2, otherPlayersNotMocked.size());
        luckyPlayer = bot2.doPassivePower();
        myLuckyPlayer = (Player) luckyPlayer.get(0);
        assertNull(myLuckyPlayer);// s'il y a que  2 joueurs il ne va pas choisir lui-même même s'il a le plus des cartes
        otherPlayersNotMocked.remove(p4);

        assertEquals(1, otherPlayersNotMocked.size());
        luckyPlayer = bot2.doPassivePower();
        myLuckyPlayer = (Player) luckyPlayer.get(0);
        assertEquals(p2, myLuckyPlayer);

    }

    @Test void doPassivePowerForCondottiereWithoutEvequeTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charRoi);
        p4.setCharacter(charMag);
        p5.setCharacter(charVol);

        p2.setGold(500);
        p3.setGold(500);
        p4.setGold(500);
        p5.setGold(500);

        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d1",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("d2",12,12,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("e2",30,31,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("h2",16,16,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("k",81,19,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m&ms",11,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("n",19,20,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        p3.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        p4.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        p5.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));

        assertEquals(buildingsInTheHand.size(), p2.getNbOfbuildingsInHisHand());
        assertEquals(buildingsInTheHand.size(), p3.getNbOfbuildingsInHisHand());
        assertEquals(buildingsInTheHand.size(), p4.getNbOfbuildingsInHisHand());
        assertEquals(buildingsInTheHand.size(), p5.getNbOfbuildingsInHisHand());

        for(int i = 0; i<4; i++)p2.useBuilding(p2.getBuildingsOfThisPlayer().get(0));
        for(int i = 0; i<10; i++)p3.useBuilding(p3.getBuildingsOfThisPlayer().get(0));
        for(int i = 0; i<2; i++)p4.useBuilding(p4.getBuildingsOfThisPlayer().get(0));
        for(int i = 0; i<5; i++)p5.useBuilding(p5.getBuildingsOfThisPlayer().get(0));

        assertEquals(4,p2.getNbOfbuildingsInCity());
        assertEquals(10,p3.getNbOfbuildingsInCity());
        assertEquals(2,p4.getNbOfbuildingsInCity());
        assertEquals(5,p5.getNbOfbuildingsInCity());

        ArrayList<Object> listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p3, listOfPlayerAndHisBuilding.get(0));
        assertEquals(buildingsInTheHand.get(9),listOfPlayerAndHisBuilding.get(1));// car le 9ème bâtiment est le moins chère car il coût 1 gold
        otherPlayersNotMocked.remove(p3);
        assertEquals(3, otherPlayersNotMocked.size());

        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p5, listOfPlayerAndHisBuilding.get(0));
        assertEquals(buildingsInTheHand.get(1),listOfPlayerAndHisBuilding.get(1));
        otherPlayersNotMocked.remove(p5);
        assertEquals(2, otherPlayersNotMocked.size());

        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p4, listOfPlayerAndHisBuilding.get(0));
        assertEquals(buildingsInTheHand.get(1),listOfPlayerAndHisBuilding.get(1));// car le 2ème bâtiment est le moins chère car il coût 11 gold
        otherPlayersNotMocked.remove(p4);
        assertEquals(1, otherPlayersNotMocked.size());

        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p2, listOfPlayerAndHisBuilding.get(0)); // oui il peut choisr lui-même car de toute façon il n'y a que lui dans le champ
        assertEquals(buildingsInTheHand.get(1),listOfPlayerAndHisBuilding.get(1));
        otherPlayersNotMocked.remove(p2);
        assertEquals(0, otherPlayersNotMocked.size());

        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertNull(listOfPlayerAndHisBuilding);
    }

    @Test void doPassivePowerForCondottiereWithEvequeTest(){
        p2.setCharacter(charCon);
        p3.setCharacter(charRoi);
        p4.setCharacter(charMag);
        p5.setCharacter(charEveq);

        p2.setGold(500);
        p3.setGold(500);
        p4.setGold(500);
        p5.setGold(500);

        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d1",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("h",16,16,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("d2",12,12,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("e2",30,31,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("h2",16,16,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("a",1,1,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("j",18,18,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("k",81,19,BuildingsTypeEnum.RELIGIEUX.toString()));
        buildingsInTheHand.add(new BaseBuildings("m",19,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("m&ms",11,101,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("n",19,20,BuildingsTypeEnum.MILITAIRE.toString()));
        p2.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        p3.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        p4.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        p5.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));

        assertEquals(buildingsInTheHand.size(), p2.getNbOfbuildingsInHisHand());
        assertEquals(buildingsInTheHand.size(), p3.getNbOfbuildingsInHisHand());
        assertEquals(buildingsInTheHand.size(), p4.getNbOfbuildingsInHisHand());
        assertEquals(buildingsInTheHand.size(), p5.getNbOfbuildingsInHisHand());

        for(int i = 0; i<4; i++)p2.useBuilding(p2.getBuildingsOfThisPlayer().get(0));
        for(int i = 0; i<10; i++)p3.useBuilding(p3.getBuildingsOfThisPlayer().get(0));
        for(int i = 0; i<2; i++)p4.useBuilding(p4.getBuildingsOfThisPlayer().get(0));
        for(int i = 0; i<5; i++)p5.useBuilding(p5.getBuildingsOfThisPlayer().get(0));

        assertEquals(4,p2.getNbOfbuildingsInCity());
        assertEquals(10,p3.getNbOfbuildingsInCity());
        assertEquals(2,p4.getNbOfbuildingsInCity());
        assertEquals(5,p5.getNbOfbuildingsInCity());

        ArrayList<Object> listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p3, listOfPlayerAndHisBuilding.get(0));
        assertEquals(buildingsInTheHand.get(9),listOfPlayerAndHisBuilding.get(1));// car le 9ème bâtiment est le moins chère car il coût 1 gold
        otherPlayersNotMocked.remove(p3);
        assertEquals(3, otherPlayersNotMocked.size());


        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p4, listOfPlayerAndHisBuilding.get(0));
        assertEquals(buildingsInTheHand.get(1),listOfPlayerAndHisBuilding.get(1));// car le 2ème bâtiment est le moins chère car il coût 11 gold
        otherPlayersNotMocked.remove(p4);
        assertEquals(2, otherPlayersNotMocked.size());

        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertEquals(p2, listOfPlayerAndHisBuilding.get(0));
        assertEquals(buildingsInTheHand.get(1),listOfPlayerAndHisBuilding.get(1));
        otherPlayersNotMocked.remove(p2);
        assertEquals(1, otherPlayersNotMocked.size());

        listOfPlayerAndHisBuilding = bot2.doPassivePower();
        assertNull(listOfPlayerAndHisBuilding);


    }

    @Test void laboratoryDecisionTest(){
        ArrayList<BaseBuildings> buildingsInTheHand = new ArrayList<>();
        p2.setGold(41);// ou 40 ça marche aussi l'ideal c'est qu'il lui restra moins de 2 pièce après la construction
        p2.getCity().getBuildings().clear();
        buildingsInTheHand.add(new BaseBuildings("i",17,17,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("c",11,11,BuildingsTypeEnum.MARCHAND.toString()));
        buildingsInTheHand.add(new BaseBuildings("d1",12,12,BuildingsTypeEnum.MILITAIRE.toString()));
        buildingsInTheHand.add(new BaseBuildings("e",31,13,BuildingsTypeEnum.NOBLE.toString()));
        buildingsInTheHand.add(new BaseBuildings("f",14,14,BuildingsTypeEnum.RELIGIEUX.toString()));
        p2.setBuildingsOfThisPlayer(new ArrayList<>(buildingsInTheHand));
        for (int i = 0; i<3;i++)p2.useBuilding(p2.getBuildingsOfThisPlayer().get(0));
        BaseBuildings theUnWantedBuilding = bot2.laboratoryDecision();
        assertEquals(buildingsInTheHand.get(3),theUnWantedBuilding);// car c'est le plus chère parmis les deux cartes qui sont restés dans sa main
    }
}