package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author FRANCIS Anas
 * \brief Cette classe permet de lire le fichier Json dans lequel nous avons mis toutes les cartes des bâtiments.
 */
public class ReadJSONFile {
    private ArrayList<Object> cardsOfBuildings_bis = new ArrayList<>();
    private ArrayList<BaseBuildings> cardsOfBuildings = new ArrayList<>();
    private final String DECK_FILENAME = "carte_des_batiments.json";

    /**
     * @author FRANCIS Anas
     * \brief Constructeur d'un objet de type ReadJSONFile.
     */
    public ReadJSONFile() {
        try {
            URL res = getClass().getClassLoader().getResource(DECK_FILENAME);
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(absolutePath));
            cardsOfBuildings_bis.add(jo.get("cardsOfGames"));
            cardsOfBuildings_bis = (ArrayList<Object>) cardsOfBuildings_bis.remove(0);
        } catch (IOException | ParseException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author FRANCIS Anas
     * @return ArrayList de bâtiments qui va être utiliser dans le GameEngin.
     */
    public ArrayList<BaseBuildings> create(){
        for(int i = 0;i<cardsOfBuildings_bis.size();i++){
            Map<String,String> thisMap = (Map) cardsOfBuildings_bis.get(i);
            String str =  thisMap.get("name");
            String price = thisMap.get("price");
            String type = thisMap.get("type");
            int price_bis = Integer.parseInt(price);
            String victoryPoints = thisMap.get("points");
            int victoryPoints_bis = Integer.parseInt(victoryPoints);
            if(!type.equals(BuildingsTypeEnum.MERVEILLE.toString()))cardsOfBuildings.add(new BaseBuildings(str,price_bis,victoryPoints_bis,type));
            else {
                switch(str){
                    case "Laboratoire" : cardsOfBuildings.add(new LaboratoireBuilding(str,price_bis,victoryPoints_bis,type));break;
                    case "Manufacture" : cardsOfBuildings.add(new ManufactureBuilding(str,price_bis,victoryPoints_bis,type));break;
                    case "Observatoire" : cardsOfBuildings.add(new ObservatoireBuilding(str,price_bis,victoryPoints_bis,type));break;
                    case "Bibliothèque" : cardsOfBuildings.add(new BibliothequeBuilding(str,price_bis,victoryPoints_bis,type));break;
                    case "Cimetière" : cardsOfBuildings.add(new CimetiereBuilding(str,price_bis,victoryPoints_bis,type));break;
                    default : cardsOfBuildings.add(new WonderBuilding(str,price_bis,victoryPoints_bis,type));break;
                }
            }
        }
        return cardsOfBuildings;
    }
}
