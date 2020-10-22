package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.BuildingsPackage.WonderBuilding;
import fr.unice.polytech.startingpoint.City;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CondottiereTest {
    private CharacterEnum character = CharacterEnum.Condottiere;
    private CharacterEnum autre = CharacterEnum.Assassin;
    private BaseCharacter condottiereCharacter,assassincharacter;
    private ArrayList<Player> players = new ArrayList<>();
    private Player condottierePlayer;
    private City citeMock;
    private BaseBuildings buildingMock;
    private Player joueur1,joueur2,joueur3;



    @BeforeEach
    void setUp(){
        assassincharacter = autre.createCharacter();
        condottiereCharacter = character.createCharacter();
        BaseBot bot = BotsType.BasicBot.createBot(1);
        assert bot != null;
        joueur1 = new Player(bot,1);joueur2 = new Player(bot,2);joueur3 = new Player(bot,2);condottierePlayer = new Player(bot,1);
        players.add(joueur1);players.add(joueur2);players.add(joueur3);players.add(condottierePlayer);
        joueur1.setCharacter(CharacterEnum.Assassin.createCharacter());// le joueur1 a l'assassin
        joueur2.setCharacter(CharacterEnum.Magicien.createCharacter());// le joueur2 a le condottiere
        joueur3.setCharacter(CharacterEnum.Voleur.createCharacter());// le joueur3 a le voleur
        condottierePlayer.setCharacter(condottiereCharacter);
        joueur1.setCharacter(assassincharacter);

        condottierePlayer.setCity(citeMock);
        citeMock = mock(City.class);
        condottierePlayer.setCity(citeMock);
        condottierePlayer.setGold(0);

        buildingMock = mock(BaseBuildings.class);
    }

    @Test
    void testGoodPriority(){
        assertEquals(CharacterEnum.CONDOTTIEREPRIORITY,condottiereCharacter.getPriority());
    }

    @Test
    void testCondottiereConstructor(){
        BaseCharacter condottiere2 = character.createCharacter();
        assert condottiere2 != null;
        assertEquals(CharacterEnum.CONDOTTIEREPRIORITY,condottiere2.getPriority());
    }

    @Test void activePowerTestCasNormal() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(3);
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(9);//il a 9 batiments militaires
        when(citeMock.getNbrNobleBuildings()).thenReturn(2);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(7);
        condottiereCharacter.activePower(null, null, null, condottierePlayer);
        assertEquals(9, condottierePlayer.getGold());
    }
    @Test void activePowerTestAvecEcoleDeMagie() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(2);//il a 2 batiments militaires et l'école de magie
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(2);
        when(citeMock.getNbrNobleBuildings()).thenReturn(0);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(1);
        when(citeMock.getEcoleDeMagie()).thenReturn(new WonderBuilding("", 1, 1, ""));
        condottiereCharacter.activePower(null, null, null, condottierePlayer);
        assertEquals(3, condottierePlayer.getGold());
    }
    @Test void activePowerTestSansBatiment() {
        when(citeMock.getNbrCommercialBuildings()).thenReturn(0);//il a rien
        when(citeMock.getNbrMilitaryBuildings()).thenReturn(0);
        when(citeMock.getNbrNobleBuildings()).thenReturn(0);
        when(citeMock.getNbrReligiousBuildings()).thenReturn(0);
        when(citeMock.getEcoleDeMagie()).thenReturn(null);
        condottiereCharacter.activePower(null, null, null, condottierePlayer);
        assertEquals(0, condottierePlayer.getGold());
    }
    @Test
    void passivePowerTest(){
        ArrayList<BaseBuildings> buildingsOfJoueurMock = new ArrayList<>();
        buildingsOfJoueurMock.add(buildingMock);
        joueur1.setBuildingsOfThisPlayer(buildingsOfJoueurMock);
        when(buildingMock.getType()).thenReturn(BuildingsTypeEnum.MERVEILLE.toString());
        when(buildingMock.getName()).thenReturn("");
        joueur1.useBuilding(buildingMock);//un joueur 1 a posé un building
        joueur1.setGold(0);

        when(buildingMock.getPrice()).thenReturn(4);//il vaut 4
        condottierePlayer.setGold(10);//le condottiere a 10 golds

        ArrayList<Object> destroy = new ArrayList<>();
        destroy.add(joueur1);destroy.add(buildingMock);//destruction de ce batiment dans cette cité
        assertEquals(1,joueur1.getBuildingsBuilt().size());
        condottiereCharacter.passivePower(destroy,null,null,condottierePlayer);
        assertEquals(0,joueur1.getBuildingsBuilt().size());//le batiment est bien détruit
        assertEquals(10-4+1,condottierePlayer.getGold());//il a bien -(coutdubatiment-1) en moins

    }
}
