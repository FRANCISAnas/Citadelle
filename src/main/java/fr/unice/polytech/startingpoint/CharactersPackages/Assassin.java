package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

/**
 * @author FRANCIS Anas
 * Created 03/11/2019
 * \\brief Classe de l'Assassin, cette classe permet d'instancier un objet Assassin pour tuer(ou pas) un autre personnage.
 */
public class Assassin extends BaseCharacter{

    public Assassin(){
        this.priority = CharacterEnum.ASSASSINPRIORITY;
        this.name = "Assassin";
    }

    /**
     * @author FRANCIS Anas
     * @author BABOURI Sofiane
     * @author DOMINGUEZ Lucas
     * Created 05/11/2019
     * \brief pouvoir de l'assassin sur un personnage,
     */

    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority()== priority) {
            if(autresParametres==null||autresParametres.size()!=1)return;
            int characterToKill = (int) autresParametres.get(0); // numéro du personnage à assassiner
            if (characterToKill != priority) { // ne peut pas se tuer lui même
                for (Player player : players) { //On cherche le joueur qui possède ce personnage
                    if (player.getCharacter().getPriority() == characterToKill) {
                        player.kill();//On tue le joueur
                        break;
                    }
                }
            }
        }
    }

    /**
     * @author DOMINGUEZ Lucas
     * \brief pas de pouvoir passif
     */
    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){}
}
