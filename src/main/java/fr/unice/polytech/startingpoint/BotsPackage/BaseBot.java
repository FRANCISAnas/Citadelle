package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;

public abstract class BaseBot {
    Player currentPlayer;

    private int ID;
    private String name;

    BaseBot(int ID, String name) {
        currentPlayer = null;
        this.ID = ID;
        this.name = "(" + name + " " + ID + ")";
        if(name.equals("humain")){
            System.out.println("Veuillez choisir un nom :");
            Scanner sc = new Scanner(System.in);
            this.name= sc.nextLine();
        }
    }

    public void updateCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @author FRANCIS Anas
     * \brief Retourner vrai si sa main n'est pas vide (car c'est le BasicBot)
     */
    public abstract boolean wannaBuild();

    /**
     * Permet à un joueur de choisir un personnage à utiliser
     *
     * @param characters les personnages que le joueur peut choisir
     * @return celui qu'il désire utiliser
     */

    public abstract BaseCharacter chooseCharacter(ArrayList<BaseCharacter> characters);


    /**
     * @author FRANCIS Anas
     * \brief Retourner vrai s'il veut piocher faux pour récupérer des Gold!
     */
    public abstract boolean drawOrGold();
    /**
     * Permet à un joueur de choisir un bâtiment à construire
     *
     * @param buildings les batiments que le joueur peut construire
     * @return celui qu'il désire construire
     */

    public abstract BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings);

    public abstract BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings);

    BaseBuildings lowestCostBuilding(ArrayList<BaseBuildings> buildings) {//renvoie le batiment au cout le plus faible (forcément construisible car il peut en construire au moins 1)
        if (buildings.size() == 0) return null;
        int minPrice = 0;//indice du batiment avec le cout le moins élévé
        for (int i = 0; i < buildings.size(); i++) {
            if (buildings.get(i).getPrice() <= buildings.get(minPrice).getPrice()) {
                minPrice = i;
            }
        }
        return buildings.get(minPrice);
    }

    BaseBuildings highestCostBuilding(ArrayList<BaseBuildings> buildings,boolean constructible) {//renvoi le batiment le plus cher construisible si boolean = true et le plus cher tout cour sinon.
        BaseBuildings mostExpensiveBuilding = null;
        if (buildings==null || buildings.size()==0) return null;
        if(constructible) {
            for (BaseBuildings building : buildings) {
                if (building.getPrice() <= currentPlayer.getGold()) {
                    mostExpensiveBuilding = building;
                    break;//on prend le premier construisible pour initialiser
                }
            }
        }
        else mostExpensiveBuilding=buildings.get(0);
        for (BaseBuildings building : buildings) {
            if (mostExpensiveBuilding != null && building.getPrice() > mostExpensiveBuilding.getPrice() && building.getPrice() <= currentPlayer.getGold()&& constructible) {
                mostExpensiveBuilding = building;//on regarde s'il y a en qui est plus cher tout en étant construisible
            }
            else if(mostExpensiveBuilding!=null && !constructible && building.getPrice() > mostExpensiveBuilding.getPrice()){
                mostExpensiveBuilding = building;
            }
        }
        return mostExpensiveBuilding;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
    public abstract ArrayList<String> architecteTurn();
    public abstract ArrayList<String> assassinTurn();
    public abstract ArrayList<String> condottiereTurn();
    public abstract ArrayList<String> evequeTurn();
    public abstract ArrayList<String> magicienTurn();
    public abstract ArrayList<String> marchandTurn();
    public abstract ArrayList<String> roiTurn();
    public abstract ArrayList<String> voleurTurn();

    public abstract ArrayList<Object> doActivePower();
    public abstract ArrayList<Object> doPassivePower();

    public abstract BaseBuildings laboratoryDecision();

    /**
     * @author FRANCIS Anas
     * @return true si le bâtiment détruit n'est pas déjà dans sa main false sinon
     * created 01/12/2019
     * @param destroyedBuilding le bâtiment qui va essyer de reprendre s'il pouurais
     */
    public boolean cimetiereDecision(BaseBuildings destroyedBuilding){
        if(this.currentPlayer.getGold() == 0)return false;
        for (BaseBuildings buildingInHand:this.currentPlayer.getBuildingsOfThisPlayer()) {
            if(buildingInHand.getName().equals(destroyedBuilding.getName()) )return false;// si le bâtiment existe déjà dans sa cité il est inutile de le remettre dans sa main
        }
        return true;
    }


    /**
     * \brief La décision étant la même pour tous les bots, la méthode de choix de la couleur carte est ici
     * tableau, dans l'ordre : bleu, jaune, vert, rouge
     * @return couleur choisie
     */
    public String courDesMiraclesDecision(){
        ArrayList<String> typeSorted = numberTypeSorted(true);
        Collections.reverse(typeSorted);
        return (typeSorted.size() != 0) ? typeSorted.get(0) : BuildingsTypeEnum.MERVEILLE.toString();
    }


    ArrayList<String> numberTypeSorted (boolean allType) {//renvoi liste des types rangés par nb de batiment décroissant, allType == true -> merveille et même si 0 batiments
        TreeMap sortedTypeMap = new TreeMap();
        if(!allType) {
            if(currentPlayer.getCity().getNbrCommercialBuildings()!=0) {
                sortedTypeMap.put(currentPlayer.getCity().getNbrCommercialBuildings()+"Ma", BuildingsTypeEnum.MARCHAND);
            }
            if(currentPlayer.getCity().getNbrMilitaryBuildings()!=0) {
                sortedTypeMap.put(currentPlayer.getCity().getNbrMilitaryBuildings()+"Mi", BuildingsTypeEnum.MILITAIRE);
            }
            if(currentPlayer.getCity().getNbrNobleBuildings()!=0) {
                sortedTypeMap.put(currentPlayer.getCity().getNbrNobleBuildings()+"No", BuildingsTypeEnum.NOBLE);
            }
            if(currentPlayer.getCity().getNbrReligiousBuildings()!=0) {
                sortedTypeMap.put(currentPlayer.getCity().getNbrReligiousBuildings()+"Re", BuildingsTypeEnum.RELIGIEUX);//ajout du nombre de batiment de chaque type trié par ordre croissant (treeMap)
            }
        }
        else {
            sortedTypeMap.put(currentPlayer.getCity().getNbrCommercialBuildings() + "Ma", BuildingsTypeEnum.MARCHAND);
            sortedTypeMap.put(currentPlayer.getCity().getNbrMilitaryBuildings() + "Mi", BuildingsTypeEnum.MILITAIRE);
            sortedTypeMap.put(currentPlayer.getCity().getNbrNobleBuildings() + "No", BuildingsTypeEnum.NOBLE);
            sortedTypeMap.put(currentPlayer.getCity().getNbrReligiousBuildings() + "Re", BuildingsTypeEnum.RELIGIEUX);//ajout du nombre de batiment de chaque type trié par ordre croissant (treeMap)
        }
        ArrayList<String> liste = new ArrayList(sortedTypeMap.keySet());//liste des nombres de batiments par type triée croissant
        ArrayList<String> typesSorted = new ArrayList<>();//liste des types triés croissant
        for(String i : liste ) {
            typesSorted.add(sortedTypeMap.get(i).toString());
        }
        Collections.reverse(typesSorted);
        return typesSorted;
        }

}
