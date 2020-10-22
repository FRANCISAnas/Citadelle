package fr.unice.polytech.startingpoint.BuildingsPackage;

public enum MerveilleEnum {
    LABORATOIRE ("Laboratoire"),
    BIBLIOTHEQUE ("Bibliothèque"),
    MANUFACTURE ("Manufacture"),
    OBSERVATOIRE ("Observatoire"),
    ECOLEMAGIE ("Ecole de magie"),
    COURDESMIRACLES ("Cour des miracles"),
    CIMETIERE ("Cimetière"),
    DONJON ("Donjon")
    ;

    private String name;

    //Constructeur
    MerveilleEnum(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
