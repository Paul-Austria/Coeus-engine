package paul.coeus.base;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.liquidengine.legui.animation.AnimatorProvider;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.CursorEnterEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.CursorEnterEventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.system.context.CallbackKeeper;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.context.DefaultCallbackKeeper;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessor;
import org.liquidengine.legui.system.handler.processor.SystemEventProcessorImpl;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.liquidengine.legui.system.renderer.Renderer;
import org.liquidengine.legui.system.renderer.nvg.NvgRenderer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import paul.coeus.GlobalModules;
import paul.coeus.graphics.postProcessing.FBO;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private final String title;
    private int width;
    private int height;
    private long windowHandle;
    private boolean resized;
    private boolean vSync;
    private boolean debugMode = false;
    private Renderer UIRenderer;

    SystemEventProcessor systemEventProcessor;
    CallbackKeeper keeper;
    Frame frame;
    Context UIContext;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }






    public void init(){

        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }



        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        GlobalModules.setWindow(this);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }


        //UI CALLBACKS
        keeper = new DefaultCallbackKeeper();
        CallbackKeeper.registerCallbacks(this.windowHandle, keeper);

        systemEventProcessor = new SystemEventProcessorImpl();
        SystemEventProcessor.addDefaultCallbacks(keeper, systemEventProcessor);



        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        if(isvSync())
        {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glClearColor(0.0f, 0, 0, 0.0f);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable( GL_BLEND );
        glDepthFunc(GL_LEQUAL);
        glDepthRange(0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);



        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            Window.this.width = width;
            Window.this.height = height;
            this.frame.setSize(new Vector2f(width, height));
            try {
                GlobalModules.setFbo(new FBO());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Window.this.setResized(true);
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_F12 && action == GLFW_RELEASE) {
                debugMode = !debugMode;
                if(debugMode)
                {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);  // this tells it to only render lines

                }
                else {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

                }
            }
        });


        setupUI();

    }

    private void setupUI() {

       UIContext = new Context(this.getWindowHandle());
        UIRenderer = new NvgRenderer();
        UIRenderer.initialize();
        frame = new Frame(this.width, this.height);



        frame.getContainer().setFocusable(false);
    }

    int i = 0;


    public void destroyUIStuff(){
        UIRenderer.destroy();
    }


    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void update(){


        glfwSwapBuffers(windowHandle);
        glfwPollEvents();

    //    systemEventProcessor.processEvents(frame, UIContext);
        EventProcessorProvider.getInstance().processEvents();

        LayoutManager.getInstance().layout(frame);

        AnimatorProvider.getAnimator().runAnimations();
    }


    public void renderUI(){
        UIContext.updateGlfwWindow();
        UIRenderer.render(frame, UIContext);
    }

    public String getTitle() {
        return title;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }


    public Frame getFrame() {
        return frame;
    }

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public boolean isResized() {
        return resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }


    public void setClearColor(float i, float i1, float i2, float v) {
        glClearColor(i,i1,i2,v);
    }
}
