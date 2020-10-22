package fr.unice.polytech.startingpoint.BotsPackage;

import fr.unice.polytech.startingpoint.BuildingsPackage.BaseBuildings;
import fr.unice.polytech.startingpoint.BuildingsPackage.BuildingsTypeEnum;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;
import fr.unice.polytech.startingpoint.Player;
import java.util.ArrayList;
import java.util.Scanner;

public class BotHumain extends BaseBot{
    private Scanner sc = new Scanner(System.in);
    private ArrayList<String> commandes =new ArrayList<>();

    public BotHumain(int ID){
        super(ID,"humain");
        commandes.add("info");commandes.add("info+");commandes.add("help");commandes.add("règles");commandes.add("stop");commandes.add("commandes");commandes.add("oscour");
    }

    public BaseBuildings chooseBuildingsToBuild(ArrayList<BaseBuildings> buildings){ //renvoie le bâtiment à construire de sa main (ici le premier constructible)
        String mot="";
        ArrayList<BaseBuildings> main = currentPlayer.getBuildingsOfThisPlayer();
        do {
            System.out.println("Quel batiment voulez vous constuire ? \n");
            System.out.println("Batiments dans la main : ");
            for (int i = 0; i < main.size(); i++) {
                System.out.println("\t" + main.get(i).getName() + " de type " + main.get(i).getType() + " coutant " + main.get(i).getPrice() + ". Tapez " + i);
            }
            System.out.println("\t" + "Ne rien construire : tapez -1");
            mot = info(sc.next());
            if(mot.equals("-1"))return null;
        }
        while(!isInt(mot,main.size()));
        int reponse = Integer.parseInt(mot);
        if(main.get(reponse).getPrice()>currentPlayer.getGold() || currentPlayer.getCity().isBuild(main.get(reponse))){System.out.println("Mauvais choix, tant pis pour vous... ");return null; }
        return main.get(reponse);
    }
    public BaseCharacter chooseCharacter(ArrayList<BaseCharacter> characters){// renvoie le personnage qui veut prendre
        String mot="";
        do {
            System.out.println("\nChoisissez un personnage : ");
            System.out.println("(" + (currentPlayer.getGamePlayers().size() + 1 - characters.size()) + " joueurs ont choisit avant vous)");
            for (int i = 0; i < characters.size(); i++) {
                System.out.println("\t" + characters.get(i).getName() + " tapez " + i);
            }
            mot = info(sc.next());
        }
        while(!isInt(mot,characters.size()));
        int reponse = Integer.parseInt(mot);
        return characters.get(reponse);
    }

    public BaseBuildings chooseBuildings(ArrayList<BaseBuildings> buildings){// retourner le premier batiment (celui qu'il veut piocher)
        String mot="";
        do {
            System.out.println("Choisissez quel batiment vous voulez piocher : ");
            for (int i = 0; i < buildings.size(); i++) {
                System.out.println(buildings.get(i).getName() + " de type " + buildings.get(i).getType() + " coutant " + buildings.get(i).getPrice() + ". Tapez " + i);
            }
            mot = info(sc.next());
        }
        while(!isInt(mot,buildings.size()));
        int reponse = Integer.parseInt(mot);
        return buildings.get(reponse);
    }

