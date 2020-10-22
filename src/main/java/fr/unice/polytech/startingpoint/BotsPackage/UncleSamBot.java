package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.BuildingsPackage.MerveilleEnum;
import fr.unice.polytech.startingpoint.CharactersPackages.Architecte;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;


/**
 * @author FRANCIS Anas
 * \brief Ce bot va chercher à récupérer l'argent pour construir des bâtiment de préférence chère(pour empêcher le condottière de les péter)
 */
public class UncleSamBot extends BaseBot{
    UncleSamBot(int ID) {super(ID,"UncleSamBot");}

    public boolean wannaBuild(){ // true s'il peut construire false sinon
        return this.currentPlayer.canBuildABuilding();
    }

    /**
     * @author FRANCIS Anas
     * @param buildings list des bâtiment à partir duquel on choisira 1
     * @return le bâtiment qu'on veut garder dans notre main, Bien sûr l'autre carte sera remise dans le tas des bâtiments dans le GameEngin.
     */
    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings) {
        if (buildings==null || buildings.size()==0)return null;
        BaseBuildings buildingChosen = highestCostBuilding(buildings, true);
        if(buildingChosen==null) return highestCostBuilding(buildings,false);
        else return buildingChosen;
    }

    public boolean drawOrGold(){//seulement quand il n'a plus de carte
        return currentPlayer.getBuildingsOfThisPlayer().size() == 0 || currentPlayer.imposableBuildings();
    }

    public ArrayList<String> architecteTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        return turnActions;
    }
    public ArrayList<String> assassinTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> condottiereTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if(currentPlayer.getCity().getNbrMilitaryBuildings()==0){
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
            turnActions.add(CharacterEnum.ACTIVEPOWER);
        }
        else {
            turnActions.add(CharacterEnum.ACTIVEPOWER);
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        }
        if(currentPlayer.getPlayerMaxBuildings(this.currentPlayer.getGamePlayers(),true).getNbOfbuildingsInCity()>=6 || this.currentPlayer.getGold()>=2)turnActions.add(CharacterEnum.PASSIVEPOWER);

        return turnActions;
    }
    public ArrayList<String> evequeTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if(currentPlayer.getCity().getNbrReligiousBuildings()==0){
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
            turnActions.add(CharacterEnum.ACTIVEPOWER);
        }
        else {
            turnActions.add(CharacterEnum.ACTIVEPOWER);
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        }
        return turnActions;
    }
    public ArrayList<String> magicienTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if(this.currentPlayer.getBuildingsOfThisPlayer().size()<=2 && magicienActivePower()!= null)turnActions.add(CharacterEnum.PASSIVEPOWER);
        else turnActions.add(CharacterEnum.ACTIVEPOWER);
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }
    public ArrayList<String> marchandTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if(currentPlayer.getCity().getNbrCommercialBuildings()==0){
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
            turnActions.add(CharacterEnum.ACTIVEPOWER);
        }
        else {
            turnActions.add(CharacterEnum.ACTIVEPOWER);
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        }
        return turnActions;
    }
    public ArrayList<String> roiTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if(currentPlayer.getCity().getNbrNobleBuildings()==0){
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
            turnActions.add(CharacterEnum.ACTIVEPOWER);
        }
        else {
            turnActions.add(CharacterEnum.ACTIVEPOWER);
            if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        }
        return turnActions;
    }
    public ArrayList<String> voleurTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if(this.currentPlayer.getGold()>BuildingsTypeEnum.MANUFACTURETAXE && this.currentPlayer.getBuildingsOfThisPlayer().size()<=1)turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        if (wannaBuild()) turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    public ArrayList<Object> doActivePower(){
        ArrayList<Object> autresParametres = new ArrayList<>();
        switch (currentPlayer.getCharacter().getPriority()){
            case CharacterEnum.ASSASSINPRIORITY : autresParametres.add(CharacterEnum.CONDOTTIEREPRIORITY);return autresParametres;
            case CharacterEnum.VOLEURPRIORITY : autresParametres.add(CharacterEnum.MAGICIENPRIORITY);return autresParametres;
            case CharacterEnum.MAGICIENPRIORITY : {
                ArrayList<BaseBuildings> unwantedBuildings = magicienActivePower();
                if(unwantedBuildings != null){
                    autresParametres.add(unwantedBuildings);
                    return autresParametres;
                }
                break;
            }
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

    /**
     * @author FRANCIS Anas
     * @return une liste des Bâtiments qu'on va construire
     */
    public ArrayList<BaseBuildings> architecteBuildingschoice(){
        ArrayList<BaseBuildings> listOfBuildingToReturn = new ArrayList<>();
        ArrayList<BaseBuildings> localBuildingsOfPlayer = new ArrayList<>(this.currentPlayer.getBuildingsOfThisPlayer());
        int i = 0;
        BaseBuildings currentBuildingChosen;
        while(i < Architecte.NBMAXOFBUILDINGSOFARCHITECT){
            if (currentPlayer.canBuildABuilding()) {// on vérifie qu'il a l'argent et des cartes dans sa main pour construire
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


    /**
     * @author FRANCIS Anas
     * @return list des bâtiments qui ne les intérésse pas pour remmetre dans le tas
     */
    public ArrayList<BaseBuildings> magicienActivePower(){
        ArrayList<BaseBuildings> unwantedBuildings = new ArrayList<>();
        boolean checked = true;
        for (BaseBuildings buildingInHand: currentPlayer.getBuildingsOfThisPlayer()) {
            for (BaseBuildings buildingInCity:currentPlayer.getCity().getBuildings()) {
                if( (buildingInHand.getType().equals(buildingInCity.getType())) || buildingInHand.getName().equals(buildingInCity.getName())){ // je compare le bâtiment dans sa main avec Les bâtiments dans sa cité s'il y a déjà même type ou même nom on va le retirer
                    checked = false;
                    break;
                }
            }
            if(checked)unwantedBuildings.add(buildingInHand);
            checked = true;
        }
        if(unwantedBuildings.size()>0)return unwantedBuildings;
        else return null;
    }

    /**
     * @author FRANCIS Anas
     * @return Le maxPlayer le joueur qui possède le plus de bâtiments
     */
    public Player magicianPassivePower(){
        return this.currentPlayer.getPlayerMaxBuildings(this.currentPlayer.getGamePlayers(), false);
    }

    /**
     * @author FRANCIS Anas
     * @return ArrayList des Objets dont le première objet étant le joueur chanceux qui a le plus des bâtiments et le deuxième objets étant le bâtiment qu'on veut détruire
     */
    public ArrayList<Object> condottierePassivePower(){
        ArrayList<Object> playerandHisBuilding = new ArrayList<>();
        Player maxPlayer;
        ArrayList<Player> thesePlayers = this.currentPlayer.playersButWithout(CharacterEnum.EVEQUEPRIORITY);
        maxPlayer = this.currentPlayer.getPlayerMaxBuildings(thesePlayers,true);
        if(maxPlayer == null || maxPlayer.getBuildingsBuilt().size() == 0)return null;
        BaseBuildings buildingChosen = maxPlayer.getBuildingsBuilt().get(0);
        for (BaseBuildings building_bis: maxPlayer.getBuildingsBuilt()) {
            if(building_bis.getPrice()<buildingChosen.getPrice())buildingChosen = building_bis;
        }
        playerandHisBuilding.add(maxPlayer);
        if(this.currentPlayer.getGold()>=buildingChosen.getPrice() && !buildingChosen.getName().equals(MerveilleEnum.DONJON.toString()))playerandHisBuilding.add(buildingChosen);
        return playerandHisBuilding;
    }


    /**
     * @author FRANCIS Anas
     * @return list des objets qu'on va utiliser pour appliquer le pouvoir passive (pour la condottière et le magician)
     */

    public ArrayList<Object> doPassivePower(){
        ArrayList<Object> autresParametres = new ArrayList<>();
        if(currentPlayer.getCharacter().getPriority()== CharacterEnum.MAGICIENPRIORITY){
            autresParametres.add(magicianPassivePower()); //renvoit le joueur pour échanger de mains.
            return autresParametres;
        }
        else if(currentPlayer.getCharacter().getPriority()== CharacterEnum.CONDOTTIEREPRIORITY){
            return condottierePassivePower();//renvoit la cité et le batiment à détruire.
        }
        else return null;
    }

    /**
     * @author FRANCIS Anas
     * @return le bâtiment le plus chere dans sa main qui va remettre dans la pioche pour récupérer une pièce d'or
     */

    public BaseBuildings laboratoryDecision(){
        if (this.currentPlayer.getGold() >= 1 && this.currentPlayer.getNbOfbuildingsInHisHand()>0){
            return highestCostBuilding(this.currentPlayer.getBuildingsOfThisPlayer(),false);
        }
        return null;
    }


    /**
     * \brief En fonction de nombre maximum de type dans sa cité il va construire le bâtiment qui correspond à ce type, ainsi lorsuq'il prendra le personnage
     * qui correspond à ce type son gain sera maximiser.
     * created 19/11/2019
     * @param buildings list des bâtiments dans sa main
     */

    public BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings){
        if(buildings.size()==0) return null;
        ArrayList<String> typesSorted = numberTypeSorted(false);//on récupère le type qui le max de batiment
        for(String s : typesSorted){
            if(hasTypeInHand(s,buildings)!=null)
                return hasTypeInHand(s,buildings);//construit le batiment dont on a le plus de type sur le terrain
        }
        //si on arrive là c'est que on n'a aucun type de batiment désiré ou que la cité est vide
        if(currentPlayer.getCharacter().getPriority()==CharacterEnum.ROIPRIORITY && currentPlayer.getCity().getNbrNobleBuildings()==0 && hasTypeInHand(BuildingsTypeEnum.NOBLE.toString(),buildings)!=null){
            return hasTypeInHand(BuildingsTypeEnum.NOBLE.toString(),buildings);
            //si il a un perso particulier et rien de construit il va en constuire 1 de ce type pour ensuite récupérer des golds avec le pouvoir
        }
        else if(currentPlayer.getCharacter().getPriority()==CharacterEnum.EVEQUEPRIORITY && currentPlayer.getCity().getNbrReligiousBuildings()==0 && hasTypeInHand(BuildingsTypeEnum.RELIGIEUX.toString(),buildings)!=null){
            return hasTypeInHand(BuildingsTypeEnum.RELIGIEUX.toString(),buildings);
        }
        else if(currentPlayer.getCharacter().getPriority()==CharacterEnum.MARCHANDPRIORITY && currentPlayer.getCity().getNbrCommercialBuildings()==0 && hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(),buildings)!=null){
            return hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(),buildings);
        }
        else if(currentPlayer.getCharacter().getPriority()==CharacterEnum.CONDOTTIEREPRIORITY && currentPlayer.getCity().getNbrMilitaryBuildings()==0 && hasTypeInHand(BuildingsTypeEnum.MILITAIRE.toString(),buildings)!=null){
            return hasTypeInHand(BuildingsTypeEnum.MILITAIRE.toString(),buildings);
        }
        if (hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(),buildings)!=null) return hasTypeInHand(BuildingsTypeEnum.MARCHAND.toString(),buildings);
        //sinon il va construire des batiments marchands
        //sinon il construit le plus cher
        return highestCostBuilding(buildings,true);

    }

    public BaseBuildings hasTypeInHand(String type,ArrayList<BaseBuildings> buildings) {//renvoi un batiment constructible d'un type choisit et null sinon
        for (BaseBuildings building : buildings) {
            if (building.getType().equals(type) && building.getPrice()<= currentPlayer.getGold()) {
                return building;
            }
        }
        return null;
    }
    /**
     * @author FRANCIS Anas
     * @return BaseCharacter le personnage qui veut choisir en fonction de ses argents, i.e s'il a assez d'argent il va choisir un personnage
     * qui va l'aider à construire, sinon il va essayer de prendre un personnage qui peut lui apporter le maximum possible.
     * @param availableCharacters list de toutes les personnage à partir duqel il fera son choix
     * created 19/11/2019
     */
    public BaseCharacter chooseCharacter(ArrayList<BaseCharacter> availableCharacters){
        ArrayList<String> sortedTypes = numberTypeSorted(false);
        ArrayList<Integer> preferedCharacter = new ArrayList<>();
        if(currentPlayer.getCity().getNbrBuildings()==0) preferedCharacter.add(CharacterEnum.MARCHANDPRIORITY);
        for(String type : sortedTypes){
            if(type.equals(BuildingsTypeEnum.MILITAIRE.toString())) preferedCharacter.add(CharacterEnum.CONDOTTIEREPRIORITY);
            else if(type.equals(BuildingsTypeEnum.MARCHAND.toString())) preferedCharacter.add(CharacterEnum.MARCHANDPRIORITY);
            else if(type.equals(BuildingsTypeEnum.NOBLE.toString())) preferedCharacter.add(CharacterEnum.ROIPRIORITY);
            else if(type.equals(BuildingsTypeEnum.RELIGIEUX.toString())) preferedCharacter.add(CharacterEnum.EVEQUEPRIORITY);
        }
        preferedCharacter.add(CharacterEnum.ARCHITECTEPRIORITY);
        preferedCharacter.add(CharacterEnum.MARCHANDPRIORITY);preferedCharacter.add(CharacterEnum.ROIPRIORITY);preferedCharacter.add(CharacterEnum.CONDOTTIEREPRIORITY);preferedCharacter.add(CharacterEnum.EVEQUEPRIORITY);//on les rajoute si la cité est vide (et ça change rien)
        preferedCharacter.add(CharacterEnum.VOLEURPRIORITY);preferedCharacter.add(CharacterEnum.ASSASSINPRIORITY);preferedCharacter.add(CharacterEnum.MAGICIENPRIORITY);
        for(int priority : preferedCharacter){
            for(BaseCharacter character : availableCharacters){
                if(character.getPriority()==priority){
                    return character;
                }
            }
        }
        return null;
    }

}