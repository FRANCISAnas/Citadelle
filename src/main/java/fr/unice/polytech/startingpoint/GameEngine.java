package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotHumain;
import fr.unice.polytech.startingpoint.BuildingsPackage.*;
import fr.unice.polytech.startingpoint.CharactersPackages.BaseCharacter;
import fr.unice.polytech.startingpoint.CharactersPackages.CharacterEnum;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

public class GameEngine {
    private boolean DETAIL=false;
    private final static int PLAYABLE_CHARACTER = 8;
    private static final int NB_OF_BUILDING_TO_END = 8;
    private static final int MAXRECUPERATEGOLD = 2; // max des pièces que les joueurs peuvent prendre et avec lequel ils commencent
    private static final int GOLDINITIAL = 2;
    private static final int NBCARTESINITIALE = 4;
    private static final int NBBUILDINIGSDRAWN  =2;
    private int turn;
    private ArrayList<Player> players;
    private Player activePlayer;
    private ArrayList<Player> winners;
    private int winnerscore;
    private boolean endingCondition = false;
    private CharacterEnum roi = CharacterEnum.Roi;
    private CharacterEnum voleur = CharacterEnum.Voleur;
    private CharacterEnum magicien = CharacterEnum.Magicien;
    private CharacterEnum assassin = CharacterEnum.Assassin;
    private CharacterEnum architecte = CharacterEnum.Architecte;
    private CharacterEnum condottiere = CharacterEnum.Condottiere;
    private CharacterEnum marchand = CharacterEnum.Marchand;
    private CharacterEnum eveque = CharacterEnum.Eveque;
    private ArrayList<BaseBuildings> cardsOfBuildings; //Le tas des bâtiments pour piocher
    private ArrayList<BaseCharacter> availableCharacters;//le tas des personnages
    private ArrayList<BaseCharacter> visibleFaceRemovedCharacter;
    private int characterToSteal;
    private static final Logger logger = Logger.getLogger("GameLogger");
    private static int nbrOfCharacterToRemove;

    GameEngine(ArrayList<Player> players) {
        this.players = players;
        this.activePlayer = players.get(0);
        this.availableCharacters = new ArrayList<>();
        this.visibleFaceRemovedCharacter = new ArrayList<>();
        this.winners = new ArrayList<>();
        turn = 1;
        ReadJSONFile jsonFile = new ReadJSONFile();
        cardsOfBuildings = jsonFile.create();
        Collections.shuffle(cardsOfBuildings);
        nbrOfCharacterToRemove =calculateNbrOfCharacterToRemove();
    }
    void setPlayers (ArrayList<Player> players) {this.players = players;}
    void setDetail(){
        this.DETAIL = true;
    }

