package fr.unice.polytech.startingpoint.BotsPackage;

public enum BotsType {

    BasicBot,
    BuilderBot,
    UncleSamBot,
    JealousBot,
    MentalistBot,
    BotHumain;

    public BaseBot createBot(int ID) {
        switch (this) {
            case BasicBot:
                return new BasicBot(ID);
            case BuilderBot:
                return new BuilderBot(ID);
            case JealousBot:
                return new JealousBot(ID);
            case UncleSamBot:
                return new UncleSamBot(ID);
            case MentalistBot:
                return new MentalistBot(ID);
            case BotHumain:
                return new BotHumain(ID);
        }
        return null;
    }
}
