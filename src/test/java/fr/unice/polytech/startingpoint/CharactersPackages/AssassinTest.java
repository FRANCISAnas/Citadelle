package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import fr.unice.polytech.startingpoint.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author FRANCIS Anas
 * Created 03/11/2019
 * \brief Ici on teste les méthodes de la classe Assassin
  */

class AssassinTest {
    private CharacterEnum character = CharacterEnum.Assassin;
    private BaseCharacter assassincharacter;
    private ArrayList<Player> players = new ArrayList<>();
    private Player joueur1,joueur2,joueur3;
    private ArrayList<Object> characterToKill = new ArrayList<>();


    @BeforeEach
    void setUp(){
        assassincharacter = character.createCharacter();
        BaseBot bot = BotsType.BasicBot.createBot(1);
        BaseBot bot2 = BotsType.BasicBot.createBot(2);
        BaseBot bot3 = BotsType.BasicBot.createBot(3);
        assert bot != null;assert bot2 != null;assert bot3 != null;
        joueur1 = new Player(bot,1);
        joueur2 = new Player(bot2,2);
        joueur3 = new Player(bot3,2);
        players.add(joueur1);players.add(joueur2);players.add(joueur3);
        joueur1.setCharacter(assassincharacter);// le joueur1 a l'assassin
        joueur2.setCharacter(CharacterEnum.Condottiere.createCharacter());// le joueur2 a le condottiere
        joueur3.setCharacter(CharacterEnum.Voleur.createCharacter());// le joueur3 a le voleur
        characterToKill.clear();
        for (Player player : players){
            player.dekill();
        }
    }
    /**
     * @author FRANCIS Anas
     * Created 03/11/2019
     * \brief on teste si le personnage tué est bien celui qui est retourné par la méthode numberOfKilledCharacter
     */
    @Test void assassinatCondottiereNormal() {
        //veut tuer le condottiere
        characterToKill.add(CharacterEnum.CONDOTTIEREPRIORITY);
        assassincharacter.activePower(characterToKill, players, null, joueur1);
        assertTrue(joueur2.getKilled());//le joueur qui a le condottiere est mort
        assertFalse(joueur3.getKilled());//les autres sont vivants
        assertFalse(joueur1.getKilled());
    }
    @Test void suicideImpossible(){
        characterToKill.add(CharacterEnum.ASSASSINPRIORITY);//s'il veut se tuer lui-même
        assassincharacter.activePower(characterToKill, players, null, joueur1);
        assertFalse(joueur1.getKilled());// cela n'arrive pas
        }
    @Test void usurpationDelAssassin() {
        characterToKill.add(CharacterEnum.CONDOTTIEREPRIORITY);
        assassincharacter.activePower(characterToKill, players, null, joueur3);//si ce n'est pas l'assassin
        for (Player player : players) {
            assertFalse(player.getKilled());// il ne se passe rien
        }
    }
    @Test void assassinatdunInconnu() {
        characterToKill.add(9);// si c'est un mauvais numéro
        assassincharacter.activePower(characterToKill, players, null, joueur1);
        for (Player player : players) {
            assertFalse(player.getKilled());// il ne se passe rien
        }
    }

    @Test
    void testGoodPriority(){
        assertEquals(assassincharacter.getPriority(),CharacterEnum.ASSASSINPRIORITY);
    }

    @Test
    void testAssassinConstructor(){
        BaseCharacter assassin2 = character.createCharacter();
        assert assassin2 != null;
        assertEquals(assassincharacter.getPriority(),assassin2.getPriority());
    }
}