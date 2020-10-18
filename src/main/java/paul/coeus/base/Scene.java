package paul.coeus.base;

import paul.coeus.Engine;

public interface Scene {
    IGameLogic gameLogic = null;

    void init(IGameLogic gameLogic) throws Exception;
    void update(float interval);
}

