package fr.unice.polytech.startingpoint;
import fr.unice.polytech.startingpoint.BotsPackage.BotsType;
import java.util.Scanner;

public class Main {
    private static int nbJoueurs=0;
    private static String difficulty="";
    private static boolean play;
    private enum difficultyRange {
        TRESFACILE("novice"),FACILE("facile"),NORMALE("normale"),DIFFICILE("difficile");
        private String nom;
        difficultyRange(String difficulty) {
            this.nom=difficulty;
        }
    }
    public static void main(String... args) {
        BotsType joueur1;
        BotsType joueur2=BotsType.MentalistBot;
        BotsType joueur3=BotsType.MentalistBot;
        BotsType joueur4=BotsType.BasicBot;
        BotsType joueur5=BotsType.MentalistBot;
        BotsType[] bots = new BotsType[]{};
        init();
        int nbDeParties;
        if(play){
            joueur1 = BotsType.BotHumain;
            nbDeParties =1;
            if(difficulty.equals(difficultyRange.TRESFACILE.nom)){
                joueur2=BotsType.BasicBot;
                joueur3=BotsType.BasicBot;
            }
            if(difficulty.equals(difficultyRange.FACILE.nom)){
                joueur2=BotsType.BuilderBot;
                joueur3=BotsType.JealousBot;
            }
            if(difficulty.equals(difficultyRange.NORMALE.nom)){
                joueur2=BotsType.JealousBot;
                joueur3=BotsType.UncleSamBot;
            }
            if(difficulty.equals(difficultyRange.DIFFICILE.nom)){
                joueur2= BotsType.UncleSamBot;
                joueur3= BotsType.UncleSamBot;
            }
            switch (nbJoueurs){
                case 3 : bots = new BotsType[]{joueur1,joueur2,joueur3};break;
                case 4 : bots = new BotsType[]{joueur1,joueur2,joueur3,joueur2};break;
                case 5 : bots = new BotsType[]{joueur1,joueur2,joueur3,joueur2,joueur3};break;
                case 6 : bots = new BotsType[]{joueur1,joueur2,joueur3,joueur2,joueur3,joueur3};break;
            }
            GameLoader gameLoader1 = new GameLoader(bots, nbDeParties);
            gameLoader1.run();
            System.out.println(gameLoader1.computeStats());
        }
        else{
            joueur1 = BotsType.JealousBot;
            joueur2 = BotsType.BuilderBot;
            joueur3 = BotsType.UncleSamBot;
            nbDeParties =1000;
            BotsType[] bestBot = new BotsType[]{joueur1, joueur2, joueur3, joueur4, joueur5, joueur3};
            GameLoader gameLoader2 = new GameLoader(bestBot, nbDeParties);
            gameLoader2.run();
            System.out.println(gameLoader2.computeStats());
        }

    }

    private static void init(){
        Scanner sc = new Scanner(System.in);
        String res;
        do {
            System.out.println("Voulez-vous jouer contre les robots (oui/non)");
            res = sc.next();
        }
        while (!isPlay(res));
        if(res.equals("oui"))play=true;
        else if(res.equals("non"))play=false;
        if(play) {
            do {
                System.out.println("Choisissez la difficulté : (" +difficultyRange.TRESFACILE.nom+ ", " + difficultyRange.FACILE.nom +", "+ difficultyRange.NORMALE.nom +", " + difficultyRange.DIFFICILE.nom+" )");
                difficulty = sc.next();
            }
            while (!isDifficulty());
            do {
                System.out.println("Choisissez le nombre de joueurs : (3-6) ");
                res = sc.next();
            }
            while (!isNbJoueurs(res));
            nbJoueurs=Integer.parseInt(res);
        }
    }
    private static boolean isPlay(String play){
        if(play.equals("oui"))return true;
        if(play.equals("non"))return true;
        System.out.println("Mauvaise réponse");
        return false;
    }
    private static boolean isDifficulty(){
        if(difficulty.equals(difficultyRange.TRESFACILE.nom))return true;
        if(difficulty.equals(difficultyRange.FACILE.nom))return true;
        if(difficulty.equals(difficultyRange.NORMALE.nom))return true;
        if(difficulty.equals(difficultyRange.DIFFICILE.nom))return true;
        System.out.println("Mauvaise réponse");
        return false;
    }
    private static boolean isNbJoueurs(String res){
        int nb;
        try {
            nb=Integer.parseInt(res);
        }
        catch (Exception e) {
            System.out.println("Mauvaise réponse");
            return false;
        }
        if(nb>2 && nb<7) return true;
        System.out.println("Mauvaise réponse");
        return false;
    }

}