    /**
     * @author FRANCIS Anas
     * @return ArrayList<Player> une liste des joueurs dans le jeu
     * \brief Je retourne une liste de joueurs
     * Created le 22/10/2019
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }
    Player getActivePlayer(){
        return this.activePlayer;
    }
    ArrayList<BaseBuildings> getCardsOfBuildings(){
        return this.cardsOfBuildings;
    }

    void setListOfGameCards(ArrayList<BaseBuildings> cardOfBuildings) {
        this.cardsOfBuildings = cardOfBuildings;
    }

    ArrayList<BaseCharacter> getAvailableCharacters (){
        return this.availableCharacters;
    }

    int calculateNbrOfCharacterToRemove() {
        if (players.size() < PLAYABLE_CHARACTER) {
            return PLAYABLE_CHARACTER - (players.size()+ 1);
        }
        return 0;
    }

    void remixCharacterDeck(){
        availableCharacters.clear();
        availableCharacters.add(roi.createCharacter());
        availableCharacters.add(voleur.createCharacter());
        availableCharacters.add(magicien.createCharacter());
        availableCharacters.add(assassin.createCharacter());
        availableCharacters.add(architecte.createCharacter());
        availableCharacters.add(condottiere.createCharacter());
        availableCharacters.add(marchand.createCharacter());
        availableCharacters.add(eveque.createCharacter());
        for(BaseCharacter character : availableCharacters){character.setDetail(DETAIL);}
        Collections.shuffle(availableCharacters);
        removeCharacters();
    }

    private void removeCharacters() {
        nbrOfCharacterToRemove=calculateNbrOfCharacterToRemove();
        if(nbrOfCharacterToRemove<=0)return;
        availableCharacters.remove(0);
        nbrOfCharacterToRemove -= 1;
        for(int i = 0; i < nbrOfCharacterToRemove; i++){
            if(availableCharacters.get(i).getPriority()==CharacterEnum.ROIPRIORITY){visibleFaceRemovedCharacter.add(availableCharacters.get(nbrOfCharacterToRemove));}
            else{visibleFaceRemovedCharacter.add(availableCharacters.get(i));}
        }
        for (BaseCharacter characterToRemove: visibleFaceRemovedCharacter) {
            availableCharacters.remove(characterToRemove);
        }
    }

    void newTurn(){
        remixCharacterDeck();
        characterDistribution(this.players);// on distribue les cartes
        for (Player player : players){player.dekill();}//on fait revivre le joueur mort.
        characterToSteal = -1;
        turn++;// on incrémente le tour
        if(DETAIL)System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////");
        if(DETAIL)System.out.println("                                            Tour des joueurs                                               ");
        if(DETAIL)System.out.println("\n                                 Tour " + turn + ", joueur couronné : " + searchCrown().getName() + "\n");
        if(DETAIL)System.out.println("//////////////////////////////////////////////");
        logger.info("\nTour " + turn + ", joueur couronné : " + searchCrown().getName() + "\n");

    }

    void gamePreparation(){
        if(DETAIL)System.out.println("Nouvelle partie de Citadelle à : " + players.size()+ " joueurs.");
        if(DETAIL)System.out.println("Les joueurs sont :" );
        for (Player player : players){
            if(DETAIL)System.out.println(player.getName());
            player.setGold(GOLDINITIAL);// les joueurs commencent avec 2 golds
            player.setBuildingsOfThisPlayer(player.drawNBuildings(cardsOfBuildings,NBCARTESINITIALE)); // et 4 cartes
            player.setGamePlayers(players);
        }
        if(DETAIL)System.out.println("\nPour avoir des informations sur vous tapez info");
        if(DETAIL)System.out.println("Pour avoir des informations sur les autres tapez info+");
        if(DETAIL)System.out.println("Pour avoir la liste des commandes tapez commandes");
        if(DETAIL)System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////");
        players.get(new Random().nextInt(players.size())).crown();// on donne la couronne au hasard.
        remixCharacterDeck();//on mélange les perso
        characterDistribution(this.players);// on distribue les personnage aux joueurs
    }

    void run(){
        int numerodappel = 0; // c'est la priorité du joueur qui va jouer
        gamePreparation();
        logger.info("\nTour " + turn + ", joueur couronné : " + searchCrown().getName() + "\n");
        while(!(endingCondition)){ // on boucle tant que personne n'a construit 8 bâtiments et on finit le tour
            numerodappel++;
            call(numerodappel);// on appelle le joueur dont c'est le tour
            if ((activePlayer.getCity().getNbrBuildings() >= NB_OF_BUILDING_TO_END || cardsOfBuildings.size() == 0) && !endingCondition){ //un joueur met fin a la partie
                endingCondition = true;
                activePlayer.finishCitadel(true);
            }
            else if (activePlayer.getCity().getNbrBuildings() >= NB_OF_BUILDING_TO_END && endingCondition){ activePlayer.finishCitadel(false); } //un joueur fini sa citadelle lors du dernier tour

            if(numerodappel == PLAYABLE_CHARACTER){ // si on a fait un tour on reset à 0
                newTurn();
                numerodappel = 0;
            }
        }
        setWinners();
        logWinnerMessage();
    }

    void setWinners () {
        winnerscore = 0 ;
        for (Player player : players) {
            player.setScore();
            if (player.getScore() > winnerscore){
                this.winnerscore = player.getScore();
            }
        }
        for (Player player : players) {
            if (player.getScore() == winnerscore){
                this.winners.add(player);
            }
        }
        if(DETAIL) {
            System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////");
            System.out.println("                                                   Fin de la partie\n");
            if (winners.size() < 2) {
                System.out.println("Le gagnant est " + winners.get(0).getName() + " avec " + winners.get(0).getScore() + " points");
            } else {
                System.out.println("Il y a une égalité");
            }
            System.out.println("Les scores des joueurs sont : ");
            for (Player player : players) {
                System.out.println(player.getName() + " : " + player.getScore());
            }
            System.out.println("                                                   Merci d'avoir joué ! ");
        }
    }


    /**
     * @param players les personnages que le joueur peut choisir
     * @author Julien MADRIAS
     * @author Timothée POULAIN
     * \brief Brief Cette méthode donne le choix aux joueurs parmis les cartes de personnages
     */

