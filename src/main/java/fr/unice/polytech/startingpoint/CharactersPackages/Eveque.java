package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;
import java.util.ArrayList;

public class Eveque extends BaseCharacter {
    Eveque(){
        this.priority = CharacterEnum.EVEQUEPRIORITY;
        this.name = "Eveque";
    }

    /**
     * @author DOMINGUEZ Lucas
     * \brief pas de pouvoir passif car le fait que ses quartiers soit indestructibles est implementé dans le condottiere
     */
    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){}

    /**
     * @author DOMINGUEZ Lucas
     * \brief pouvoir de récupérer les golds de ses batiments quand il le veut
     */
    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){ //gagne 1 gold/batiments religieux
        if (activePlayer.getCharacter().getPriority() == priority) {
            int goldPre = activePlayer.getGold();
            activePlayer.addGold(activePlayer.getCity().getNbrReligiousBuildings()); // +1 gold par batiment religieux
            if(activePlayer.getEcoleDeMagie()!=null) activePlayer.addGold(1);
            if (DETAIL)System.out.println(activePlayer.getName() + " récupère " + (activePlayer.getGold()-goldPre) + " grace à son personnage.");
        }
    }
}