    public boolean drawOrGold(){// true = draw, false = gold
        String mot ="";
        do {
            System.out.println("Voulez-vous piocher 1 carte ou avoir 2 pièces en plus ?");
            System.out.println("Piocher : tapez 1");
            System.out.println("Pièces : tapez 0");
            mot = info(sc.next());
        }
        while(!isInt(mot,2));
        int reponse = Integer.parseInt(mot);
        return reponse == 1;
    }
    public ArrayList<String> globalTurn(){
        ArrayList<String> turnActions = new ArrayList<>();
        String reponse = "";
        int borne = 0;
        do {
            System.out.println("\nChoisissez vos actions lors de votre tour : ");
            System.out.println("Tapez les numéros dans l'ordre que vous voulez faire les actions correspondantes.");
            System.out.println("\tPour construire tapez 0");
            borne=1;
            switch (currentPlayer.getCharacter().getPriority()) {
                case CharacterEnum.ASSASSINPRIORITY: {
                    System.out.println("\tPour assassiner un personnage tapez 1");
                    borne++;
                    break;
                }
                case CharacterEnum.VOLEURPRIORITY: {
                    System.out.println("\tPour voler un personnage tapez 1");
                    borne++;
                    break;
                }
                case CharacterEnum.MAGICIENPRIORITY: {
                    System.out.println("\tChoisir une seule des deux actions suivantes : ");
                    System.out.println("\t\tPour échanger des cartes avec la pioche tapez 1");
                    System.out.println("\t\tPour échanger sa main avec un autre joueur tapez 2");
                    borne+=2;
                    break;
                }
                case CharacterEnum.ROIPRIORITY:{
                    System.out.println("\tPour récupérer ses taxes tapez 1");
                    borne++;
                    break;}
                case CharacterEnum.MARCHANDPRIORITY:{
                    System.out.println("\tPour récupérer ses taxes tapez 1");
                    borne++;
                    break;}
                case CharacterEnum.EVEQUEPRIORITY: {
                    System.out.println("\tPour récupérer ses taxes tapez 1");
                    borne++;
                    break;}
                case CharacterEnum.ARCHITECTEPRIORITY: {
                    System.out.println("\tPour construire 1 ou 2 batiments en plus tapez 1");
                    borne++;
                    break;
                }
                case CharacterEnum.CONDOTTIEREPRIORITY: {
                    borne+=2;
                    System.out.println("\tPour récupérer ses taxes tapez 1");
                    System.out.println("\tPour détruire un batiment d'un autre joueur tapez 2 (Doit être fait à la fin du tour)");
                    break;
                }
            }
            if (currentPlayer.getCity().getLaboratoire() != null) {
                System.out.println("\tVous avez le laboratoire, si voulez l'utiliser durant votre tour (défausser 1 carte et +1pièce) tapez 3 dans votre ordre.");
                borne++;
            }
            if (currentPlayer.getCity().getManufacture() != null) {
                System.out.println("\tVous avez la manufacture, si voulez l'utiliser durant votre tour (piocher 3 cartes contre 3 pièces) tapez 4 dans votre ordre.");
                borne++;
            }
            System.out.println("\tPour ne rien faire tapez -1.");
            reponse = info(sc.next());
            if(reponse.equals("-1"))return null;
        }
        while (!goodTurn(reponse,borne,borne));
        for(int i = 0;i<reponse.length();i++){
            if(reponse.charAt(i)=='0')turnActions.add(CharacterEnum.BUILD);
            else if(reponse.charAt(i) =='1')turnActions.add(CharacterEnum.ACTIVEPOWER);
            else if(reponse.charAt(i) =='2' && (currentPlayer.getCharacter().getPriority()==CharacterEnum.MAGICIENPRIORITY||currentPlayer.getCharacter().getPriority()==CharacterEnum.CONDOTTIEREPRIORITY))turnActions.add(CharacterEnum.PASSIVEPOWER);
            else if(reponse.charAt(i) =='3')turnActions.add(CharacterEnum.LABORATOIREABILITY);
            else if(reponse.charAt(i) =='4')turnActions.add(CharacterEnum.MANUFACTUREABILITY);
        }
        return turnActions;
    }