    void characterDistribution(ArrayList<Player> players){
        Player playercrownded = searchCrown();
        players.remove(playercrownded);
        players.add(0,playercrownded);// mis en premier pour qu'il choississe
        if(DETAIL)System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////");
        if(DETAIL)System.out.println("                                             Choix des roles                                               ");
        for (Player player: players) {
            BaseCharacter character = player.getBot().chooseCharacter(availableCharacters);// stockage de personnage qui a choisis.
            player.setCharacter(character);// on affecte le personnage choisi au joueur
            availableCharacters.remove(character);// on retire le personnage chois du tas des personnage.
            logger.info("Le joueur " + player.getName() + " choisi: " + character.getName() + "\n");
        }
    }
    /**
     * @author MADRIAS Julien
     * @author FRANCIS Anas
     * @author DOMINGUEZ Lucas
     * \brief Brief description Cette methode elle appelle le joueur dans le passage, pour effectuer ses actions.
     * @param numerodappel La valeur de priorité du personnage que l'on veut faire jouer.
     */
    private void call(int numerodappel) {
        for(Player player : this.players){
            if(player.getCharacter().getPriority() == numerodappel){// on cherche le bon joueur
                activePlayer = player;
                if(DETAIL)System.out.println("//////////////////////////////////////////////\n");
                if(DETAIL)System.out.println("Le " + activePlayer.getCharacter().getName() + " est appelé. C'est le joueur " + activePlayer.getName() + "\n");
                if(DETAIL&&activePlayer.getKilled()) System.out.println(activePlayer.getName()+ " a été assassiné et ne peut donc pas jouer.");
                if (!activePlayer.getKilled()) { //vérification qu'il n'est pas mort
                    logger.info("Le " + activePlayer.getCharacter().getName() + " est appelé. C'est le joueur " + activePlayer.getName() + "\n");
                    //vole du personnage :
                    if (characterToSteal==activePlayer.getCharacter().getPriority() && searchVoleur()!=null){
                        logger.info("Le joueur " + activePlayer.getName()  + " qui a le " + activePlayer.getCharacter().getName() +" se fait voler par " + searchVoleur().getName() + "\n");
                        ArrayList<Object> liste = new ArrayList<>(); liste.add(characterToSteal);
                        searchVoleur().getCharacter().activePower(liste, players, null, searchVoleur());}
                    //pouvoir passif du roi :
                    if (activePlayer.getCharacter().getPriority() == CharacterEnum.ROIPRIORITY){
                        logger.info("Le pouvoir passif du roi se déclenche pour le joueur " + activePlayer.getName()  + " qui a le " + activePlayer.getCharacter().getName() + "\n");
                        activePlayer.getCharacter().passivePower(null, players, null, activePlayer);}

                    if (activePlayer.getBot().drawOrGold()) {// Choix de piocher ou de récupérer du gold
                        logger.info("Le joueur " + activePlayer.getName() + " veut piocher.\n");
                        if(DETAIL)System.out.println("Le joueur " + activePlayer.getName() + " pioche.\n");
                        //passif de la bibliothèque
                        if(activePlayer.getBibliotheque()!=null){// si le joueur possède une bibliothèque dans sa cité il gardera les 2 cartes qui piochera
                            logger.info("Le pouvoir passif de la bilbiothèque se déclenche pour le joueur " + activePlayer.getName() + "\n");
                            activePlayer.getBibliotheque().useWonderAbility(activePlayer, cardsOfBuildings);
                        }
                        //passif de l'observatoire
                        else if (activePlayer.getObservatoire()!=null){
                            logger.info("Le pouvoir passif de l'observatoire se déclenche pour le joueur " + activePlayer.getName() + "\n");
                            activePlayer.getObservatoire().useWonderAbility(activePlayer, cardsOfBuildings);
                        }
                        else {// sinon il fera le choix entre les 2 cartes piochés
                            logger.info("Le joueur " + activePlayer.getName() + " ne pioche qu'une carte.\n");
                            pickBuilding(cardsOfBuildings);// le joueur pioche une carte
                        }
                    } else {
                        logger.info("Le joueur " + activePlayer.getName() + " prend de l'argent.\n");
                        if(DETAIL)System.out.println("Le joueur " + activePlayer.getName() + " récupère 2 pièces.\n");
                        activePlayer.addGold(MAXRECUPERATEGOLD);// Le joueur récupère 2 pièce!
                    }
                    //pouvoir passif de l'architecte :
                    if (activePlayer.getCharacter().getPriority() == CharacterEnum.ARCHITECTEPRIORITY) {
                        logger.info("Le pouvoir passif de l'architecte se déclenche pour le joueur " + activePlayer.getName() + " qui a le " + activePlayer.getCharacter().getName() + "\n");
                        activePlayer.getCharacter().passivePower(null, null, cardsOfBuildings, activePlayer);
                    }

                    //pouvoir passif du marchand :
                    if (activePlayer.getCharacter().getPriority() == CharacterEnum.MARCHANDPRIORITY) {
                        logger.info("Le pouvoir passif du marchand se déclenche pour le joueur " + activePlayer.getName() + " qui a le " + activePlayer.getCharacter().getName() + "\n");
                        activePlayer.getCharacter().passivePower(null, null, null, activePlayer);
                    }
                    turnManagement();
                    if(DETAIL)System.out.println("\nFin du tour, récapitulatif : \n");
                    if(DETAIL)System.out.println(activePlayer.getNbOfbuildingsInCity()+ " batiments construits : " + ((activePlayer.getNbOfbuildingsInCity()==0) ? "rien":activePlayer.getCity()));
                    if(DETAIL)System.out.println("Il a " + activePlayer.getGold()+ ((activePlayer.getGold()<=1) ? " pièce.":" pièces."));
                    if(DETAIL)System.out.println("Il a " + activePlayer.getBuildingsOfThisPlayer().size()+ ((activePlayer.getNbOfbuildingsInHisHand()<=1) ? " carte en main.":" cartes en main."));
                }
            }
        }
    }
    private void turnManagement() {
        ArrayList<String> whatTurn = new ArrayList<>();
        if(activePlayer.getBot() instanceof BotHumain){
            whatTurn = ((BotHumain) activePlayer.getBot()).globalTurn();
        }
        else {
            switch (activePlayer.getCharacter().getPriority()) {
                case CharacterEnum.ASSASSINPRIORITY:
                    whatTurn = activePlayer.getBot().assassinTurn();
                    break;
                case CharacterEnum.VOLEURPRIORITY:
                    whatTurn = activePlayer.getBot().voleurTurn();
                    break;
                case CharacterEnum.MAGICIENPRIORITY:
                    whatTurn = activePlayer.getBot().magicienTurn();
                    break;
                case CharacterEnum.ROIPRIORITY:
                    whatTurn = activePlayer.getBot().roiTurn();
                    break;
                case CharacterEnum.EVEQUEPRIORITY:
                    whatTurn = activePlayer.getBot().evequeTurn();
                    break;
                case CharacterEnum.MARCHANDPRIORITY:
                    whatTurn = activePlayer.getBot().marchandTurn();
                    break;
                case CharacterEnum.ARCHITECTEPRIORITY:
                    whatTurn = activePlayer.getBot().architecteTurn();
                    break;
                case CharacterEnum.CONDOTTIEREPRIORITY:
                    whatTurn = activePlayer.getBot().condottiereTurn();
                    break;
            }
        }
        boolean hasBuild = false, hasUseActivePower= false, hasUsePassivePower = false;
        //ces booleans servent à s'assurer que l'on ne puisse pas faire 2 fois la même action, et que les pouvoirs passifs ne sont pas choississable (sauf magicien)
        activePlayer.resetAllUsedWonder();
        if(whatTurn==null)return;
        for (String action : whatTurn) {//whatTurn est la liste des actions que va faire le bot durant son tour dans l'ordre
            switch (action) {
                case CharacterEnum.BUILD: {
                    hasBuild = build(hasBuild,hasUsePassivePower);
                    break;
                }
                case CharacterEnum.ACTIVEPOWER: {
                    hasUseActivePower = useActivePower(hasUseActivePower, hasUsePassivePower);
                    break;
                }
                case CharacterEnum.PASSIVEPOWER: {
                    hasUsePassivePower = usePassivePower(hasUsePassivePower,hasUseActivePower);
                    break;
                }
                case CharacterEnum.LABORATOIREABILITY:{
                    if (activePlayer.getLaboratoire() != null && !activePlayer.getLaboratoire().getUsed()) {
                        logger.info("Le joueur " + activePlayer.getName() + " utilise le pouvoir du : Laboratoire \n");
                        BaseBuildings buildingToDiscard = activePlayer.laboratoryDecision();
                        ArrayList<BaseBuildings> buildings = new ArrayList<>();buildings.add(buildingToDiscard);
                        activePlayer.getLaboratoire().useWonderAbility(activePlayer,buildings);
                    }
                    break;
                }

                case CharacterEnum.MANUFACTUREABILITY: {
                    if (activePlayer.getManufacture() != null && !activePlayer.getManufacture().getUsed()) {
                        logger.info("Le joueur " + activePlayer.getName() + " utilise le pouvoir de la : Manufacture \n");
                        activePlayer.getManufacture().useWonderAbility(activePlayer, cardsOfBuildings);
                    }
                    break;
                }
                default:
                    break;// il peut passer son tour.
            }
        }
    }


