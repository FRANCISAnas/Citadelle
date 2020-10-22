package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;
import java.util.ArrayList;


class Voleur extends BaseCharacter {
    Voleur(){
        this.priority = CharacterEnum.VOLEURPRIORITY;
        this.name = "Voleur";
    }

    /**
     * @author DOMINGUEZ Lucas
     * \brief pas de pouvoir passif
     */
    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){}//pas de pouvoir passif

    /**
     * @author DOMINGUEZ Lucas
     * \brief pouvoir de voler un personnage se déclenche au début du tour
     */

    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority() == priority) {
            if(autresParametres==null || autresParametres.size()!=1)return;
            int characterToSteal = (int) autresParametres.get(0);
            Player playerToSteal = null;
            for (Player player : players) {// recherche du personnage
                if (player.getCharacter().getPriority() == characterToSteal) {
                    playerToSteal = player;
                    break;
                }
            }
            if ((playerToSteal == null || playerToSteal.getKilled() || characterToSteal == CharacterEnum.ASSASSINPRIORITY)) return;// si il est mort ou que c'est l'assassin
            activePlayer.addGold(playerToSteal.getGold()); //On lui vole son argent
            if(DETAIL)System.out.println("Le joueur " + playerToSteal.getName()  + " qui a le " + playerToSteal.getCharacter().getName() +" se fait voler "+ playerToSteal.getGold() + " pièces par " +activePlayer.getName() + "\n");
            playerToSteal.setGold(0);
        }
    }



}
