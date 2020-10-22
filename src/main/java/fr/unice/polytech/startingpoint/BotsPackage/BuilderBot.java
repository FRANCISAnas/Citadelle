package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.Architecte;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;

import java.util.ArrayList;

/**
 * Ce bot va chercher à construire le plus de bâtiments, les moins chers possibles.
 * @author Timothée Poulain
 * @author lucas Dominguez
 */

 class BuilderBot extends BaseBot {
    BuilderBot(int ID) {
        super(ID, "BuilderBot");
    }

    /**
     * Lorsque le rôle de l'architecte sera disponible, il le choisira en priorité.
     * @param characters les personnages que le joueur peut choisir
     * @return Le personnage choisi
     */
    public BaseCharacter chooseCharacter(ArrayList<BaseCharacter> characters) {// ici il choisit l'architecte en priorité
        BaseCharacter role = characters.get(0);
        for (BaseCharacter availableRole : characters) {
            if (availableRole.getPriority() == CharacterEnum.ARCHITECTEPRIORITY){
                role = availableRole;
                return role;
            }
        }
        for (BaseCharacter availableRole : characters) {
            if (availableRole.getPriority() == CharacterEnum.EVEQUEPRIORITY){//et sinon l'évèque (pour pas qu'on puisse détruire ses batiments)
                role = availableRole;
                return role;

            }
        }
        return role;//sinon il prend le premier.
    }

    /**
     * @param buildings les batiments que le joueur peut choisir
     * @return Le batiment choisi
     */
    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings) { //il pioche celui qui a le cout le plus faible
        if (buildings.size() == 0) return null;
        return lowestCostBuilding(buildings);
    }

    /**
     * @param buildings Les batiments que le joueur peut construire
     * @return Le batiment à construire
     */
    public BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings) {//il construit celui qui a le cout le plus faible
        if (currentPlayer.canBuildABuilding()) return lowestCostBuilding(buildings);
        else return null;
    }

    /**
     * Le comportement du BuilderBot est de construire le plus possible.
     * @return true si il peut construire, sinon false
     */
    public boolean wannaBuild(){//Il veut construire s'il le peut
        return this.currentPlayer.canBuildABuilding();

    }

    public boolean drawOrGold(){
        return currentPlayer.getBuildingsOfThisPlayer().size() == 0 ; // s'il a pas de batiments : il pioche
    }
    public ArrayList<String> architecteTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.BUILD);
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        return turnActions;
    }

    public ArrayList<String> assassinTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> condottiereTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> evequeTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> magicienTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> marchandTurn(){ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> roiTurn(){ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> voleurTurn(){ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }



    public ArrayList<Object> doActivePower(){ // renvoi la liste des arguments choisit par le bot pour effectuer le pouvoir
        ArrayList<Object> autresParametres = new ArrayList<>();
        switch (currentPlayer.getCharacter().getPriority()){
            case CharacterEnum.ASSASSINPRIORITY : autresParametres.add(CharacterEnum.CONDOTTIEREPRIORITY);return autresParametres;
            case CharacterEnum.VOLEURPRIORITY : autresParametres.add(CharacterEnum.MAGICIENPRIORITY);return autresParametres;
            case CharacterEnum.MAGICIENPRIORITY : break;
            case CharacterEnum.ARCHITECTEPRIORITY : {
                ArrayList<BaseBuildings> buildingsToBuild = architecteBuildingschoice();
                if (buildingsToBuild != null) {
                    autresParametres.addAll(buildingsToBuild);
                    return autresParametres;
                }
                break;
            }
            }
        return null;
    }
    ArrayList<BaseBuildings> architecteBuildingschoice(){
        ArrayList<BaseBuildings> listOfBuildingToReturn = new ArrayList<>();
        ArrayList<BaseBuildings> localBuildingsOfPlayer = new ArrayList<>(this.currentPlayer.getBuildingsOfThisPlayer());
        int i = 0;
        BaseBuildings currentBuildingChosen;
        while(i < Architecte.NBMAXOFBUILDINGSOFARCHITECT){
            if (currentPlayer.canBuildABuilding(localBuildingsOfPlayer)) {// on vérifie qu'il a l'argent et des cartes dans sa main pour construire
                currentBuildingChosen = chooseBuildingsToBuild(localBuildingsOfPlayer);//on choisit un building à construire parmis ceux restant
                if(currentBuildingChosen!=null) {
                    localBuildingsOfPlayer.remove(currentBuildingChosen); // pour éviter de rechoisir à chaque tour de boucle
                    listOfBuildingToReturn.add(currentBuildingChosen);
                }
            }
            i++;
        }
        if(listOfBuildingToReturn.size()>0)return listOfBuildingToReturn;
        return null;
    }

    // renvoi la liste des arguments choisit par le bot pour effectuer le pouvoir
    public ArrayList<Object> doPassivePower(){// il n'y a que le magicien et le condottiere qui ont un passif qui demande une reflexion du bot.
        if(currentPlayer.getCharacter().getPriority()== CharacterEnum.MAGICIENPRIORITY){
             return null; //renvoit le joueur pour échanger de mains.
        }
        else if(currentPlayer.getCharacter().getPriority()== CharacterEnum.CONDOTTIEREPRIORITY){
            return null; //renvoit la cité et le batiment à détruire.
        }
        else return null;
    }

    public BaseBuildings laboratoryDecision(){
        return highestCostBuilding(currentPlayer.getBuildingsOfThisPlayer(),false);
    }
}