    ArrayList<Player> getWinners(){
        return winners;
    }


    private boolean build(boolean hasBuild, boolean hasUsePassivePower){
        if (!hasBuild) {
            if (activePlayer.getCharacter().getPriority() != CharacterEnum.CONDOTTIEREPRIORITY || !hasUsePassivePower) { // condottiere ne peut rien faire après son passif
                if(activePlayer.canBuildABuilding()) {
                    BaseBuildings buildingsToBuild = activePlayer.getBot().chooseBuildingsToBuild(activePlayer.getBuildingsOfThisPlayer()); // decider quel bâtiment construir parmis la liste de ses bâtiments
                    if(buildingsToBuild==null)return false;
                    logger.info("Le joueur " + activePlayer.getName() + " construit: " + buildingsToBuild.getName() + " car il a "+  activePlayer.getGold() +" >= " + buildingsToBuild.getPrice() + "golds.\n" );
                    activePlayer.useBuilding(buildingsToBuild); // construction de bâtiment
                    return true;
                }
            }
        }
        return false;
    }

    private boolean useActivePower(boolean hasUseActivePower, boolean hasUsePassivePower){
        if (!hasUseActivePower) {
            if (activePlayer.getCharacter().getPriority() != CharacterEnum.MAGICIENPRIORITY || !hasUsePassivePower){//le magicien ne peut pas utliser ses 2 pouvoirs
                if (activePlayer.getCharacter().getPriority() != CharacterEnum.CONDOTTIEREPRIORITY || !hasUsePassivePower) { // condottiere ne peut rien faire après son passif
                    logger.info("Le joueur " + activePlayer.getName() + " utilise le pouvoir actif du : " + activePlayer.getCharacter().getName() + "\n");
                    if (activePlayer.getCharacter().getPriority() == CharacterEnum.VOLEURPRIORITY) {
                        characterToSteal = (int) activePlayer.getBot().doActivePower().get(0);
                    } else {
                        activePlayer.getCharacter().activePower(activePlayer.getBot().doActivePower(), players, cardsOfBuildings, activePlayer);//active power est différente pour chaque perso
                        //et prend en paramètre une liste des choses à faire (car le bot sait quel pouvoirs il doit faire et renvoi la cible, etc...) et les choses utiles
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean usePassivePower(boolean hasUsePassivePower, boolean hasUseActivePower){
        if(!hasUsePassivePower && (activePlayer.getCharacter().getPriority()== CharacterEnum.MAGICIENPRIORITY ||activePlayer.getCharacter().getPriority()==CharacterEnum.CONDOTTIEREPRIORITY)) {
            if (activePlayer.getCharacter().getPriority() != CharacterEnum.MAGICIENPRIORITY || !hasUseActivePower) {//le magicien ne peut pas utliser ses 2 pouvoirs
                logger.info("Le joueur " + activePlayer.getName() + " utilise le pouvoir passif du : " + activePlayer.getCharacter().getName() + "\n");
                activePlayer.getCharacter().passivePower(activePlayer.getBot().doPassivePower(), players, cardsOfBuildings, activePlayer);
                return true;
            }
        }
        return false;
    }

    /**
     * @author FRANCIS Anas
     * @author DOMINGUEZ Lucas
     * \brief Cela tire 2 batiments, demande au joueur celui qu'il veut, lui met dans sa main et remet l'autre au fond du paquet.
     * @param cardsOfBuildings La pioche de bâtiments
     */

    void pickBuilding(ArrayList<BaseBuildings> cardsOfBuildings){ // ce joueur decide de piocher un bâtiment d'un paquet tas parmis 2.
        if(cardsOfBuildings.size() == 0)return;
        BaseBuildings buildingchosen;
        ArrayList<BaseBuildings> availableBuildings;
        availableBuildings = activePlayer.drawNBuildings(cardsOfBuildings,NBBUILDINIGSDRAWN); // pioche 2 batiments si dans le tas y'a plus de 2 cartes
        buildingchosen = activePlayer.getBot().chooseBuildings(availableBuildings);
        if(buildingchosen != null){
            for (BaseBuildings building : availableBuildings ){
                if (buildingchosen.equals(building)){ // si c'est celui choisi
                    activePlayer.getBuildingsOfThisPlayer().add(building); // ajout dans sa main
                }else {
                    activePlayer.putInDeckBuildings(building,cardsOfBuildings); // remise dans le deck des autres batiments.
                }
            }
        }
    }

    /**
     * @author
     * \brief Cette méthode permet de retourner le joueur qui possède actuellement la couronne.
     * @return Renvoi le joueur recherché
     */
    Player searchCrown(){
        for (Player player: players) {
            if (player.getCrowned()){return player;}
        }
        return null;
    }

    Player searchVoleur(){
        for (Player player: players) {
            if (player.getCharacter().getPriority()==CharacterEnum.VOLEURPRIORITY){return player;}
        }
        return null;
    }


    /**
     * @author
     * \brief Cette méthode permet de créer le message qui indiquera qui a gagné avec son score
     */
    private void logWinnerMessage() {
        StringBuilder sb = new StringBuilder();

        for (Player winner : winners) {
            String s = winner.getBot().getID() + " ";
            sb.append(s);
        }
        String ids = sb.toString();
        if (winners.size() == 1) {
            logger.info("\nLe joueur " + winners.get(0).getName() + " gagne avec " + winnerscore + " points\n\n");
        } else {
            logger.info("\nLes joueurs " + ids + "gagnent avec " + winnerscore + " points\n\n");
        }
    }

    /**
     * Permet d'obtenir les IA gagnantes, à égalités et perdantes à la fin d'une partie
     *
     * @return La liste des noms des IA gagnantes, à égalités et perdantes sous forme de liste
     */

    ArrayList<ArrayList<BaseBot>> getStats() {
        ArrayList<ArrayList<BaseBot>> statsArray = new ArrayList<>();

        ArrayList<BaseBot> winnersIA = new ArrayList<>();
        ArrayList<BaseBot> equalitiesIA = new ArrayList<>();
        ArrayList<BaseBot> loosersIA = new ArrayList<>();

        ArrayList<Player> winners = getWinners();
        boolean equality = winners.size() > 1;


        players.forEach(player -> {
            if (winners.contains(player)) {
                if (equality) equalitiesIA.add(player.getBot());
                else winnersIA.add(player.getBot());
            }
            else {
                loosersIA.add(player.getBot());
            }
        });

        statsArray.add(winnersIA);
        statsArray.add(equalitiesIA);
        statsArray.add(loosersIA);
        return statsArray;

    }

}
