package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class Architecte extends BaseCharacter{
    public static final int NBMAXOFBUILDINGSOFARCHITECT = 2;//2 et non 3 car on lui demandera s'il veut construir un bâtiment ou pas (il dira oui s'il peut) et on rajoute 2 autres parce que c'est l'rchitechte soit 3 au total

    public Architecte(){
        this.priority = CharacterEnum.ARCHITECTEPRIORITY;
        this.name = "Architecte";
    }
    /**
     * @author FRANCIS Anas
     * @author POULAIN
     * @author DOMINGUEZ
     * created 01/11/2019
     * \brief Permet de piocher 2 cartes en plus (indécidable au début du tour)
     * @param activePlayer le joueur qui a l'architecte, le deck.
     */

    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority()== priority){
            ArrayList<BaseBuildings> main = activePlayer.getBuildingsOfThisPlayer();
            main.addAll(activePlayer.drawNBuildings(buildingDeck, NBMAXOFBUILDINGSOFARCHITECT));
            activePlayer.setBuildingsOfThisPlayer(main);
            if (DETAIL)System.out.println(activePlayer.getName() + " pioche 2 cartes en plus grace à l'architecte.");
        }
    }

    /**
     * @author FRANCIS Anas
     * @author POULAIN
     * @author DOMINGUEZ
     * created 01/11/2019
     * \brief Permet de construire 2 batiments en plus
     * @param activePlayer le joueur qui a l'architecte, le deck autres les building à construire. (on s'asssure qu'il peut véritablement les construire)
     */

    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer) {
        if (activePlayer.getCharacter().getPriority() == priority && autresParametres != null) {
            BaseBuildings buildingToBuild2 = null;
            if (autresParametres.size() > 2 || autresParametres.size() == 0) return;
            BaseBuildings buildingToBuild1 = (BaseBuildings) autresParametres.get(0);
            if (autresParametres.size() == 2) {
                buildingToBuild2 = (BaseBuildings) autresParametres.get(1);
            }
            if (activePlayer.canBuildABuilding()) {
                activePlayer.useBuilding(buildingToBuild1);
            }
            if (activePlayer.canBuildABuilding() && buildingToBuild2 != null) {
                activePlayer.useBuilding(buildingToBuild2);
            }
        }
    }

}
