package fr.unice.polytech.startingpoint.BuildingsPackage;

import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class LaboratoireBuilding extends WonderBuilding{


    public LaboratoireBuilding(String str, int price, int victoryPoints, String type) {
        super(str, price, victoryPoints, type);
    }

    public void useWonderAbility(Player activePlayer, ArrayList<BaseBuildings> buildings){
        BaseBuildings buildingToDrop = buildings.get(0);
        if (buildingToDrop != null){
            ArrayList<BaseBuildings> newBuildings =  activePlayer.getBuildingsOfThisPlayer();
            newBuildings.remove(buildingToDrop);
            activePlayer.setBuildingsOfThisPlayer(newBuildings);
            activePlayer.addGold(BuildingsTypeEnum.LABORATORYINCOMEMONEY);
            this.hasUseAbility();
        }
    }

}
