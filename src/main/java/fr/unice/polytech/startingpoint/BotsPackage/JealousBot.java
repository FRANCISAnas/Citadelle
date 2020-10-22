package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;

/**
 * Focus le rôle "Voleur", "Assassin" ou "Condottière", et essaie d'atteindre le joueur avec le plus de points posés
 * @author Timothée Poulain
 */

class JealousBot extends BaseBot {
    JealousBot(int ID) {
        super(ID, "JealousBot");
    }

    /**
     * Il choisit le voleur, sinon le condottiere, sinon l'assassin et sinon le premier rôle qu'il trouve.
     * Il va avant tout essayer de voler un deck adverse avec le magicien s'il n'a plus beaucoup de cartes.
     * @param characters les personnages que le joueur peut choisir
     * @return Le personnage choisi
     */
    public BaseCharacter chooseCharacter(ArrayList<BaseCharacter> characters) {
        ArrayList<Integer> rolePreference = new ArrayList<>();

        for (Player player : currentPlayer.getGamePlayers()){
            if(!player.getName().equals(currentPlayer.getName())) {
                if (player.getBuildingsBuilt().size() > 5) { // Si un des joueurs a plus de 5 batiments on prend le condottiere en priorité
                    rolePreference.add(CharacterEnum.CONDOTTIEREPRIORITY);
                    break;
                }
                for (BaseBuildings building : player.getBuildingsBuilt()) {
                    if (building.getPrice() == 1) {//Si un joueur a un batiment coutant 1 on prend le condottiere en priorité
                        rolePreference.add(CharacterEnum.CONDOTTIEREPRIORITY);
                        break;
                    }
                }
            }
        }
        //Si on a qu'un bâtiment ou moins alors on prend le magicien en priorité
        if(currentPlayer.getBuildingsOfThisPlayer().size()<=1) rolePreference.add(CharacterEnum.MAGICIENPRIORITY);

        int sumGlobalOfGold =0;
        for (Player player : currentPlayer.getGamePlayers()){
            if(!player.getName().equals(currentPlayer.getName())){//Si les joueurs ont beaucoup d'or en global on prend le voleur pour en voler
                sumGlobalOfGold+=player.getGold();
            }
        }
        if (sumGlobalOfGold >= currentPlayer.getGold()){rolePreference.add(CharacterEnum.VOLEURPRIORITY);}

        rolePreference.add(CharacterEnum.ASSASSINPRIORITY);//sinon on prend l'assassin
        rolePreference.add(CharacterEnum.CONDOTTIEREPRIORITY);//sinon c'est dans un ordre
        rolePreference.add(CharacterEnum.VOLEURPRIORITY);rolePreference.add(CharacterEnum.MAGICIENPRIORITY);
        rolePreference.add(CharacterEnum.ARCHITECTEPRIORITY);rolePreference.add(CharacterEnum.ROIPRIORITY);
        rolePreference.add(CharacterEnum.MARCHANDPRIORITY);rolePreference.add(CharacterEnum.EVEQUEPRIORITY);


        for(int rolePrefered : rolePreference) {
            for (BaseCharacter availableRole : characters) {
                if(rolePrefered == availableRole.getPriority()) return availableRole;
            }
        }
        return characters.get(0);
    }

    /**
     * @param buildings les batiments que le joueur peut choisir
     * @return Le batiment choisi
     */
    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings) { //il pioche celui qui a le cout le plus fort
        if (buildings==null || buildings.size()==0)return null;
        BaseBuildings buildingChosen = highestCostBuilding(buildings, true);
        if(buildingChosen==null) return highestCostBuilding(buildings,false);
        else return buildingChosen;
    }

    /**
     * @author POULIN Timothée
     * @author FRANCIS Anas
     * @param buildings Les batiments que le joueur peut construire
     * @return Le batiment à construire
     */
    public BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings) {//il construit celui qui a le cout le plus élevé
        return lowestCostBuilding(buildings);// créer le bâtiment le moins chère construisible (il y en a forcément un à ce stade)
    }

    /**
     *
     * @return true si il peut construire, sinon false
     */
    public boolean wannaBuild(){//Il veut construire s'il le peut
        return this.currentPlayer.canBuildABuilding();
    }

    /**
     * Le comportement du JealousBot est de s'enrichir pour se sentir supérieur à ses adversaires
     * @return false car il prend toujours l'argent
     */
    public boolean drawOrGold(){
        if(currentPlayer.getCharacter().getPriority()!=CharacterEnum.MAGICIENPRIORITY){
            return currentPlayer.getBuildingsOfThisPlayer().size()==0 ||currentPlayer.imposableBuildings();
        }
        return false;
    }

    public ArrayList<String> architecteTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        turnActions.add(CharacterEnum.LABORATOIREABILITY);
        turnActions.add(CharacterEnum.BUILD);
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
        turnActions.add(CharacterEnum.PASSIVEPOWER);
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
        turnActions.add(CharacterEnum.PASSIVEPOWER);
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
            case CharacterEnum.ASSASSINPRIORITY : autresParametres.add(CharacterEnum.ARCHITECTEPRIORITY);break;//ne sait pas quoi faire
            case CharacterEnum.VOLEURPRIORITY : autresParametres.add(CharacterEnum.ARCHITECTEPRIORITY); return autresParametres;//ne sait pas quoi faire
        }
        return null;
    }
    public ArrayList<Object> doPassivePower(){// il n'y a que le magicien et le condottiere qui ont un passif qui demande une reflexion du bot.
        ArrayList<Object> autresParametres = new ArrayList<>();
        if(currentPlayer.getCharacter().getPriority()== CharacterEnum.MAGICIENPRIORITY){
            autresParametres.add(magicienPassivePower());
            return autresParametres;
        }
        else if(currentPlayer.getCharacter().getPriority()== CharacterEnum.CONDOTTIEREPRIORITY){
            return condottierePassivePower();
        }
        else return null;
    }


    private Player magicienPassivePower(){//renvoi le joueur dont il veut échanger ses cartes
        Player victim = currentPlayer.getGamePlayers().get(0);
        for(Player player : currentPlayer.getGamePlayers()){
            if (player.getBuildingsOfThisPlayer().size()>= victim.getBuildingsOfThisPlayer().size() && !player.getName().equals(currentPlayer.getName())){
                victim=player;
            }
        }
        return victim;

    }
    private ArrayList<Object> condottierePassivePower(){
        ArrayList<Object> playerandHisBuilding = new ArrayList<>();
        Player maxPlayer;
        maxPlayer = this.currentPlayer.getPlayerMaxBuildings(this.currentPlayer.getGamePlayers(),true);
        if(maxPlayer.getBuildingsBuilt().size() == 0)return null;
        BaseBuildings buildingChosen = maxPlayer.getBuildingsBuilt().get(0);
        for (BaseBuildings building_bis: maxPlayer.getBuildingsBuilt()) {
            if(building_bis.getPrice()<buildingChosen.getPrice())buildingChosen = building_bis;
        }
        playerandHisBuilding.add(maxPlayer);
        if(this.currentPlayer.getGold()>=buildingChosen.getPrice())playerandHisBuilding.add(buildingChosen);
        return playerandHisBuilding;
    }

    public BaseBuildings laboratoryDecision(){
        if (this.currentPlayer.getGold() >= 1 && this.currentPlayer.getNbOfbuildingsInHisHand()>0){
            return highestCostBuilding(this.currentPlayer.getBuildingsOfThisPlayer(),false);
        }
        return null;
    }
}
