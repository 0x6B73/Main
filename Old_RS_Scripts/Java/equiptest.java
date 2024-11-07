import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

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

@ScriptManifest(author = "0x6B", category = Category.OTHER, description = "Equip Test", name = "0xEquip", servers = { "Zenyte" }, version = "0.01", discord = "snek#7707")

public class equiptest extends Script {
	
	//private WorldPoint BANKTILE = new WorldPoint(2724, 3493, 0);
	private String STATUS = null;
	private String SCRIPTNAME = "0x";
	private String SCRIPTER = "0x6B";
	long start = System.currentTimeMillis();
	private Integer WCLVL= 1;
	private Integer ATKLVL= 1;
	private Integer WCREQ= 41;
	private Integer ATKREQ= 40;
	@SuppressWarnings("deprecation")
	
	@Override
	public void paint(Graphics Graphs) {
	Graphics2D g = (Graphics2D) Graphs;
	    g.setColor(Color.BLACK);
	    g.fillRect(5, 227, 200, 110);
	    g.setColor(Color.CYAN);
	    g.drawRect(5, 227, 200, 110);
	    g.setColor(Color.YELLOW);
	    g.drawString(SCRIPTNAME + " | Scripted by " + SCRIPTER, 7, 242);
	    g.setColor(Color.GREEN);
	    if(ATKLVL == 69) {g.drawString("Attack Level: " + ATKLVL + "     ( ͡° ͜ʖ ͡°)"+ "  |  WoodCutting Lvl: " + WCLVL, 7, 317);}else {
	    	g.drawString("Attack Level: " + ATKLVL + "  |  WC Level: " + WCLVL, 7, 317);
	    }
	    g.setColor(Color.WHITE);
	    g.drawString("Status: " + STATUS, 7, 332);
	    long end = System.currentTimeMillis();
	    float sec = (end - start) / 1000F;
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
		ATKREQ= 40;
		WCREQ= 41;
		STATUS = "checking attack";
		ATKLVL = ctx.skills.realLevel(Skills.ATTACK);
		WCLVL = ctx.skills.realLevel(Skills.WOODCUTTING);
		if(ATKLVL>=ATKREQ && WCLVL>=WCREQ) { //has stats
			STATUS = "STATS GOOD ATK(" + ATKLVL + ") | WC(" + WCLVL + ")";
			ctx.sleep(1000);
			if(ctx.equipment.populate().filter("Rune axe").next()!=null){//axe equipped
				STATUS = "Axe Equiped";
				ctx.sleep(1000);
			}else { //axe equipped else
				STATUS = "Axe Not Equiped";
				ctx.sleep(1000);
				if(ctx.inventory.populate().filter("Rune axe").next()!=null) { //axe in inv
					STATUS = "Axe in inventory";
					ctx.sleep(1000);
					if(!ctx.getBank().bankOpen()) {//bank not open
						STATUS = "Clicking Axe";
						ctx.inventory.populate().filter("Rune axe").next().click(1);
						ctx.sleep(1000);
					}//end bank not open
				}else{//axe in inv else
					if(ctx.getBank().bankOpen()) {//if bank open
						STATUS = "Bank Open";
						ctx.sleep(1000);
						if(ctx.bank.populate().filter("Rune Axe").next()!=null) {//axe in bank
							STATUS = "Axe in bank";
							ctx.sleep(1000);
							ctx.bank.populate().filter("Rune Axe").next().click(1);
						}//end axe in bank
					}//end bank open
				}//end axe in inv
			}//end axe equipped
		}else { //has stats else
			STATUS = "ATK ( " + ATKLVL + "/" + ATKREQ + " ) | WC ( " + WCLVL + "/" + WCREQ + " )";
			ctx.sleep(1000);
		}//end has stats
	}// end onProcess
	
	@Override
	public void onTerminate() {
	// TODO Auto-generated method stub
	STATUS = "terminated";
	}
}