package fr.unice.polytech.startingpoint.BotsPackage;
import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicBotTest {
    private BotsType bot = BotsType.BasicBot;
    private BaseBot basicBot;
    private Player joueurMock;

    @BeforeEach
    void setUp(){
        basicBot = bot.createBot(0);
        joueurMock = mock(Player.class);
        basicBot.updateCurrentPlayer(joueurMock);
    }

    @Test
    void chooseBuildingsToBuildTest(){// choix d'un bat à construire parmis sa main (ici le premier construisible)
        ArrayList<BaseBuildings> mainDuJoueur1= new ArrayList<>();//liste vide
        ArrayList<BaseBuildings> mainDuJoueur2 = new ArrayList<>();
        ArrayList<BaseBuildings> mainDuJoueur3 = new ArrayList<>();
        for (int i = 1; i< 20; i ++){
            mainDuJoueur2.add(new BaseBuildings("Bat" + i,i,i,"")); //liste de batiment de coût croissant
        }
        for (int i = 1; i< 200; i ++){
            mainDuJoueur3.add(new BaseBuildings("Bat" + i,i+1,i,"")); //liste de batiment aleatoire de cout>1
        }
        Collections.shuffle(mainDuJoueur3);


        when(joueurMock.getGold()).thenReturn(20);// si le joueur a 20 po
        assertNull(basicBot.chooseBuildingsToBuild(mainDuJoueur1)); //il ne renvoit rien
        assertEquals("Bat1", basicBot.chooseBuildingsToBuild(mainDuJoueur2).getName()); //il prend bien le premier
        assertTrue(20 >= basicBot.chooseBuildingsToBuild(mainDuJoueur3).getPrice()); //Celui choisit a un prix < 20
        when(joueurMock.getGold()).thenReturn(1);// si le joueur a 1 po
        assertNull(basicBot.chooseBuildingsToBuild(mainDuJoueur1)); //il ne renvoit rien
        assertEquals("Bat1", basicBot.chooseBuildingsToBuild(mainDuJoueur2).getName()); //il prend bien le premier
        assertNull(basicBot.chooseBuildingsToBuild(mainDuJoueur3)); //il ne peux rien construire

    }
    @Test
    void chooseBuildingsTest(){//choix d'un batiment à piocher (ici prend le premier)
        ArrayList<BaseBuildings> choixPioche1 = new ArrayList<>();//liste vide
        ArrayList<BaseBuildings> choixPioche2 = new ArrayList<>();//2 bat de même cout
        ArrayList<BaseBuildings> choixPioche3 = new ArrayList<>();//1 seul batiment piochable
        choixPioche2.add(new BaseBuildings("Bat1",3,3,""));
        choixPioche2.add(new BaseBuildings("Bat2",3,3,""));
        choixPioche3.add(new BaseBuildings("Bat1",10,10,""));
        assertNull(basicBot.chooseBuildings(choixPioche1));//ne choisit rien
        assertEquals("Bat1", basicBot.chooseBuildings(choixPioche2).getName());//choisit bien le premier
        assertEquals("Bat1", basicBot.chooseBuildings(choixPioche3).getName());//choisit bien le seul batiment
    }

    @Test
    void wannaBuildTest() {
        when(joueurMock.canBuildABuilding()).thenReturn(true);
        assertTrue(basicBot.wannaBuild());
        when(joueurMock.canBuildABuilding()).thenReturn(false);
        assertFalse(basicBot.wannaBuild());
    }

    @Test
    void drawOrGold(){// renvoi true s'il veut piocher, false sinon (ici il pioche tant qu'il a plus de 6 gold)
        for (int i = 0;i<5;i++){
            when(joueurMock.getGold()).thenReturn(i);// si le joueur a entre 0 et 5 po
            assertFalse(basicBot.drawOrGold()); // le bot veut des po
        }
        for (int i = 6;i<20;i++){
            when(joueurMock.getGold()).thenReturn(i);// si le joueur a plus de 6 po
            assertTrue(basicBot.drawOrGold()); // le bot veut piocher
        }
    }
    @Test
    void chooseCharacterTest(){//choix d'un personnage à prendre (ici prend le premier)
        CharacterEnum roi = CharacterEnum.Roi;
        CharacterEnum voleur = CharacterEnum.Voleur;
        ArrayList<BaseCharacter> choixChar1 = new ArrayList<>();//liste vide
        ArrayList<BaseCharacter> choixChar2 = new ArrayList<>();// 2 characters disponibles
        ArrayList<BaseCharacter> choixChar3 = new ArrayList<>();//1 seul character disponible
        choixChar2.add(voleur.createCharacter());
        choixChar2.add(roi.createCharacter());
        choixChar3.add(roi.createCharacter());
        assertNull(basicBot.chooseCharacter(choixChar1));//ne choisit rien
        assertEquals(CharacterEnum.Voleur.toString(), basicBot.chooseCharacter(choixChar2).getName());//choisit bien le premier
        assertEquals(CharacterEnum.Roi.toString(), basicBot.chooseCharacter(choixChar3).getName());//choisit bien le seul personnage

    }
    @Test void characterTurnCanBuild() {
        when(joueurMock.canBuildABuilding()).thenReturn(true);
        assertEquals(CharacterEnum.BUILD, basicBot.assassinTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.voleurTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.magicienTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.roiTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.evequeTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.marchandTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.architecteTurn().get(0));
        assertEquals(CharacterEnum.BUILD, basicBot.condottiereTurn().get(0));
    }
    @Test void characterTurnCanNotBuild(){
        when(joueurMock.canBuildABuilding()).thenReturn(false);
        assertEquals(0,basicBot.assassinTurn().size());
        assertEquals(0,basicBot.voleurTurn().size());
        assertEquals(0,basicBot.magicienTurn().size());
        assertEquals(0,basicBot.roiTurn().size());
        assertEquals(0,basicBot.evequeTurn().size());
        assertEquals(0,basicBot.marchandTurn().size());
        assertEquals(0,basicBot.architecteTurn().size());
        assertEquals(0,basicBot.condottiereTurn().size());
    }
    @Test void characterAndMerveillePowerDoNothing(){
        assertNull(basicBot.doActivePower());
        assertNull(basicBot.doPassivePower());
        assertNull(basicBot.laboratoryDecision());
    }

}
