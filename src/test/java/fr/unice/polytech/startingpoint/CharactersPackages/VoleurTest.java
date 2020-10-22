package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VoleurTest {
    private CharacterEnum character = CharacterEnum.Voleur;
    private BaseCharacter voleurcharacter;
    private ArrayList<Object> characterToSteal = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private Player voleur;
    private Player architect;
    private Player roi;
    private Player assassin;

    @BeforeEach
    void setUp() {
        voleurcharacter = character.createCharacter();
        BaseBot bot = BotsType.BasicBot.createBot(1);
        assert bot != null;
        voleur = new Player(bot,1);
        architect = new Player(bot,2);
        Player magicien = new Player(bot, 3);
        roi = new Player(bot,4);
        assassin = new Player(bot,5);
        players.add(voleur);
        players.add(architect);
        players.add(magicien);
        players.add(assassin);
        players.add(roi); // on setup tous les joueurs
        voleur.setCharacter(voleurcharacter);
        architect.setCharacter(CharacterEnum.Architecte.createCharacter());
        magicien.setCharacter(CharacterEnum.Magicien.createCharacter());
        roi.setCharacter(CharacterEnum.Roi.createCharacter());
        assassin.setCharacter(CharacterEnum.Assassin.createCharacter());

        characterToSteal.clear();
    }

    @Test
    void testGoodPriority(){
        assertEquals(CharacterEnum.VOLEURPRIORITY,voleurcharacter.getPriority());
    }

    @Test
    void testVoleurConstructor(){
        BaseCharacter voleur2 = character.createCharacter();
        assert voleur2 != null;
        assertEquals(voleurcharacter.getPriority(),voleur2.getPriority());
    }

    @Test void voleArchitecteAvecVoleurPauvre() {
        characterToSteal.add(CharacterEnum.ARCHITECTEPRIORITY);//vole de l'architecte
        architect.setGold(3);
        voleur.setGold(0);//le voleur n'a pas de sous au départ
        voleurcharacter.activePower(characterToSteal, players, null, voleur);
        assertEquals(3,voleur.getGold());
        assertEquals(0,architect.getGold());
    }
    @Test void voleArchitecteAvecVoleurRiche() {
        characterToSteal.add(CharacterEnum.ARCHITECTEPRIORITY);
        architect.setGold(3);
        voleur.setGold(6);//le voleur a des sous au départ
        voleurcharacter.activePower(characterToSteal, players, null, voleur);
        assertEquals(9,voleur.getGold());
        assertEquals(0,architect.getGold());
    }
    @Test void voleArchitectePauvre() {
        characterToSteal.clear();
        characterToSteal.add(CharacterEnum.ARCHITECTEPRIORITY);
        architect.setGold(0);//la victime n'a pas de sous
        voleur.setGold(6);
        voleurcharacter.activePower(characterToSteal, players, null, voleur);
        assertEquals(6,voleur.getGold());
        assertEquals(0,architect.getGold());
    }
    @Test void voleAssassinImpossible() {
        characterToSteal.clear();
        characterToSteal.add(CharacterEnum.ASSASSINPRIORITY);//la victime ne peut pas être l'assassin
        assassin.setGold(3);
        voleur.setGold(6);
        voleurcharacter.activePower(characterToSteal, players, null, voleur);
        assertEquals(6,voleur.getGold());
        assertEquals(3,assassin.getGold());
    }
    @Test void voleMortImpossible() {
        characterToSteal.clear();
        characterToSteal.add(CharacterEnum.ROIPRIORITY);//la victime ne peut pas être le mort
        roi.kill();
        roi.setGold(3);
        voleur.setGold(6);
        voleurcharacter.activePower(characterToSteal, players, null, voleur);
        assertEquals(6,voleur.getGold());
        assertEquals(3,roi.getGold());
    }
    @Test void volePersonnageAbsent() {
        characterToSteal.clear();
        characterToSteal.add(CharacterEnum.CONDOTTIEREPRIORITY);//si la victime n'est pas en jeu actuellement
        voleur.setGold(6);
        voleurcharacter.activePower(characterToSteal, players, null, voleur);
        assertEquals(6,voleur.getGold());
    }

}
