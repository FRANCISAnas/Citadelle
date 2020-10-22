package fr.unice.polytech.startingpoint.BuildingsPackage;

public class BaseBuildings {
    private String name;
    private int victoryPoints;
    private int price;
    private String type;

    public BaseBuildings(String str, int price, int victoryPoints, String type){
        this.name=str;
        this.price=price;
        this.victoryPoints = victoryPoints;
        this.type = type;
    }

    public int getVictoryPoints(){return this.victoryPoints;}
    public int getPrice(){return this.price;}
    public String getName(){return this.name;}
    public String getType(){return this.type;}
    public void setType(String type){this.type = type;}
}

