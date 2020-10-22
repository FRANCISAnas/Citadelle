package fr.unice.polytech.startingpoint.CharactersPackages;

public enum CharacterEnum {

    Assassin, Voleur, Magicien, Roi, Eveque, Marchand, Architecte, Condottiere;

    public static final int ASSASSINPRIORITY = 1;
    public static final int VOLEURPRIORITY = 2;
    public static final int MAGICIENPRIORITY = 3;
    public static final int ROIPRIORITY = 4;
    public static final int EVEQUEPRIORITY = 5;
    public static final int MARCHANDPRIORITY = 6;
    public static final int ARCHITECTEPRIORITY = 7;
    public static final int CONDOTTIEREPRIORITY = 8;

    public static final String ACTIVEPOWER = "ActivePower";
    public static final String PASSIVEPOWER = "PassivePower";
    public static final String LABORATOIREABILITY = "laboratoireAbility";
    public static final String MANUFACTUREABILITY = "manufactureAbility";
    public static final String BUILD = "Build";



    public BaseCharacter createCharacter(){
        switch (this){
            case Roi:
                return new Roi();
            case Voleur:
                return new Voleur();
            case Magicien:
                return new Magicien();
            case Assassin:
                return new Assassin();
            case Architecte:
                return new Architecte();
            case Condottiere:
                return new Condottiere();
            case Marchand:
                return new Marchand();
            case Eveque:
                return new Eveque();
        }
        return null;
    }
}
