package fr.unice.polytech.startingpoint.BuildingsPackage;

public enum BuildingsTypeEnum {
    MILITAIRE ("militaire"),
    NOBLE ("noble"),
    RELIGIEUX ("religious"),
    MARCHAND ("marchand"),
    MERVEILLE ("merveille")
    ;

    private String name;


    public static final int NBMAXOFDRAWNEDCARDSFORBIBLIOTHEQUE = 2;// Nombre des cartes qu'on doit piocher si on a la bibliothèque
    public static final int MANUFACTURETAXE = 3; // Le taxe qu'on doit payer à la banque pour piocher des cartes en plus
    public static final int NBOFMANUFACTUREDRAWNBUILDINGS = 3; // nb des cartes qu'on doit piocher si on a payer à la banque la taxe MANUFACTURTAXE si on a la carte de manufacture
    public static final int LABORATORYINCOMEMONEY = 1; // l'argent qu'on récupère si on défausse d'une carte si on a le laboratoire dans
    public static final int NBMAXOFDRAWNEDCARDSFOROBSERVATOIRE = 3; // nb des cartes qu'on doit piocher si on a l'observatoire

    //Constructeur
    BuildingsTypeEnum(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
