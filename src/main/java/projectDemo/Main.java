package projectDemo;

import paul.coeus.Engine;
import paul.coeus.base.IGameLogic;

public class Main {
    public static void main(String[] args)
    {
        try {
            boolean vSync = true;
            Engine gameEngine;
            IGameLogic gameLogic = new PlaceHolderLogic();
            gameEngine = new Engine("COEUS Engine", 1280, 720, vSync, gameLogic);
            gameEngine.start();
        }catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