    public ArrayList<Object> doActivePower(){
        ArrayList<Object> autresParametres = new ArrayList<>();
        String mot;
        switch (currentPlayer.getCharacter().getPriority()){
            case CharacterEnum.ASSASSINPRIORITY:{
                do {
                    System.out.println("Choisissez un personnage à tuer : ");
                    System.out.println("Magicien = 0, Roi = 1, Eveque = 2, Marchand = 3, Architecte = 4, Condottiere = 5");
                    mot = info(sc.next());
                    if(mot.equals("-1"))return null;
                }
                while(!isInt(mot,6));
                int reponse = Integer.parseInt(mot);
                autresParametres.add(reponse+3);
                break;
            }
            case CharacterEnum.VOLEURPRIORITY:{
                do {
                    System.out.println("Choisissez un personnage à voler : ");
                    System.out.println("Voleur = 0, Magicien = 1, Roi = 2, Eveque = 3, Marchand = 4, Architecte = 5, Condottiere = 6");
                    mot = info(sc.next());
                    if(mot.equals("-1"))return null;
                }
                while(!isInt(mot,7));
                int reponse = Integer.parseInt(mot);
                autresParametres.add(reponse+2);
                break;
            }
            case CharacterEnum.MAGICIENPRIORITY:{
                String reponse="";
                ArrayList<BaseBuildings> choix = currentPlayer.getBuildingsOfThisPlayer();
                ArrayList<BaseBuildings> buildingsChosen = new ArrayList<>();
                do {
                    System.out.println("\nChoisissez les batiments que vous voulez échanger avec la pioche : \n");
                    for (int i = 0; i < choix.size(); i++) {
                        System.out.println("\t"+choix.get(i).getName() + " de type " + choix.get(i).getType() + " coutant " + choix.get(i).getPrice() + ". Tapez " + i);
                    }
                    reponse = info(sc.next());
                    if(reponse.equals("-1"))return null;
                }
                while(!goodTurn(reponse,choix.size(),choix.size()+1));
                for (int i = 0;i<reponse.length();i++){
                    buildingsChosen.add(choix.get(Integer.parseInt(String.valueOf(reponse.charAt(i)))));
                }
                autresParametres.add(buildingsChosen);
                break;
            }
            case CharacterEnum.ARCHITECTEPRIORITY:{
                String reponse="";
                ArrayList<BaseBuildings> main = currentPlayer.getBuildingsOfThisPlayer();
                do {
                    System.out.println("\nChoisissez les batiments que vous voulez construire en plus : ");
                    System.out.println("Batiments dans la main : ");
                    for (int i = 0; i < main.size(); i++) {
                        System.out.println("\t" + main.get(i).getName() + " de type " + main.get(i).getType() + " coutant " + main.get(i).getPrice() + ". Tapez " + i);
                    }
                    System.out.println("\tNe rien construire : tapez -1");
                    System.out.println("\nNombre de pièces : " + currentPlayer.getGold());
                    System.out.println("\nBatiments construits : " + ((currentPlayer.getNbOfbuildingsInCity() == 0) ? "aucun" : ""));
                    System.out.println(currentPlayer.getCity());
                    reponse = info(sc.next());
                    if(reponse.equals("-1"))return null;
                }
                while (!goodTurn(reponse,main.size(),2));
                for (int i = 0;i<reponse.length();i++){
                    autresParametres.add(main.get(Integer.parseInt(String.valueOf(reponse.charAt(i)))));
                }
                break;
            }
        }
        return autresParametres;
    }
    public ArrayList<Object> doPassivePower() {
        ArrayList<Object> autresParametres = new ArrayList<>();
        switch (currentPlayer.getCharacter().getPriority()) {
            case CharacterEnum.MAGICIENPRIORITY:{
                String mot;
                ArrayList<Player> choix = currentPlayer.getGamePlayers();
                do {
                    System.out.println("Choisissez le joueur avec qui vous voulez échanger votre main : ");
                    for (int i = 0; i < choix.size(); i++) {
                        System.out.println(choix.get(i).getName() + " qui a " + choix.get(i).getBuildingsOfThisPlayer().size() + " cartes en main." + " Tapez " + i);
                    }
                    mot = info(sc.next());
                    if(mot.equals("-1"))return null;
                }
                while(!isInt(mot,choix.size()));
                int reponse = Integer.parseInt(mot);
                autresParametres.add(choix.get(reponse));
                break;
            }
            case CharacterEnum.CONDOTTIEREPRIORITY:{
                String mot;
                ArrayList<Player> choix = currentPlayer.getGamePlayers();
                do {
                    System.out.println("\nChoisissez le joueur dont vous voulez détruire un batiment :\n");
                    for (int i = 0; i < choix.size(); i++) {
                        System.out.println("\t" + choix.get(i).getName() + " qui " + ((choix.get(i).getNbOfbuildingsInCity()==0) ? "n'a rien :" : "a " + choix.get(i).getCity().toString() )+ " Tapez " + i);
                    }
                    mot = info(sc.next());
                    if(mot.equals("-1"))return null;
                }
                while(!isInt(mot,choix.size()));
                int reponse = Integer.parseInt(mot);
                ArrayList<BaseBuildings> choix2 = choix.get(reponse).getBuildingsBuilt();
                do {
                    System.out.println("Choisissez le batiment que vous voulez détruire :\n");
                    for (int i = 0; i < choix2.size(); i++) {
                        System.out.println("\t"+choix2.get(i).getName() + " de type " + choix2.get(i).getType() + " coutant " + choix2.get(i).getPrice() + " tapez " + i);
                    }
                    mot = info(sc.next());
                    if(mot.equals("-1"))return null;
                }
                while(!isInt(mot,choix2.size()));
                reponse = Integer.parseInt(mot);
                autresParametres.add(choix2.get(reponse));
                break;
            }
        }
        return autresParametres;
    }
    private String articleEsthetique(String nom ){
        ArrayList<Character> voyelle = new ArrayList<>();
        voyelle.add('a');voyelle.add('e');voyelle.add('i');voyelle.add('o');voyelle.add('u');
        voyelle.add('A');voyelle.add('E');voyelle.add('I');voyelle.add('O');voyelle.add('U');
        if(voyelle.contains(nom.charAt(0))){
            return "l'" +nom;
        }
        else return "le " + nom;
    }
    public BaseBuildings laboratoryDecision(){
        String mot="";
        ArrayList<BaseBuildings> main = currentPlayer.getBuildingsOfThisPlayer();
        do {
            System.out.println("Choisissez un batiment à défausser et vous recevrez 1 pièce :");
            for (int i = 0; i < main.size(); i++) {
                System.out.println("\t" + main.get(i).getName() + " de type " + main.get(i).getType() + " coutant " + main.get(i).getPrice() + ". Tapez " + i);
            }
            mot = info(sc.next());
            if(mot.equals("-1"))return null;
        }
        while(!isInt(mot,main.size()));
        int reponse = Integer.parseInt(mot);
        return main.get(reponse);
    }
    @Override
    public boolean cimetiereDecision(BaseBuildings destroyedBuilding){
        String mot="";
        do {
            System.out.println("Vous avez le cimetière et le condottiere vous a détruit " + destroyedBuilding.getName() + ".\nVoulez-vous payer 1 pièce pour le reprendre dans votre main ?");
            System.out.println("\tTapez 1 pour oui");
            System.out.println("\tTaper 0 pour non");
            mot = info(sc.next());
        }
        while(!isInt(mot,2));
        int reponse = Integer.parseInt(mot);
        return reponse==1;
    }
    @Override
    public String courDesMiraclesDecision(){
        String mot="";
        do {
            System.out.println("C'est la fin de la partie et vous avez la cour des miracles, vous devez choisir un type qu'elle prendra.");
            System.out.println("Tapez " + BuildingsTypeEnum.MILITAIRE.toString() + " ou " + BuildingsTypeEnum.RELIGIEUX.toString() + " ou " + BuildingsTypeEnum.NOBLE.toString() + " ou " + BuildingsTypeEnum.MERVEILLE.toString() + " ou " + BuildingsTypeEnum.MARCHAND.toString());
            mot = info(sc.next());
        }
        while (!goodCourDesMiracles(mot));
        return mot;
    }

