package fr.unice.polytech.startingpoint.BuildingsPackage;

import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class CimetiereBuilding extends WonderBuilding{


    public CimetiereBuilding(String str, int price, int victoryPoints, String type) {
        super(str, price, victoryPoints, type);
    }

    public void useWonderAbility(Player activePlayer, ArrayList<BaseBuildings> buildings){
        if(buildings.size()!=1 || activePlayer.getCharacter().getPriority()== CharacterEnum.CONDOTTIEREPRIORITY)return;
        ArrayList<BaseBuildings> hand = activePlayer.getBuildingsOfThisPlayer();
        BaseBuildings buildingToSave = buildings.get(0);
        if (activePlayer.getGold() > 0) {
            activePlayer.addGold(-1);
            hand.add(buildingToSave);
            activePlayer.getCity().destroyBuilding(buildingToSave);
            activePlayer.setBuildingsOfThisPlayer(hand);
        }
    }

}
