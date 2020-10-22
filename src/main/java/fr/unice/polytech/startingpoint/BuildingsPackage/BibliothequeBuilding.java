package fr.unice.polytech.startingpoint.BuildingsPackage;

import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class BibliothequeBuilding extends WonderBuilding{

    public BibliothequeBuilding(String str, int price, int victoryPoints, String type) {
        super(str, price, victoryPoints, type);
    }

    public void useWonderAbility(Player activePlayer, ArrayList<BaseBuildings> buildings){
        ArrayList<BaseBuildings> hand = activePlayer.getBuildingsOfThisPlayer();
        hand.addAll(activePlayer.drawNBuildings(buildings, BuildingsTypeEnum.NBMAXOFDRAWNEDCARDSFORBIBLIOTHEQUE));
        activePlayer.setBuildingsOfThisPlayer(hand);
    }

}
