package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;

import java.util.ArrayList;

public class BasicBot extends BaseBot {

    public BasicBot(int ID) {
        super(ID,"BasicBot");
    }

    public boolean wannaBuild(){ // dit s'il VEUT construire un batiment (ici pour le basic, tant qu'il peut, il veut)
        return (this.currentPlayer.canBuildABuilding());
    }

    public BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings){ //renvoie le bâtiment à construire de sa main (ici le premier constructible)
        for (BaseBuildings building : buildings) {
            if (building.getPrice() <= this.currentPlayer.getGold()) {
                return (building);
            }
        }
        return null;
    }

    public BaseCharacter chooseCharacter(ArrayList<BaseCharacter> characters){// renvoie le personnage qui veut prendre
        for (BaseCharacter character : characters ) {
            return character;
        }
        return null;
    }

    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings){// retourner le premier batiment (celui qu'il veut piocher)
        for (BaseBuildings building : buildings){
            return building;
        }
        return null;
    }

    public boolean drawOrGold(){// il pioche s'il a plus de 5 po. (5 est le meilleur)
        return currentPlayer.getGold() >= 5;
    }

    public ArrayList<String> architecteTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> assassinTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> condottiereTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> evequeTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> magicienTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> marchandTurn(){ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> roiTurn(){ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> voleurTurn(){ArrayList<String> turnActions = new ArrayList<>();
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    public ArrayList<Object> doActivePower(){return null;}
    public ArrayList<Object> doPassivePower(){return null;}

    public BaseBuildings laboratoryDecision(){
        return null;
    }
}
