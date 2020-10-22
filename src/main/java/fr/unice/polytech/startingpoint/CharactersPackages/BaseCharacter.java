package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

/**
 * BaseBot est une interface pour les robots
 *
 * @author Julien Madrias
 */

public abstract class BaseCharacter {
    int priority;
    String name;
    boolean DETAIL=false;

    public void setDetail(boolean detail){this.DETAIL=detail;}
    public int getPriority(){
        return this.priority;
    }
    public String getName(){return this.name;}

    /**
     * \Brief Cette méthode permet au personnage d'effectuer son pouvoir qui nécessite une prise de décision
     * \Brief Elle diffère d'un personnage à un autre
     * Parametres : autresParametres est une liste variable ( c'est le résultat du choix des bots), les autres sont les même pour tout le monde mais pas toujours utilisés.
     */
    public abstract void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer);

    /**
     * \Brief Cette méthode permet au personnage d'effectuer son pouvoir qui ne nécessite pas une prise de décision (sauf magicien, condottiere)
     * \Brief Elle diffère d'un personnage à un autre
     * Parametres : autresParametres est une liste variable (c'est le résultat du choix des bots) , les autres sont les même pour tout le monde mais pas toujours utilisés.
     */
    public abstract void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer);

}