    private boolean goodCourDesMiracles(String mot){
        if(mot.equals(BuildingsTypeEnum.MILITAIRE.toString()) || mot.equals(BuildingsTypeEnum.RELIGIEUX.toString())||mot.equals(BuildingsTypeEnum.NOBLE.toString())||mot.equals(BuildingsTypeEnum.MERVEILLE.toString())||mot.equals(BuildingsTypeEnum.MARCHAND.toString())){
            return true;
        }
        else{
            System.out.println("Mauvais type, veuillez réessayer \n");
            return false;
        }
    }

    private String info(String info){
        if(info.equals("info")){
            System.out.println("************************************INFO Perso****************************************");
            if(currentPlayer.getCharacter()!=null) System.out.println("Vous avez " + articleEsthetique(currentPlayer.getCharacter().getName()));
            System.out.println("\nBatiments construits : "+((currentPlayer.getNbOfbuildingsInCity()==0) ?"aucun" : ""));
            System.out.println(currentPlayer.getCity());
            System.out.println("Batiments dans la main : "+((currentPlayer.getNbOfbuildingsInHisHand()==0) ?"aucun" : ""));
            for (BaseBuildings building : currentPlayer.getBuildingsOfThisPlayer()){
                System.out.println("\t"+building.getName()+ " de type "+building.getType()+" coutant "+ building.getPrice());
            }
            System.out.println("\nNombre de pièces : " + currentPlayer.getGold());
            System.out.println("*************************************************************************************");
        }
        else if(info.equals("info+")){
            System.out.println("************************************INFO Joueurs*****************************************");
            for(Player player : currentPlayer.getGamePlayers()){
                if(player.getCharacter()==null || currentPlayer.getCharacter()==null)System.out.println("\n++++++++++++++++++++++"+player.getName()+"+++++++++++++++++++++++");
                else System.out.println("\n++++++++++++++++++++++ "+player.getName()+((player.getCharacter().getPriority()<=currentPlayer.getCharacter().getPriority())? (" ayant "+ articleEsthetique(player.getCharacter().getName())) : "")+" +++++++++++++++++++++++");
                System.out.println("\n"+player.getNbOfbuildingsInCity()+" batiments construits : "+((player.getNbOfbuildingsInCity()==0) ?"aucun" : ""));
                System.out.println(player.getCity());
                System.out.println("Nb de batiments dans la main : " + player.getNbOfbuildingsInHisHand());
                System.out.println("\nNombre de pièces : " + player.getGold());
            }
            System.out.println("*************************************************************************************");
        }
        else if(info.equals("help")||info.equals("oscour")){
            System.out.println("Lisez les consignes, si vous ne voulez pas agir tapez -1\n");
        }
        else if (info.equals("commandes")){
            System.out.println(commandes);
        }
        else if(info.equals("stop")) {
            System.out.println("Confirmation tapez -1 pour fermer le jeu, autres choses pour continuer\n");
            String mot = sc.next();
            if (mot.equals("-1")) System.exit(666);
        }
        return info;

    }
    private boolean isInt(String s, int borne){
        if(commandes.contains(s))return false;
        if(s.length()!=1) {
            System.out.println("Mauvaise valeur, veuillez réessayer.\n");
            return false;
        }
        for(int i = 0; i<borne;i++){
            try {
                if (i == (Integer.parseInt(String.valueOf(s.charAt(0))))) return true;
            }
            catch (Exception e) {
                System.out.println("Ce n'est pas un entier, veuillez réessayer.\n");
                return false;
            }
        }
        System.out.println("Mauvaise valeur, veuillez réessayer.\n");
        return false;
    }

