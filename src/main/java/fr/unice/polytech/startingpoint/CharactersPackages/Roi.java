package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.Player;
import java.util.ArrayList;

class Roi extends BaseCharacter {
    Roi(){
        this.priority = CharacterEnum.ROIPRIORITY;
        this.name = "Roi";
    }

    /**
     * @author BABOURI Sofiane
     * @author DOMINGUEZ Lucas
     * \brief on donne la couronne au roi (en lui donnant la couronne),
     */
    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer) {
        if (activePlayer.getCharacter().getPriority() == priority) {
            for (Player player : players) {
                player.deCrown();
            }
            activePlayer.crown(); //On donne la couronne au nouveau roi
            if(DETAIL) System.out.println("Le roi se révèle, c'était le joueur "+ activePlayer.getName() +" et prend la couronne.");
        }
    }
    /**
     * @author DOMINGUEZ Lucas
     * \brief pouvoir de récupérer les golds de ses batiments quand il le veut
     */
    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){ //gagne 1 gold/batiments religieux
        if (activePlayer.getCharacter().getPriority() == priority) {
            int goldPre = activePlayer.getGold();
            activePlayer.addGold(activePlayer.getCity().getNbrNobleBuildings()); // +1 gold par batiment nobles
            if(activePlayer.getEcoleDeMagie()!=null) activePlayer.addGold(1);
            if (DETAIL)System.out.println(activePlayer.getName() + " récupère " + (activePlayer.getGold()-goldPre) + " grace à son personnage.");
        }
    }


}
