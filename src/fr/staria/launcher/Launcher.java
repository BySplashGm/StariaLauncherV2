package fr.staria.launcher;

import java.io.File;
import java.io.IOException;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.launcher.AuthInfos;
import fr.theshark34.openlauncherlib.launcher.GameFolder;
import fr.theshark34.openlauncherlib.launcher.GameInfos;
import fr.theshark34.openlauncherlib.launcher.GameLauncher;
import fr.theshark34.openlauncherlib.launcher.GameTweak;
import fr.theshark34.openlauncherlib.launcher.GameType;
import fr.theshark34.openlauncherlib.launcher.GameVersion;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;

public class Launcher {

    public static final GameVersion ST_VERSION = new GameVersion("1.8.9", GameType.V1_8_HIGHER);
    public static final GameInfos ST_INFOS = new GameInfos("staria", ST_VERSION, false, new GameTweak[]{GameTweak.OPTIFINE});
    public static final File ST_DIR = ST_INFOS.getGameDir();
    
    public static AuthInfos authInfos;
    private static Thread updateThread;
    
    
    public static void auth(String username) throws AuthenticationException {
    	
    	authInfos = new AuthInfos(username, "", "");
    	
    }
    
    public static void update() throws Exception {
    	
    	SUpdate su = new SUpdate("http://www.staria.fr/launcherupdate", ST_DIR);
    	
    	updateThread = new Thread() {
    		
    		private int max;
    		private int val;
    		
    		@Override
    		public void run() {
    			while (!this.isInterrupted()) {
    				
    				if (BarAPI.getNumberOfFileToDownload() == 0) {
    					LauncherFrame.getInstance().getLauncherPanel().setInfoText("Verification des fichiers...");
    					continue;
    				}
    				
    				val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
    				max = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
    				
    				LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
    				LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
    				
    				LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des fichiers... " + BarAPI.getNumberOfDownloadedFiles() + "/" +
    						BarAPI.getNumberOfFileToDownload() + " - " + Swinger.percentage(val, max) + "%");
    				
    			}
    		}
    		
    	};
    	
    	updateThread.start();
    	su.start();
    	
    	
    	updateThread.interrupt();
    	
    }
    
    public static void launch() throws IOException {
    	
    	GameLauncher gameLauncher = new GameLauncher(ST_INFOS, GameFolder.BASIC, authInfos);
    	Process p = gameLauncher.launch();
    	
    	try {
    		Thread.sleep(5000L);
    	} catch (InterruptedException e) {
    		
    	} LauncherFrame.getInstance().setVisible(false);
    	try {
    		p.waitFor();
    	} catch (InterruptedException e) {
    		
    	} System.exit(0);
    	
    }
    
    
    public static void interruptThread() {
    	updateThread.interrupt();
    }

}
