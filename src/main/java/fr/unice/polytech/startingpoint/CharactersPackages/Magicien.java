package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

/**
 * @author FRANCIS Anas
 * created 01/11/2019
 * \brief Classe du Magicien avec les 2 pouvoirs du magicien
 */
public class Magicien extends BaseCharacter{
    public Magicien(){
        this.priority = CharacterEnum.MAGICIENPRIORITY;
        this.name="Magicien";
    }

    /**
     * @author FRANCIS Anas
     * @author DOMINGUEZ Lucas
     * created 01/11/2019
     * \brief Brief description: cette méthode permet d'utiliser le pouvoir du magicien à échanger toutes les bâtiments du joueurs avec un autre joueur.
     * @param activePlayer le joueur qui a le personnage du magicien
     */

    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority() == priority) {
            if(autresParametres==null|| autresParametres.size()!=1)return;
            Player victimPlayer = (Player) autresParametres.get(0);//joueur dont on veut échanger la main
            if(victimPlayer.getCharacter().getPriority() != priority){
                ArrayList<BaseBuildings> chosenBuildingsToReplace = activePlayer.getBuildingsOfThisPlayer();
                ArrayList<BaseBuildings> chosenBuildingsOfThatPlayer = victimPlayer.getBuildingsOfThisPlayer();
                activePlayer.setBuildingsOfThisPlayer(chosenBuildingsOfThatPlayer);
                victimPlayer.setBuildingsOfThisPlayer(chosenBuildingsToReplace);
                if (DETAIL)System.out.println(activePlayer.getName() + " échange sa main avec le joueur "+victimPlayer.getName());
            }
        }
    }



    /**
     * @author FRANCIS Anas
     * @author DOMINGUEZ Lucas
     * Created 02/11/2019
     * \brief Brief description cette methode permet d'utiliser le pouvoir du magicien sur le tas
     * On remet les cartes qu'on ne veut pas et on prend des nouvelles cartes de la pioche (de même nombre de ceux qu'on a remis!).
     * @param activePlayer le joueur qui possède le magicien
     */
    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer) {
        if (activePlayer.getCharacter().getPriority() == priority) {
            if(autresParametres==null|| autresParametres.size()!=1)return;
            ArrayList<BaseBuildings> unWantedCards = (ArrayList<BaseBuildings>) autresParametres.get(0);
            int nbUnWantedCards = unWantedCards.size();
            if (nbUnWantedCards > activePlayer.getBuildingsOfThisPlayer().size()) {
                return;
            } // on ne peut pas demander de changer plus de cartes que l'on a dans la main. D'où cette ligne
            ArrayList<BaseBuildings> activePlayerNewHand = activePlayer.getBuildingsOfThisPlayer();// la nouvelle liste des bâtiments de notre joueur
            for (BaseBuildings building : unWantedCards) {
                activePlayerNewHand.remove(building);
                activePlayer.putInDeckBuildings(building, buildingDeck);
            }
            activePlayerNewHand.addAll(activePlayer.drawNBuildings(buildingDeck, nbUnWantedCards));
            activePlayer.setBuildingsOfThisPlayer(activePlayerNewHand);// à la fin il a des nouvelles cartes
            if (DETAIL)System.out.println(activePlayer.getName() + " échange " + nbUnWantedCards+" cartes avec la pioche.");
        }
    }
}
