package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;
import java.util.ArrayList;

public class Marchand extends BaseCharacter {
    Marchand(){
        this.priority = CharacterEnum.MARCHANDPRIORITY;
        this.name = "Marchand";
    }

    /**
     * @author DOMINGUEZ Lucas
     * \brief pouvoir d'avoir 1 gold de plus au début de son tout
     */

    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority() == priority) {
            activePlayer.addGold(1);//+1 gold au début de son tour (indécidable)
            if (DETAIL)System.out.println(activePlayer.getName() + " gagne 1 pièce en plus grace au marchand.");
        }
    }

    /**
     * @author DOMINGUEZ Lucas
     * \brief pouvoir de récupérer les golds de ses batiments quand il le veut
     */

    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority() == priority) {
            int goldPre = activePlayer.getGold();
            activePlayer.addGold(activePlayer.getCity().getNbrCommercialBuildings());
            if(activePlayer.getEcoleDeMagie()!=null) activePlayer.addGold(1);// +1 gold par batiment marchand
            if (DETAIL)System.out.println(activePlayer.getName() + " récupère " + (activePlayer.getGold()-goldPre) + " grace à son personnage.");
        }
    }
}
