package paul.coeus.base;

import paul.coeus.Engine;

public interface IScene {
    IGameLogic gameLogic = null;

    void setupUI();
    void init(IGameLogic gameLogic) throws Exception;
    void update(float interval);
}

