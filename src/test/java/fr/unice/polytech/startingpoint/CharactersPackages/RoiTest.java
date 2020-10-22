package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.WonderBuilding;
import fr.unice.polytech.startingpoint.City;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author FRANCIS Anas
 * Created 03/11/2019
 * \brief Ici on teste les méthodes de la classe Assassin
 */

class RoiTest {
    private BaseCharacter roiCharacter =CharacterEnum.Roi.createCharacter();
    private BaseCharacter evequeCharacter =CharacterEnum.Eveque.createCharacter();
    private BaseCharacter assassinCharacter =CharacterEnum.Assassin.createCharacter();
    private ArrayList<Player> players = new ArrayList<>();
    private Player evequePlayer,assassinPlayer,roiPlayer;
    private City citeMock;


    @BeforeEach
    void setUp(){
        BaseBot bot = BotsType.BasicBot.createBot(1);
        assert bot != null;
        roiPlayer = new Player(bot,1); // On crée un joueur simple
        evequePlayer = new Player(bot,1);
        assassinPlayer = new Player(bot,1);
        roiPlayer = new Player(bot,1);
        players.add(roiPlayer);players.add(evequePlayer);players.add(assassinPlayer);
        roiPlayer.setCharacter(roiCharacter);
        assassinPlayer.setCharacter(assassinCharacter);
        evequePlayer.setCharacter(evequeCharacter);
        citeMock = mock(City.class);
        roiPlayer.setGold(0);
        roiPlayer.setCity(citeMock);
    }

    @Test
    void testGoodPriority(){
        assertEquals(CharacterEnum.ROIPRIORITY,roiCharacter.getPriority());
    }


    @Test
    void activePowerTestCasNormal() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(1);
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(9);
        when(citeMock.getNbrNobleBuildings()).thenReturn(3);//il a 3 batiments nobles
        when(citeMock.getNbrReligiousBuildings()).thenReturn(7);
        roiCharacter.activePower(null, null, null, roiPlayer);//il a 3 batiments nobles
        assertEquals(3, roiPlayer.getGold());
    }

    @Test void activePowerTestAvecEcoleDeMagie() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(0);//il a 2 batiments nobles et l'école de magie
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(2);
        when(citeMock.getNbrNobleBuildings()).thenReturn(2);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(1);
        when(citeMock.getEcoleDeMagie()).thenReturn(new WonderBuilding("", 1, 1, ""));
        roiCharacter.activePower(null, null, null, roiPlayer);
        assertEquals(3, roiPlayer.getGold());
    }
    @Test void activePowerTestSansBatiment() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(0);//il a rien
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(0);
        when(citeMock.getNbrNobleBuildings()).thenReturn(0);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(0);
        when(citeMock.getEcoleDeMagie()).thenReturn(null);
        roiCharacter.activePower(null, null, null, roiPlayer);
        assertEquals(0, roiPlayer.getGold());
    }

    @Test
    void passivePowerTest() {
        assassinPlayer.crown();
        assertTrue(assassinPlayer.getCrowned());
        assertFalse(roiPlayer.getCrowned());
        assertFalse(evequePlayer.getCrowned());
        roiCharacter.passivePower(null,players,null,roiPlayer);
        assertFalse(assassinPlayer.getCrowned());
        assertTrue(roiPlayer.getCrowned());
        assertFalse(evequePlayer.getCrowned());
    }

}