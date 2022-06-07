package fr.staria.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = Swinger.getResource("background.png");
	
	private JTextField  usernameField = new JTextField();
	
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("playButton.png"));
	private STexturedButton quitButton = new STexturedButton(Swinger.getResource("quitButton.png"));
	private STexturedButton minimizeButton = new STexturedButton(Swinger.getResource("minimizeButton.png"));
	
	private SColoredBar progressBar = new SColoredBar(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Cliquez sur Jouer !", SwingConstants.CENTER);
	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.WHITE);
		usernameField.setFont(usernameField.getFont().deriveFont(40F));
		usernameField.setCaretColor(Color.WHITE);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(465,415,320,40);
		this.add(usernameField);
		
		playButton.setBounds(450,500);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(1240,0);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		minimizeButton.setBounds(1200,0);
		minimizeButton.addEventListener(this);
		this.add(minimizeButton);
		
		progressBar.setBounds(400, 680, 480, 20);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont().deriveFont(16F));
		infoLabel.setBounds(400, 650, 480, 25);
		this.add(infoLabel);
	}
	
	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton) {
			
			setFieldsEnabled(false);
			
			if (usernameField.getText().replaceAll(" ", "").length() == 0) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo valide", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText());
					} catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					/*try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de mettre le jeu à jour : " + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}*/
					
					try {
						Launcher.launch();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de lancer le jeu : " + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
					}
					
					System.out.println("Loading");
				}
			};
			t.start();
			
			System.out.println("Game starting...");
		} else if (e.getSource() == quitButton) {
			System.exit(0);
		} else if (e.getSource() == minimizeButton) {
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}

}
