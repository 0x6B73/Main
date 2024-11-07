import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

import net.runelite.api.Skill;
//import net.runelite.api.Skill;
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

@ScriptManifest(author = "0x6B", category = Category.OTHER, description = "Bank Testing", name = "0xBank", servers = { "Zenyte" }, version = "0.01", discord = "snek#7707")

public class Bank extends Script {
	
	private WorldPoint BANKTILE = new WorldPoint(2724, 3493, 0);
	private String STATUS = null;
	private String SCRIPTNAME = "0xBanker";
	private String SCRIPTER = "0x6B";
	long start = System.currentTimeMillis();
	
	private Integer WCLVL= 1;
	private Integer ATKLVL= 1;
	@SuppressWarnings("deprecation")
	
	@Override
	public void paint(Graphics Graphs) {
	Graphics2D g = (Graphics2D) Graphs;
	        g.setColor(Color.GRAY);
	        g.fillRect(5, 257, 200, 80);
	        g.setColor(Color.RED);
	        g.drawRect(5, 257, 200, 80);
	        g.setColor(Color.YELLOW);
	        g.drawString(SCRIPTNAME + " | Scripted by " + SCRIPTER, 7, 272);
	        g.setColor(Color.WHITE);
	        g.drawString("Level: " + WCLVL, 7, 317);
	        g.setColor(Color.GREEN);
	        g.drawString("Status:" + STATUS, 7, 332);
	        long end = System.currentTimeMillis();
	}
	
	@Override
	public void onChatMessage(ChatMessage arg0) {
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onExecute() {
		WCLVL = ctx.skills.realLevel(Skills.WOODCUTTING);
		ATKLVL = ctx.skills.realLevel(Skills.ATTACK);
	}
	
	@Override
	public void onProcess() {
		//check if we are far from bank and walk to the bank if so
		if(ctx.getPlayers().getLocal().getLocation().distanceTo(BANKTILE) > 3) {
			STATUS = "Walking to bank";
			ctx.pathing.step(BANKTILE);
			ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(BANKTILE) <= 3, 5000);
		}else { //close to bank
			//interact with the banker
			STATUS = "Talking to banker boi";
			SimpleNpc bank = ctx.getNpcs().populate().filter("Banker").nearest().next();
			//check if we can interact with the banker
			if(bank != null && bank.validateInteractable()){
				//Open bank
				STATUS = "Opening Bank";
				bank.click("Bank");
				ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 2000);
			} // end check if interact with banker
		} // end close to bank
		//open bank
		if(ctx.getBank().bankOpen()){
			STATUS = "Banking";
			//check if can use Rune Axe
			if(WCLVL >=91) 
			{
				//Check if Rune Axe is in inventory
				if(ctx.getInventory().populate().filter("Rune axe").next()!=null)
				{
					STATUS = "Rune axe in inv";
					//Deposit all except axe
					ctx.getBank().depositAllExcept("Rune axe");
				//Rune Axe is not in inventory
				}else 
				{
					//Deposit everything
					ctx.getBank().depositInventory();
					//Check if there is a Rune Axe in the bank
					if(ctx.getBank().populate().filter("Rune axe").next()!=null) 
					{
						//withdraw the Rune Axe
						ctx.getBank().withdraw("Rune axe", 1);
					}//end check if Rune Axe is in bank
				}//End Check if rune axe is in inventory
			//If we can't use a Rune Axe, check Iron
			}else 
			{
				{
					//Check if Bronze Axe is in inventory
					if(ctx.getInventory().populate().filter("Bronze axe").next()!=null)
					{
						//Deposit all except Bronze Axe
						ctx.getBank().depositAllExcept("Bronze axe");
						//Bronze Axe is not in inventory
					}else 
					{
						//Deposit Everything
						ctx.getBank().depositInventory();
						//Check if Bronze Axe is in the bank
						if(ctx.getBank().populate().filter("Bronze axe").next()!=null) 
						{
							//withdraw the Bronze Axe
							ctx.getBank().withdraw("Bronze axe", 1);
						}//end check if Bronze Axe is in bank
					}
				}
			}
				ctx.sleep(1000, 3500);
			ctx.getBank().closeBank();
		}
	}
	
	@Override
	public void onTerminate() {
	// TODO Auto-generated method stub
	STATUS = "terminated";
	}

}