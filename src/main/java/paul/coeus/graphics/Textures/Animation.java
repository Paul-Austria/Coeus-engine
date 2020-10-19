package paul.coeus.graphics.Textures;

public class Animation {

    private Texture[] textures;
    private int currentCycle;
    private float timePerFrame;
    private float timer;
    private boolean stop = false;
    private boolean stopAtFinal = false;
    public Animation(Texture[] textures, int fps) {
        this.textures = textures;
        currentCycle = 0;
        timePerFrame = 1.0f / fps;
        timer = 0;
    }

    public int getCellCount(){
        return textures.length;
    }

    public void resetCycle(){
        currentCycle = 0;
    }

    public int getCurrentCycle(){
        return currentCycle;
    }

    private boolean checkStop(){
        if(stopAtFinal && currentCycle == textures.length-1) return true;
        return  false;
    }
    public Texture getNextTexture(float timePassed){
        timer += timePassed;
        if(timer >= timePerFrame && ! stop) {
            timer = 0;
            currentCycle++;
            if (currentCycle >= textures.length) currentCycle = 0;
            if(checkStop() && stopAtFinal) stop = true;
        }
        return textures[currentCycle];
    }
}
