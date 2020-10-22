package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

/** Stratégie du bot intelligent :
 * Ce bot va définir à chaque tour une cible parmi les autres joueurs,
 * qu'il considère comme le joueur le plus avancé.
 * Ensuite, le bot agit selon 8 situations qui sont définies en fonction :
 *  - De la taille de sa ville par rapport à la cible;
 *  - Du nombre de pièces d'or par rapport à la cible;
 *  - Du nombre de cartes POSABLES par rapport à la cible;
 *
 * Ordre d'importance de la progression d'un joueur :
 * Nombre de bâtiments posés > Or en possession du joueur > Cartes en main du joueur
 * La progression du bot peut donc être définie par une formule du type :
 * 5*(Bat posés)+3*(Or)+(cartes en main)
 *
 * Le bot va également calculer sa propre côte de progression, afin de se situer par rapport aux autres.
 *
 * CAS 1 : Ville--, Gold--, Cartes--
 * Rôle souhaité : Voleur, Condottière, Assassin, Magicien
 * Demande des pièces
 * Construit le bâtiment le moins cher s'il le peut
 *
 * CAS 2 : Ville--, Gold--, Cartes++
 * Rôle souhaité : Voleur, Condottière
 * Demande des pièces
 * Construit le bâtiment le moins cher s'il le peut
 *
 * CAS 3 : Ville--, Gold++, Cartes--
 * Rôle souhaité : Magicien, Architecte, Condottière
 * Demande des bâtiments, le moins cher qu'il pourrait construire au prochain tour
 * Construit le bâtiment le moins cher
 *
 * CAS 4 : Ville--, Gold++, Cartes++
 * Rôle souhaité : Architecte, Condottière
 * Demande des bâtiments, le moins cher qu'il pourrait construire au prochain tour
 * Construit le(s) bâtiment(s) le(s) plus cher(s)
 *
 * CAS 5 : Ville++, Gold--, Cartes--
 * Rôle souhaité : Voleur, Magicien
 * Demande des pièces
 * Ne veut pas construire
 *
 * CAS 6 : Ville++, Gold--, Cartes++
 * Rôle souhaité : Voleur
 * Demande des pièces
 * Ne veut pas construire
 *
 * CAS 7 : Ville++, Gold++, Cartes--
 * Rôle souhaité : Magicien
 * Demande des bâtiments, le plus cher qu'il pourrait construire au prochain tour
 * Construit le bâtiment le plus cher
 *
 * CAS 8 : Ville++, Gold++, Cartes++
 * Rôle souhaité : Condottière
 * Demande des bâtiments, le plus cher qu'il pourrait construire au prochain tour
 * Construit le bâtiment le moins cher
 *
 * @author Timothée Poulain
 */

public class MentalistBot extends BaseBot {
    private Player target;
    private BaseCharacter lastTargetChar;

    MentalistBot(int ID, String name) {
        super(ID, name);
    }

    public MentalistBot(int id) {
        super(id, "MentalistBot");
    }

    @Override
    public boolean wannaBuild() {
        boolean choice;
        switch (getScenario()){
            case 5:
                choice = false;
                break;
            case 6:
                choice = false;
                break;
            default:
                choice = this.getCurrentPlayer().canBuildABuilding();
        }
        return choice;
    }

    @Override
    public BaseCharacter chooseCharacter(ArrayList<BaseCharacter> characters) {
        ArrayList<Integer> preferences = new ArrayList<>();
        switch (getScenario()){
            case 1:
                preferences.add(CharacterEnum.VOLEURPRIORITY);
                preferences.add(CharacterEnum.CONDOTTIEREPRIORITY);
                preferences.add(CharacterEnum.ASSASSINPRIORITY);
                preferences.add(CharacterEnum.MAGICIENPRIORITY);
                break;
            case 2:
                preferences.add(CharacterEnum.MARCHANDPRIORITY);
                preferences.add(CharacterEnum.VOLEURPRIORITY);
                preferences.add(CharacterEnum.CONDOTTIEREPRIORITY);
                break;
            case 3:
                preferences.add(CharacterEnum.MAGICIENPRIORITY);
                preferences.add(CharacterEnum.ARCHITECTEPRIORITY);
                preferences.add(CharacterEnum.CONDOTTIEREPRIORITY);
                break;
            case 4:
                preferences.add(CharacterEnum.ARCHITECTEPRIORITY);
                preferences.add(CharacterEnum.CONDOTTIEREPRIORITY);
                break;
            case 5:
                preferences.add(CharacterEnum.VOLEURPRIORITY);
                preferences.add(CharacterEnum.MAGICIENPRIORITY);
                break;
            case 6:
                preferences.add(CharacterEnum.VOLEURPRIORITY);
                break;
            case 7:
                preferences.add(CharacterEnum.MAGICIENPRIORITY);
                break;
            default:
                preferences.add(CharacterEnum.CONDOTTIEREPRIORITY);
        }
        BaseCharacter choosenRole = characters.get(0);
        for (int i = preferences.size()-1; i >= 0; i--){
            for (BaseCharacter availChar : characters){
                if (availChar.getPriority() == preferences.get(i)){
                    choosenRole = availChar;
                }
            }
        }
        return choosenRole;
    }

