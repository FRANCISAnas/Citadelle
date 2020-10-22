package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.BotsPackage.BaseBot;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.*;
 class GameLoader {
    private ArrayList<Player> players;
    ArrayList<BaseBot> bots;
    private int nbrOfGame;
    private int[][] stats;
    private int[] scores;
    private int gameNumber;
    private int nbrOfPlayer;
    private boolean showStats;
    private boolean humanPresent;


    GameLoader(BotsType[] botsTypes, int nbrOfGame){
        if(nbrOfGame >= 1){initializeLogger();}
        bots = getBotfromTypes(botsTypes);
        this.nbrOfGame = nbrOfGame;
        for(BotsType botsType : botsTypes){
            if (botsType == BotsType.BotHumain) {
                humanPresent = true;
                break;
            }
        }
        this.nbrOfPlayer = botsTypes.length;
        stats = new int[nbrOfPlayer][3];
        scores = new int[nbrOfPlayer];
        Arrays.fill(scores, 0);
        this.gameNumber = 0;
    }

    void run(){
        GameEngine currentGame;
        for (int i=0; i<nbrOfGame; i++){
            currentGame = load();
            if(nbrOfGame > 1){showStats = true;}
            if(nbrOfGame==1 && humanPresent)currentGame.setDetail();
            currentGame.run();
            gameNumber++;
            if(showStats){
                updateStats(currentGame);
                updateScores(currentGame);
            }
        }
        if(showStats){computeStats();}
    }

    public ArrayList<Player> createPlayers(ArrayList<BaseBot> bots) {

        Collections.shuffle(bots);
        ArrayList<Player> players = new ArrayList<>();
        for (BaseBot bot : bots) {
            Player newPlayer = new Player(bot, bots.indexOf(bot));
            players.add(newPlayer);
            bot.updateCurrentPlayer(newPlayer);
        }
        return players;
    }

    public ArrayList<BaseBot> getBotfromTypes(BotsType[] botsTypes) {
        ArrayList<BaseBot> bots = new ArrayList<>();
        for (int i = 0; i < botsTypes.length; i++) {
            bots.add(botsTypes[i].createBot(i));
        }
        return bots;
    }

    GameEngine load() {
        this.players = createPlayers(bots);
        return new GameEngine(this.players);
    }

    /**
     * Met à jour les données sur les parties
     *
     * @param game la nouvelle partie à ajouter aux statistiques
     */

    private void updateStats(GameEngine game) {
        ArrayList<ArrayList<BaseBot>> gameStats = game.getStats();
        for (int i = 0; i < gameStats.size(); i++) {
            for (BaseBot bot : gameStats.get(i)) {
                stats[bot.getID()][i] += 1;
            }
        }
    }

    private void updateScores(GameEngine game) {
        for (Player player : game.getPlayers()) {
            scores[player.getBot().getID()] += player.getScore();
        }
    }

    /**
     * Affiche les statistiques
     */

    String computeStats() {
        int maxWins = 0;
        String maxWinner = "Null";
        String statistiques;
        statistiques = "Stats sur " + gameNumber + " parties : \n";
        if(this.nbrOfGame == 1){
            return "";}
        for (BaseBot bot : bots) {
            int IDcurrentIA = bot.getID();
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            statistiques += "\n" + bot.getName() + " : " + f.format(stats[IDcurrentIA][0] / (float) gameNumber * 100) + " % de victoires (totales), " + f.format(stats[IDcurrentIA][1] / (float) gameNumber * 100) + " % d'égalités, " + f.format(stats[IDcurrentIA][2] / (float) gameNumber * 100) + " % de défaites, avec un score moyen de : " + scores[bot.getID()] / gameNumber + " points.";
            if (stats[IDcurrentIA][0] > maxWins) {
                maxWins = stats[IDcurrentIA][0];
                maxWinner = bot.getName();
            }
        }
        if (maxWinner.equals("Null")) {
            statistiques += "\n\nIl n'y pas eu de gagnants sur les parties simulées";
        } else {
            statistiques += "\n\nLe bot " + maxWinner + " possède le meilleur taux de victoire : " + ((float) maxWins / gameNumber *100) +" %.";
        }
        return statistiques;
    }

    private static void initializeLogger(){
        Logger globalLogger = Logger.getLogger("");
        Handler[] handlers = globalLogger.getHandlers();
        for (Handler handler : handlers) {
            globalLogger.removeHandler(handler);
        }
    }

}
