package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.WonderBuilding;
import fr.unice.polytech.startingpoint.City;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarchandTest {
    private CharacterEnum character = CharacterEnum.Marchand;
    private BaseCharacter marchandCharacter;
    private Player marchandPlayer;
    private City citeMock;



    @BeforeEach
    void setUp(){
        marchandCharacter = character.createCharacter();
        BaseBot bot = BotsType.BasicBot.createBot(1);
        assert bot != null;
        marchandPlayer = new Player(bot,1);
        marchandPlayer.setCharacter(marchandCharacter);
        marchandPlayer.setGold(0);

        citeMock = mock(City.class);
        marchandPlayer.setCity(citeMock);
    }

    @Test
    void testGoodPriority(){
        assertEquals(CharacterEnum.MARCHANDPRIORITY,marchandCharacter.getPriority());
    }

    @Test
    void testMarchandConstructor(){
        BaseCharacter marchand2 = character.createCharacter();
        assert marchand2 != null;
        assertEquals(marchand2.getPriority(),marchandCharacter.getPriority());
    }

    @Test void activePowerTestCasNormal() {
        //il a 3 batiments marchands
        when(citeMock.getNbrCommercialBuildings()).thenReturn(3);
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(9);
        when(citeMock.getNbrNobleBuildings()).thenReturn(2);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(7);
        marchandCharacter.activePower(null, null, null, marchandPlayer);
        assertEquals(3, marchandPlayer.getGold());
    }
    @Test void activePowerTestAvecEcoleDeMagie() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(2);//il a 2 batiments marchands et l'école de magie
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(2);
        when(citeMock.getNbrNobleBuildings()).thenReturn(0);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(1);
        when(citeMock.getEcoleDeMagie()).thenReturn(new WonderBuilding("", 1, 1, ""));
        marchandCharacter.activePower(null, null, null, marchandPlayer);
        assertEquals(3, marchandPlayer.getGold());
    }
    @Test void activePowerTestSansBatiment() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(0);//il a rien
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(0);
        when(citeMock.getNbrNobleBuildings()).thenReturn(0);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(0);
        when(citeMock.getEcoleDeMagie()).thenReturn(null);
        marchandCharacter.activePower(null, null, null, marchandPlayer);
        assertEquals(0, marchandPlayer.getGold());
    }

    @Test void passivePowerTest(){
        marchandPlayer.setGold(1);
        marchandCharacter.passivePower(null,null,null,marchandPlayer);
        assertEquals(2,marchandPlayer.getGold());

    }

}
