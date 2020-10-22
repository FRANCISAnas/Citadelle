package fr.unice.polytech.startingpoint.BuildingsPackage;

import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class ManufactureBuilding extends WonderBuilding {


    public ManufactureBuilding(String str, int price, int victoryPoints, String type) {
        super(str, price, victoryPoints, type);
    }

    public void useWonderAbility(Player activePlayer, ArrayList<BaseBuildings> buildings){
        if (activePlayer.getGold() < BuildingsTypeEnum.MANUFACTURETAXE) return;
        activePlayer.addGold(-1 * BuildingsTypeEnum.MANUFACTURETAXE);
        ArrayList<BaseBuildings> hand = activePlayer.getBuildingsOfThisPlayer();
        hand.addAll(activePlayer.drawNBuildings(buildings, BuildingsTypeEnum.NBOFMANUFACTUREDRAWNBUILDINGS));
        activePlayer.setBuildingsOfThisPlayer(hand);
        this.hasUseAbility();
    }
}
