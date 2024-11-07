import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;

import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "0x6B", category = Category.OTHER, description = "QuestionBox", 
name = "0xQuestion", servers = { "Zenyte" }, version = "0.01", discord = "0x6b#7707")

public class QuestionBox extends Script {
	private String version = "0.1";
	
	//private WorldPoint BANKTILE = new WorldPoint(2724, 3493, 0);
	private Integer totalxp= null;
	private Integer startxp= 0;
	private String formattedhourlyXP="";
	private Integer hourlyxp= null;
	private String formattedtotalXP="";
	
	private Integer LVL= 1;
	private Integer REQ= 41;
	private long startTime, upTime;
	
	private String STATUS = null;
	private String SCRIPTNAME = "0x";
	private String SCRIPTER = "0x6B";
	long start = System.currentTimeMillis();
		@SuppressWarnings("deprecation")
	
	@Override
	public void paint(Graphics Graphs) {
			upTime = System.currentTimeMillis() - startTime;
			totalxp = ctx.skills.experience(Skills.WOODCUTTING) - startxp;
			long end = System.currentTimeMillis();
			float sec = (end - start) / 1000F;
			hourlyxp = (int) ((totalxp / sec) * 3600);
			formattedtotalXP = NumberFormat.getNumberInstance(Locale.US).format(totalxp);
			formattedhourlyXP = NumberFormat.getNumberInstance(Locale.US).format(hourlyxp);
			
			Graphics2D g = (Graphics2D) Graphs;
			g.setColor(Color.BLACK);
			g.fillRect(5, 227, 200, 110);
			g.setColor(Color.CYAN);
			g.drawRect(5, 227, 200, 110);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(5, 227, 200, 18);
			g.setColor(Color.CYAN);
			g.drawRect(5, 227, 200, 18);
			g.setColor(Color.YELLOW);
			g.drawString(SCRIPTNAME + " | Scripted by " + SCRIPTER, 10, 242);
			g.setColor(Color.GREEN);
			g.drawString("Total Skill XP Gained: " + formattedtotalXP, 10, 260);
			g.drawString("Skill XP per hour: " + formattedhourlyXP, 10, 275);
			if(LVL == 69) {g.drawString("Skill Level: " + LVL + "     ( ͡° ͜ʖ ͡°)", 10, 290);}else {
				g.drawString("Skill Level: " + LVL, 10, 290);
			}
			g.drawString("Uptime: " + ctx.paint.formatTime(upTime),10,315);
			g.setColor(Color.WHITE);
			g.drawString("Status: " + STATUS, 10, 335);
			g.setColor(Color.DARK_GRAY);
			g.drawString("V" + version, 178, 335);
	}
	
	public class GUI{
		
		private final JDialog mainDialog;
		public GUI() {
			mainDialog = new JDialog();
			mainDialog.setTitle("Test thing");
			mainDialog.setModal(true);
			
			mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		}
	}
		
	@Override
	public void onChatMessage(ChatMessage arg0) {
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onExecute() {
		LVL = ctx.skills.realLevel(Skills.AGILITY);
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void onProcess() {

	}
	
	@Override
	public void onTerminate() {
	// TODO Auto-generated method stub
	STATUS = "terminated";
	}
}