package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import java.util.ArrayList;

public class City {
    private ArrayList<BaseBuildings> buildings;
    private int nbrBuildings;
    private int militaryBuildings;
    private int commercialBuildings;
    private int religiousBuildings;
    private int nobleBuildings;
    private int wonderBuildings;

    City(){
        buildings = new ArrayList<>();
        this.nbrBuildings = 0;
        this.commercialBuildings = 0;
        this.militaryBuildings = 0;
        this.nobleBuildings = 0;
        this.religiousBuildings = 0;
        this.wonderBuildings = 0;
    }

    public ArrayList<BaseBuildings> getBuildings(){
        return this.buildings;
    }

    public int getNbrBuildings(){return this.nbrBuildings;}

    public int getNbrCommercialBuildings(){return this.commercialBuildings;}
    public int getNbrMilitaryBuildings(){return this.militaryBuildings;}
    public int getNbrNobleBuildings(){return this.nobleBuildings;}
    public int getNbrReligiousBuildings(){return this.religiousBuildings;}
    int getNbrWonderBuildings(){return this.wonderBuildings;}


    /**
     * \brief Méthode qui informe si une ville a le droit d'avoir un bonus
     * \brief qui correspond à la possession d'au moins un bâtiment de chaque type construit
     */
    boolean bonusDifferentBuildings(){
        return (militaryBuildings > 0) && (commercialBuildings >0) && (religiousBuildings >0) && (nobleBuildings >0) && (wonderBuildings> 0);
    }

    void setBuildings(ArrayList<BaseBuildings> newBuildings){this.buildings  = newBuildings;}

    /**
     * \brief Méthode qui tient le compte du nombre de bâtiments de chaque type dans la ville
     * \brief En l'ajoutant dans la liste de la ville et en incrémentant son type pour les futurs bonus
     * @param buildingToBuild le bâtiment qui va être ajouté
     */
    void build(BaseBuildings buildingToBuild){
        if(!isBuild(buildingToBuild)) {
            buildings.add(buildingToBuild);
            nbrBuildings++;
            if (buildingToBuild.getType().equals(BuildingsTypeEnum.RELIGIEUX.toString())) religiousBuildings++;
            if (buildingToBuild.getType().equals(BuildingsTypeEnum.NOBLE.toString())) nobleBuildings++;
            if (buildingToBuild.getType().equals(BuildingsTypeEnum.MARCHAND.toString())) commercialBuildings++;
            if (buildingToBuild.getType().equals(BuildingsTypeEnum.MILITAIRE.toString())) militaryBuildings++;
            if (buildingToBuild.getType().equals(BuildingsTypeEnum.MERVEILLE.toString())) wonderBuildings++;
        }
    }

    public void destroyBuilding(BaseBuildings building) {
        if(buildings.remove(building)){
            nbrBuildings--;
            if (building.getType().equals(BuildingsTypeEnum.RELIGIEUX.toString())) religiousBuildings--;
            if (building.getType().equals(BuildingsTypeEnum.NOBLE.toString())) nobleBuildings--;
            if (building.getType().equals(BuildingsTypeEnum.MARCHAND.toString())) commercialBuildings--;
            if (building.getType().equals(BuildingsTypeEnum.MILITAIRE.toString())) militaryBuildings--;
            if (building.getType().equals(BuildingsTypeEnum.MERVEILLE.toString())) wonderBuildings--;
        }
    }

    /**
     *
     * @param building batiment dont veut savoir s'il y en a déjà un de construit
     * @return renvoi true si le nom du batiment est déjà présent dans la liste des batiments constuits
     */
    public boolean isBuild(BaseBuildings building){
        for (BaseBuildings buildingsBuilded: buildings) {
            if ((building.getName()).equals(buildingsBuilded.getName())) return true;
        }
        return false;
    }

    BibliothequeBuilding getBibliotheque() {
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.BIBLIOTHEQUE.toString())) {return (BibliothequeBuilding) building;}
        }
        return null;
    }

    public ManufactureBuilding getManufacture() {
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.MANUFACTURE.toString())) {return (ManufactureBuilding) building;}
        }
        return null;
    }

     public WonderBuilding getEcoleDeMagie() {
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.ECOLEMAGIE.toString())) {return (WonderBuilding) building;}
        }
        return null;
    }

    public LaboratoireBuilding getLaboratoire() {
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.LABORATOIRE.toString())) {return (LaboratoireBuilding) building;}
        }
        return null;
    }

    BaseBuildings getCourDesMiracles() {
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.COURDESMIRACLES.toString())) {return building;}
        }
        return null;
    }

    ObservatoireBuilding getObservatoire() {
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.OBSERVATOIRE.toString())) {return (ObservatoireBuilding) building;}
        }
        return null;
    }

    WonderBuilding getCimetiere(){
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.CIMETIERE.toString())) {return (WonderBuilding) building;}
        }
        return null;
    }

    WonderBuilding getDonjon(){
        for (BaseBuildings building: getBuildings()) {
            if(building.getName().equals(MerveilleEnum.DONJON.toString())) {return (WonderBuilding) building;}
        }
        return null;
    }
    @Override
    public String toString(){
        StringBuilder liste = new StringBuilder();
        for(BaseBuildings building : buildings){
            liste.append(building.getName()).append(" coutant ").append(building.getPrice()).append(" de type ").append(building.getType()).append(", ");
        }
        return liste.toString();
    }
}