    @Override
    public boolean drawOrGold() {
        boolean choice;
        switch (getScenario()){
            case 1:
                choice = currentPlayer.getBuildingsOfThisPlayer().size() == 0 || currentPlayer.imposableBuildings();
                break;
            case 2:
                choice = currentPlayer.getBuildingsOfThisPlayer().size() == 0 || currentPlayer.imposableBuildings();
                break;
            case 3:
                choice = false;
                break;
            case 4:
                choice = false;
                break;
            case 5:
                choice = currentPlayer.getBuildingsOfThisPlayer().size() == 0 || currentPlayer.imposableBuildings();
                break;
            case 6:
                choice = currentPlayer.getBuildingsOfThisPlayer().size() == 0 || currentPlayer.imposableBuildings();
                break;
            case 7:
                choice = false;
                break;
            default:
                choice = false;
        }
        return choice;
    }

    @Override
    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings) {
        BaseBuildings choosenBuilding;
        switch (getScenario()){
            case 7:
                choosenBuilding = super.highestCostBuilding(buildings, true);
                break;
            case 8:
                choosenBuilding = super.highestCostBuilding(buildings, true);
                break;
            default:    //Surtout pour les scénarios 3 et 4
                choosenBuilding = super.lowestCostBuilding(buildings);
        }
        return choosenBuilding;
    }

    @Override
    public BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings) {
        BaseBuildings choice;
        switch (getScenario()){
            case 1:
                choice = super.lowestCostBuilding(buildings);
                break;
            case 2:
                choice = super.lowestCostBuilding(buildings);
                break;
            case 3:
                choice = super.lowestCostBuilding(buildings);
                break;
            case 4:
                choice = super.highestCostBuilding(buildings, true);
                break;
            case 7:
                choice = super.highestCostBuilding(buildings, true);
                break;
            case 8:
                choice = super.lowestCostBuilding(buildings);
                break;
            default:
                choice = super.lowestCostBuilding(buildings);
        }
        return choice;
    }

    @Override
    public ArrayList<String> architecteTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<String> assassinTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<String> condottiereTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<String> evequeTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<String> magicienTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        if(this.currentPlayer.getBuildingsOfThisPlayer().size()<=2 && magicienActivePower()!= null)turnActions.add(CharacterEnum.ACTIVEPOWER);
        else turnActions.add(CharacterEnum.PASSIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        if (wannaBuild()){
            turnActions.add(CharacterEnum.BUILD);
        }
        return turnActions;
    }

    @Override
    public ArrayList<String> marchandTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<String> roiTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<String> voleurTurn() {
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.ACTIVEPOWER);
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
        return turnActions;
    }

    @Override
    public ArrayList<Object> doActivePower() {
        ArrayList<Object> autresParametres = new ArrayList<>();
        switch (currentPlayer.getCharacter().getPriority()){
            case CharacterEnum.ASSASSINPRIORITY:
                if (this.lastTargetChar != null){
                    autresParametres.add(this.lastTargetChar.getPriority());
                }else{
                    autresParametres.add(CharacterEnum.VOLEURPRIORITY);
                }
                break;
            case CharacterEnum.VOLEURPRIORITY:
                if (this.lastTargetChar != null){
                    autresParametres.add(this.lastTargetChar.getPriority());
                }else{
                    autresParametres.add(CharacterEnum.MARCHANDPRIORITY);
                }
                break;
            case CharacterEnum.MAGICIENPRIORITY: {
                ArrayList<BaseBuildings> unwantedBuildings = magicienActivePower();
                if(unwantedBuildings != null){
                    autresParametres.add(unwantedBuildings);// ATTENTION ici tu auras un tableau qui contient un tableau dans lequel il y a les unWantedBuildings
                    return autresParametres;
                }
                break;
            }

            case CharacterEnum.ARCHITECTEPRIORITY:
                if (currentPlayer.getBuildingsOfThisPlayer().size() > 0){
                    autresParametres.add(lowestCostBuilding(currentPlayer.getBuildingsOfThisPlayer()));
                }
                break;
            default:
                autresParametres.add(CharacterEnum.VOLEURPRIORITY);
        }
        return autresParametres;
    }

    @Override
    public ArrayList<Object> doPassivePower() {
        ArrayList<Object> autresParametres = new ArrayList<>();
        switch (currentPlayer.getCharacter().getPriority()) {
            case CharacterEnum.MAGICIENPRIORITY:
                autresParametres.add(magicianPassivePower());
                break;
            case CharacterEnum.CONDOTTIEREPRIORITY:
                ArrayList<Object> playerandHisBuilding = new ArrayList<>();
                Player maxPlayer;
                maxPlayer = this.currentPlayer.getPlayerMaxBuildings(this.getCurrentPlayer().getGamePlayers(), true);
                if(maxPlayer.getBuildingsBuilt().size() == 0)return null;
                BaseBuildings buildingChosen = maxPlayer.getBuildingsBuilt().get(0);
                for (BaseBuildings building_bis: maxPlayer.getBuildingsBuilt()) {
                    if(building_bis.getPrice()<buildingChosen.getPrice())buildingChosen = building_bis;
                }
                playerandHisBuilding.add(maxPlayer);
                if(this.currentPlayer.getGold()>=buildingChosen.getPrice())playerandHisBuilding.add(buildingChosen);
                return playerandHisBuilding;
            default:
                autresParametres.add(CharacterEnum.VOLEURPRIORITY);
        }
        return autresParametres;
    }

    public ArrayList<BaseBuildings> magicienActivePower(){
        ArrayList<BaseBuildings> unwantedBuildings = new ArrayList<>();
        boolean checked = true;
        for (BaseBuildings buildingInHand: currentPlayer.getBuildingsOfThisPlayer()) {
            for (BaseBuildings buildingInCity:currentPlayer.getBuildingsBuilt()) {
                if( buildingInHand.getType().equals(buildingInCity.getType()) || buildingInHand.getName().equals(buildingInCity.getName())){ // je compare le bâtiment dans sa main avec Les bâtiments dans sa cité s'il y a déjà même type ou même nom on va le retirer
                    checked = false;
                    break;
                }
            }
            if(!checked)unwantedBuildings.add(buildingInHand);
            checked = true;
        }
        if(unwantedBuildings.size()>0)return unwantedBuildings;
        else return null;
    }

    public Player magicianPassivePower(){
        return this.currentPlayer.getPlayerMaxBuildings(this.currentPlayer.getGamePlayers(), false);// pourquoi false car on veut chercher le joueur qui a le plus des cartes dans sa main et non pas dans sa cité
    }

    @Override
    public BaseBuildings laboratoryDecision() {
        return null;
    }

    public Player getTarget(){
        ArrayList<Player> playersList = this.getCurrentPlayer().getGamePlayers();
        Player strongest = playersList.get(0);
        for (Player p : playersList){
            int ratioMax = getScore(strongest);
            int ratio = getScore(p);
            if (ratio > ratioMax){
                strongest = p;
            }
        }
        this.target = strongest;
        return strongest;
    }

    private int getScore(Player p){
        final int i = 3 * p.getBuildingsBuilt().size() + 2 * p.getGold() + p.getBuildingsOfThisPlayer().size();
        return i;
    }

    public int getScenario(){
        Player target = getTarget();
        Player me = this.getCurrentPlayer();
        if (target.getBuildingsBuilt().size() > me.getBuildingsBuilt().size()){
            //Ville --
            if (target.getGold() > me.getGold()){
                //Gold --
                if (target.getBuildingsOfThisPlayer().size() > me.getBuildingsOfThisPlayer().size()){
                    //Ville --, Gold --, Cartes --
                    return 1;
                }else{
                    //Ville --, Gold --, Cartes ++
                    return 2;
                }
            }else{
                //Gold ++
                if (target.getBuildingsOfThisPlayer().size() > me.getBuildingsOfThisPlayer().size()){
                    //Ville --, Gold ++, Cartes --
                    return 3;
                }else{
                    //Ville --, Gold ++, Cartes ++
                    return 4;
                }
            }
        }else{
            //Ville ++
            if (target.getGold() > me.getGold()){
                //Gold --
                if (target.getBuildingsOfThisPlayer().size() > me.getBuildingsOfThisPlayer().size()){
                    //Ville ++, Gold --, Cartes --
                    return 5;
                }else{
                    //Ville ++, Gold --, Cartes ++
                    return 6;
                }
            }else{
                //Gold ++
                if (target.getBuildingsOfThisPlayer().size() > me.getBuildingsOfThisPlayer().size()){
                    //Ville ++, Gold ++, Cartes --
                    return 7;
                }else{
                    //Ville ++, Gold ++, Cartes ++
                    return 8;
                }
            }
        }
    }
}
