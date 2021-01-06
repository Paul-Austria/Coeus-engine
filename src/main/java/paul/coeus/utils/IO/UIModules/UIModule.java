package paul.coeus.utils.IO.UIModules;

import org.liquidengine.legui.component.Component;

public interface UIModule {
    Component component = null;
    public Component getCompontent();
    public void init();
    public void update();

}
