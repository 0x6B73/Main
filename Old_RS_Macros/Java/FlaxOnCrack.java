import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

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

@ScriptManifest(author = "0x6B", category = Category.OTHER, description = "Picks flax in Lletya... clicks like a crackhead", 
name = "0xFlaxOnCrack", servers = { "Zenyte" }, version = "1.0", discord = "0x6b#7707")

public class FlaxOnCrack extends Script {
	private String version = "1.0";
	private String SCRIPTNAME = "0xFlaxOnCrack";
	private String SCRIPTER = "0x6B";
	
	private WorldPoint BANK_TILE = new WorldPoint(2353, 3163, 0);
	private WorldPoint FLAX_TILE = new WorldPoint(2355, 3154, 0);
	Random rand = new Random(); 
	private Integer LOOP = 0;
	private long startTime, upTime;
	
	private String STATUS = null;

	long start = System.currentTimeMillis();
		@SuppressWarnings("deprecation")
	
	@Override
	public void paint(Graphics Graphs) {
			upTime = System.currentTimeMillis() - startTime;
			long end = System.currentTimeMillis();
			float sec = (end - start) / 1000F;
			
			Graphics2D g = (Graphics2D) Graphs;
			g.setColor(Color.BLACK);
			g.fillRect(5, 288, 200, 50);
			g.setColor(Color.CYAN);
			g.drawRect(5, 288, 200, 50);
			g.setColor(Color.DARK_GRAY);
			g.fillRect(5, 270, 200, 18);
			g.setColor(Color.CYAN);
			g.drawRect(5, 270, 200, 18);
			g.setColor(Color.YELLOW);
			g.drawString(SCRIPTNAME + " | Scripted by " + SCRIPTER, 10, 285);
			g.setColor(Color.GREEN);
			g.drawString("Uptime: " + ctx.paint.formatTime(upTime),10,315);
			g.setColor(Color.WHITE);
			g.drawString("Status: " + STATUS, 10, 335);
			g.setColor(Color.DARK_GRAY);
			g.drawString("V" + version, 178, 335);
	}
	
	@Override
	public void onChatMessage(ChatMessage arg0) {
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onExecute() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void onProcess() {
		if(ctx.inventory.inventoryFull()) {
			bank();
		}else {
			pickFlax();
		}
	}
	
	public void bank() {
		STATUS = "Banking";
		if(ctx.getPlayers().getLocal().getLocation().distanceTo(BANK_TILE) > 3) { 
			ctx.pathing.step(BANK_TILE);
			ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(BANK_TILE) <= 3, 5000);
		}else {
			//Check if we are idle
			if(ctx.getPlayers().getLocal().getAnimation() == -1) {
				//Find dispenser
				SimpleObject BANK = ctx.objects.populate().filter("Bank Counter").nearest().next();
				//check if we can interact with the dispenser
	        	if(BANK != null && BANK.validateInteractable()) {
	        		 //Check Dispenser
	        		if(!ctx.bank.bankOpen()) {
		        		STATUS = "Clicking Bank";
		        		BANK.click("Bank");
		        		ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 4000);
	        		}
	        		ctx.bank.depositInventory();
	        		ctx.bank.closeBank();
	        	} //end dispenser check
			} //end idle check
		}
	}
	
	public void pickFlax() {
		if(ctx.getPlayers().getLocal().getLocation().distanceTo(FLAX_TILE) > 3) { 
			STATUS = "Walking to flax";
			ctx.pathing.step(FLAX_TILE);
			ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(FLAX_TILE) <= 3, 5000);
		}else {
			//Check if we are idle
			if(ctx.getPlayers().getLocal().getAnimation() == -1) {
				//Find flax
				//
				int LOOP = 500;
				SimpleObject FLAX = ctx.objects.populate().filter("Flax").nearest().next();
        		for (int i = 0; i < LOOP; i++) {
	        			if(!ctx.inventory.inventoryFull()) {
	        				if(ctx.players.getLocal().getAnimation() != 827)
	        				{
	        					STATUS = "Finding Flax";
	        					FLAX = ctx.objects.populate().filter("Flax").nearest().next();
	        					ctx.sleep(250,600);
	        				}
							//check if we can interact with flax
				        	if(FLAX != null && FLAX.validateInteractable()) {
				        		STATUS = "Picking Flax";
		        				FLAX.click(1);
		        				ctx.sleep(50,150);
		        			}
	        			}else {
	        				i = LOOP;
	        			}
	        	} //end flax check
			} //end idle check
		}
	}
	
	@Override
	public void onTerminate() {
	// TODO Auto-generated method stub
	STATUS = "terminated";
	}
}