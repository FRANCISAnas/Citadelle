package fr.unice.polytech.startingpoint.CharactersPackages;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.MerveilleEnum;
import fr.unice.polytech.startingpoint.Player;
import java.util.ArrayList;

public class Condottiere extends BaseCharacter{

    Condottiere(){
        this.priority = CharacterEnum.CONDOTTIEREPRIORITY;
        this.name = "Condottiere";
    }

    /**
     * @author BABOURI Sofiane
     * @author DOMINGUEZ Lucas
     * Created 05/11/2019
     * \brief pouvoir de récupérer les golds de ses batiments quand il le veut
     */
    public void activePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority() == priority) {
            int goldPre = activePlayer.getGold();
            activePlayer.addGold(activePlayer.getCity().getNbrMilitaryBuildings()); // +1 gold par batiment militaires
            if(activePlayer.getEcoleDeMagie()!=null) activePlayer.addGold(1);
            if (DETAIL)System.out.println(activePlayer.getName() + " récupère " + (activePlayer.getGold()-goldPre) + " grace à son personnage.");
        }
    }

    /**
     * @author BABOURI Sofiane
     * @author DOMINGUEZ Lucas
     * Created 05/11/2019
     * \brief pouvoir de détruire un batiment dans une cité (à la fin de son tour s'il le veut)
     */
    public void passivePower(ArrayList<Object> autresParametres, ArrayList<Player> players, ArrayList<BaseBuildings> buildingDeck, Player activePlayer){
        if (activePlayer.getCharacter().getPriority() == priority) {
            if(autresParametres==null || autresParametres.size()!=2)return;
            Player victim = (Player) autresParametres.get(0); //Le joueur à qui on va détruire un bâtiment
            BaseBuildings buildingToDestroy = (BaseBuildings) autresParametres.get(1);//batiments à détruire
            if (victim.getCity().getNbrBuildings() < 1 || buildingToDestroy.getPrice() - 1 > activePlayer.getGold() || victim.getCity().getNbrBuildings() >= 8 || victim.getCharacter().getPriority() == CharacterEnum.EVEQUEPRIORITY||buildingToDestroy.getName().equals(MerveilleEnum.DONJON.toString())) {
                return;// si il n'a pas de batiments ou en a déjà 8 ou il ne peut pas le détruire ou c'est l'évèque
            }
            if(victim.getCimetiere()!=null && victim.getBot().cimetiereDecision(buildingToDestroy)) {
                ArrayList<BaseBuildings> buildingToSave = new ArrayList<>();
                buildingToSave.add(buildingToDestroy);
                victim.getCimetiere().useWonderAbility(victim, buildingToSave);
                return;
            }
            activePlayer.addGold(-(buildingToDestroy.getPrice()-1));
            victim.getCity().destroyBuilding(buildingToDestroy); //destruction du batiment
            if (DETAIL)System.out.println(activePlayer.getName() + " détruit " + buildingToDestroy.getName() +" du joueur " + victim.getName() + " pour " + (buildingToDestroy.getPrice()-1)+" pièces.");
        }
    }
}
