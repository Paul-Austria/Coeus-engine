package paul.coeus.graphics.Textures;

public class Animation {
    private Texture[] textures;
    private int currentCycle;
    float timePerFrame;
    float timer;
    public Animation(Texture[] textures, int fps) {
        this.textures = textures;
        currentCycle = 0;
        timePerFrame = 1.0f / fps;
        timer = 0;
    }

    public int getCells(){
        return textures.length;
    }

    public void resetCycle(){
        currentCycle = 0;
    }

    public Texture getNext(float timePassed){
        timer += timePassed;
        if(timer >= timePerFrame) {
            timer = 0;
            currentCycle++;
            if (currentCycle >= textures.length) currentCycle = 0;
        }
        return textures[currentCycle];
    }
}