    private boolean goodTurn(String turn, int borne,int lenghtMax){
        if(currentPlayer.getCharacter().getPriority()==CharacterEnum.MAGICIENPRIORITY)lenghtMax--;
        if(commandes.contains(turn))return false;
        if(turn.length()==0||turn.length()>lenghtMax) {
            System.out.println("Mauvaise valeur, veuillez réessayer.\n");
            return false;
        }
        try {
            for (int j = 0; j < turn.length(); j++) {
                for (int i = 0; i < borne; i++) {
                    if (i == (Integer.parseInt(String.valueOf(turn.charAt(j))))) break;
                    if(i==borne-1){System.out.println("Tour incorrect, veuillez réessayer.\n");return false;}
                }
            }
        }
        catch (Exception e) {
            System.out.println("Ce n'est pas un entier, veuillez réessayer.\n");
            return false;
        }
        return true;
    }
    //inutile
    public boolean wannaBuild(){return false;}public ArrayList<String> architecteTurn() {return null;}public ArrayList<String> assassinTurn(){return null;}
    public ArrayList<String> condottiereTurn(){return null;}public ArrayList<String> evequeTurn(){return null;}public ArrayList<String> magicienTurn(){return null;}
    public ArrayList<String> marchandTurn(){return null;}public ArrayList<String> roiTurn(){return null;}public ArrayList<String> voleurTurn(){return null;}
}
