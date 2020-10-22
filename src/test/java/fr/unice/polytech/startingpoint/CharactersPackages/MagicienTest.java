package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BasicBot;
import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;
import fr.unice.polytech.startingpoint.ReadJSONFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author FRANCIS Anas
 * created 02/11/2019
 * \brief On teste les methodes de la classe Magicien dans cette classe.
 */

class MagicienTest {
    private ArrayList<BaseBuildings> cardsOfBuildings = new ArrayList<>();
    private Player myPlayer, victimPlayer;
    private ArrayList<BaseBuildings> myPlayerHand;
    private CharacterEnum character = CharacterEnum.Magicien;
    private CharacterEnum autre = CharacterEnum.Roi;
    private BaseCharacter magicienCharacter;

    @BeforeEach
    void setUp() {
        magicienCharacter = character.createCharacter();
        BaseCharacter roiCharacter = autre.createCharacter();
        BaseBot bot1 = new BasicBot(1);
        BaseBot bot2 = new BasicBot(2);
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        Collections.shuffle(cardsOfBuildings);
        myPlayer = new Player(bot1, 1);
        victimPlayer = new Player(bot2,2);
        myPlayer.setCharacter(magicienCharacter);
        victimPlayer.setCharacter(roiCharacter);

        }

    /**
     * @author FRANCIS Anas
     * created 02/11/2019
     * \brief Brief description je teste si la méthode charcterAbilityOnAplayer change bien les mains entre les deux joueurs!
     */
    @Test
    void passivePowerTest() {

        ArrayList<Object> autresParametres = new ArrayList<>();
        autresParametres.add(victimPlayer);

        for(int j = 0;j<10;j++) {//nb de cartes du magicien
            for (int k = 0; k < 10; k++) {//nb de cartes de la victime
                myPlayer.getBuildingsOfThisPlayer().clear();
                victimPlayer.getBuildingsOfThisPlayer().clear();
                ReadJSONFile jsonFile = new ReadJSONFile();
                cardsOfBuildings = jsonFile.create();
                Collections.shuffle(cardsOfBuildings);


                myPlayerHand = myPlayer.drawNBuildings(cardsOfBuildings, j);//main du magicien initiale
                ArrayList<BaseBuildings> victimPlayerHand = victimPlayer.drawNBuildings(cardsOfBuildings, k);//main de la victime initiale
                myPlayer.setBuildingsOfThisPlayer(myPlayerHand);
                victimPlayer.setBuildingsOfThisPlayer(victimPlayerHand);
                magicienCharacter.passivePower(autresParametres, null, null, myPlayer);//echange des mains
                assertEquals(victimPlayerHand.size(), myPlayer.getBuildingsOfThisPlayer().size());//la taille des mains sont bien échangées
                assertEquals(myPlayerHand.size(), victimPlayer.getBuildingsOfThisPlayer().size());
                for (int i = 0; i < victimPlayerHand.size(); i++) {
                    assertEquals(victimPlayerHand.get(i), myPlayer.getBuildingsOfThisPlayer().get(i));
                }
                for (int i = 0; i < myPlayerHand.size(); i++) {
                    assertEquals(myPlayerHand.get(i), victimPlayer.getBuildingsOfThisPlayer().get(i));
                }
            }
        }

    }

    /**
     * @author FRANCIS Anas
     * Created 02/11/2019
     * \brief Brief Description: on verifie si le joueur a bien pioché le bonne nombre des cartes dans le tas du jeu et remis celles qu'il ne veut pas.
     */
    @Test
    void activePowerTest() {
        ArrayList<Object> autresParametres = new ArrayList<>();
        for(int j=0;j<10;j++) {//nb de cartes dans la main du magicien
            for (int k = 0; k < 2*j; k++) {//nb de cartes qu'il veut échanger
                autresParametres.clear();
                myPlayer.getBuildingsOfThisPlayer().clear();
                ReadJSONFile jsonFile = new ReadJSONFile();
                cardsOfBuildings = jsonFile.create();
                Collections.shuffle(cardsOfBuildings);
                myPlayerHand = myPlayer.drawNBuildings(cardsOfBuildings, j);//main du magicien initiale
                int tailledudeck = cardsOfBuildings.size();
                myPlayer.setBuildingsOfThisPlayer(myPlayerHand);

                autresParametres.add(myPlayer.drawNBuildings(myPlayerHand, k));//liste des cartes qu'il ne veut plus
                magicienCharacter.activePower(autresParametres, null, cardsOfBuildings, myPlayer);//échange

                assertEquals(myPlayerHand.size(), myPlayer.getBuildingsOfThisPlayer().size());//sa main n'a pas changé de taille.
                assertEquals(tailledudeck, cardsOfBuildings.size());//le deck a toujours le même nombre de cartes
            }
        }

    }
}