package fr.staria.launcher;

import javax.swing.JFrame;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	@SuppressWarnings("unused")
	private static LauncherPanel launcherPanel;
	
	public LauncherFrame() {
		this.setTitle("StariaLauncher 1.1.2");
		this.setSize(1280, 720);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage(Swinger.getResource("icon.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		this.setVisible(true);
	}

    public static void main(String[] args){
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/fr/staria/launcher/res/");
        
        instance = new LauncherFrame();
    }
    
    public static LauncherFrame getInstance() {
    	return instance;
    }
    
    public LauncherPanel getLauncherPanel() {
    	return launcherPanel;
    }

}
