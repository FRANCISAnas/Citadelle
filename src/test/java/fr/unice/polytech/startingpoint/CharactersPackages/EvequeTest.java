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

 class EvequeTest {
    private CharacterEnum character = CharacterEnum.Eveque;
    private BaseCharacter evequeCharacter;
    private Player evequePlayer;
    private City citeMock;



    @BeforeEach
    void setUp(){
        evequeCharacter = character.createCharacter();
        BaseBot bot = BotsType.BasicBot.createBot(1);
        assert bot != null;
        evequePlayer = new Player(bot,1);
        evequePlayer.setCharacter(evequeCharacter);

        citeMock = mock(City.class);
        evequePlayer.setGold(0);
        evequePlayer.setCity(citeMock);
    }

    @Test
    void testGoodPriority(){
        assertEquals(CharacterEnum.EVEQUEPRIORITY,evequeCharacter.getPriority());
    }

    @Test
    void testEvequeConstructor(){
        BaseCharacter eveque2 = character.createCharacter();
        assert eveque2 != null;
        assertEquals(CharacterEnum.EVEQUEPRIORITY,evequeCharacter.getPriority());
    }

    @Test
    void activePowerTestCasNormal() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(1);
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(9);
        when(citeMock.getNbrNobleBuildings()).thenReturn(3);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(7);//il a 7 batiments religieux
        evequeCharacter.activePower(null, null, null, evequePlayer);//il a 3 batiments nobles
        assertEquals(7, evequePlayer.getGold());
    }

     @Test void activePowerTestAvecEcoleDeMagie() {
         when(citeMock.getNbrCommercialBuildings()).thenReturn(2);//il a 1 batiments religieux et l'école de magie
         when(citeMock.getNbrMilitaryBuildings()).thenReturn(2);
         when(citeMock.getNbrNobleBuildings()).thenReturn(0);
         when(citeMock.getNbrReligiousBuildings()).thenReturn(1);
         when(citeMock.getEcoleDeMagie()).thenReturn(new WonderBuilding("", 1, 1, ""));
         evequeCharacter.activePower(null, null, null, evequePlayer);
         assertEquals(2, evequePlayer.getGold());
     }
     @Test void activePowerTestSansBatiment() {
         when(citeMock.getNbrCommercialBuildings()).thenReturn(0);//il a rien
         when(citeMock.getNbrMilitaryBuildings()).thenReturn(0);
         when(citeMock.getNbrNobleBuildings()).thenReturn(0);
         when(citeMock.getNbrReligiousBuildings()).thenReturn(0);
         when(citeMock.getEcoleDeMagie()).thenReturn(null);
         evequeCharacter.activePower(null, null, null, evequePlayer);
         assertEquals(0, evequePlayer.getGold());
     }
}
