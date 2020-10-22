package fr.unice.polytech.startingpoint.BuildingsPackage;

import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class ObservatoireBuilding extends WonderBuilding {

    public ObservatoireBuilding(String str, int price, int victoryPoints, String type) {
        super(str, price, victoryPoints, type);
    }

    public void useWonderAbility(Player activePlayer, ArrayList<BaseBuildings> buildings){
        ArrayList<BaseBuildings> selection = new ArrayList<>(activePlayer.drawNBuildings(buildings, BuildingsTypeEnum.NBMAXOFDRAWNEDCARDSFOROBSERVATOIRE));
        BaseBuildings chosen = activePlayer.chooseBuildings(selection);
        selection.remove(chosen);
        for (BaseBuildings building : selection) {
            activePlayer.putInDeckBuildings(building, buildings);
        }
        ArrayList<BaseBuildings> main = activePlayer.getBuildingsOfThisPlayer();
        main.add(chosen);
        activePlayer.setBuildingsOfThisPlayer(main);
    }
}
