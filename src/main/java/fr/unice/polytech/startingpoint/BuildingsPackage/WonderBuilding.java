package fr.unice.polytech.startingpoint.BuildingsPackage;

import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

public class WonderBuilding extends BaseBuildings {
    private boolean used;

    public WonderBuilding(String str, int price, int victoryPoints, String type){
        super(str,price,victoryPoints,type);
        this.used = false;
    }

    void hasUseAbility(){
        this.used = true;
    }

    public boolean getUsed(){return this.used;}

    public void resetBuilding(){
        this.used = false;
    }

    public void useWonderAbility(Player activePlayer, ArrayList<BaseBuildings> buildings) {}

}
