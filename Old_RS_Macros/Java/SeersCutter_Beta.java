import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

//import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "0x6B", category = Category.WOODCUTTING, description = "Cuts trees at Seers. Automatic tree switching on level. Supports rune axe in inventory or equiped Bronze, Iron, or Rune axe.", 
name = "0xSeers Cutter", servers = { "Zenyte" }, version = "2.1B", discord = "0x6b#7707")

public class SeersCutter_Beta extends Script {
	
	private String version = "2.1";
	private WorldPoint BANKTILE = new WorldPoint(2724, 3493, 0);
	private WorldPoint Tree = new WorldPoint(2724, 3482, 0);
	private WorldPoint Oak = new WorldPoint(2719, 3480, 0);
	private WorldPoint Willow = new WorldPoint(2713, 3510, 0);
	private WorldPoint Maple = new WorldPoint(2730, 3500, 0);
	private WorldPoint Yew = new WorldPoint(2711, 3462, 0);
	private WorldPoint Magic = new WorldPoint(2694, 3426, 0);
	private WorldPoint YEW_AREA_UPPER_COORDS = new WorldPoint(2705, 3466,0);
	private WorldPoint YEW_AREA_LOWER_COORDS = new WorldPoint(2720, 3456,0);
	private WorldPoint MAGIC_AREA_UPPER_COORDS = new WorldPoint(2689, 3427,0);
	private WorldPoint MAGIC_AREA_LOWER_COORDS = new WorldPoint(2709, 3416,0);
	
	WorldPoint[] YewPath = {
		    new WorldPoint(2725, 3491, 0),
		    new WorldPoint(2725, 3484, 0),
		    new WorldPoint(2724, 3477, 0),
		    new WorldPoint(2723, 3472, 0),
		    new WorldPoint(2721, 3467, 0),
		    new WorldPoint(2718, 3462, 0),
		    new WorldPoint(2713, 3461, 0)
	};
	WorldPoint[] MagicPath = {
			new WorldPoint(2725, 3485, 0),
		    new WorldPoint(2724, 3476, 0),
		    new WorldPoint(2722, 3469, 0),
		    new WorldPoint(2721, 3460, 0),
		    new WorldPoint(2720, 3453, 0),
		    new WorldPoint(2713, 3447, 0),
		    new WorldPoint(2710, 3442, 0),
		    new WorldPoint(2707, 3436, 0),
		    new WorldPoint(2706, 3430, 0),
		    new WorldPoint(2701, 3425, 0),
		    new WorldPoint(2698, 3422, 0)
	    };
	
	private WorldArea YewTreeArea = new WorldArea(YEW_AREA_UPPER_COORDS,YEW_AREA_LOWER_COORDS);
	private WorldArea MagicTreeArea = new WorldArea(MAGIC_AREA_UPPER_COORDS,MAGIC_AREA_LOWER_COORDS);
	
	private Integer totalwoodcuttingxp= null;
	private Integer startwoodcuttingxp= 0;
	private String formattedhourlywoodcuttingXP="";
	private Integer hourlywoodcuttingxp= null;
	private String formattedtotalwoodcuttingXP="";
	private String STATUS = null;
	
	private Integer WCLVL= 1;
	private Integer ATKLVL= 1;
	private Integer WCREQ= 41;
	private Integer ATKREQ= 40;
	private Boolean CANUSERUNEAXE=false; //does the player have the proper woodcutting to use the axe
	private Boolean CANWIELDRUNEAXE=false; //does the player have the proper attack to wield AND use the axe
	private Boolean HASAXE=false;
	private Boolean WIELDINGRUNEAXE=false;
	private Boolean HASRUNEAXEININVENTORY=false;
	private Boolean HASRUNEAXEINBANK=true;
	private String TREETOCUT = "Tree";
	private String SCRIPTNAME = "0xSeers Cutter";
	private String SCRIPTER = "0x6B";
	long start = System.currentTimeMillis();
	private long startTime, upTime;
	
	@SuppressWarnings("deprecation")
	
	@Override
	public void paint(Graphics Graphs) {
		upTime = System.currentTimeMillis() - startTime;
		totalwoodcuttingxp = ctx.skills.experience(Skills.WOODCUTTING) - startwoodcuttingxp;
		long end = System.currentTimeMillis();
		float sec = (end - start) / 1000F;
		hourlywoodcuttingxp = (int) ((totalwoodcuttingxp / sec) * 3600);
		formattedtotalwoodcuttingXP = NumberFormat.getNumberInstance(Locale.US).format(totalwoodcuttingxp);
		formattedhourlywoodcuttingXP = NumberFormat.getNumberInstance(Locale.US).format(hourlywoodcuttingxp);
		
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
		g.drawString("Total Woodcutting XP Gained: " + formattedtotalwoodcuttingXP, 10, 260);
		g.drawString("Woodcutting XP per hour: " + formattedhourlywoodcuttingXP, 10, 275);
		if(WCLVL == 69) {g.drawString("Woodcutting Level: " + WCLVL + "     ( ͡° ͜ʖ ͡°)", 10, 290);}else {
			g.drawString("Woodcutting Level: " + WCLVL, 10, 290);
		}
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
		startwoodcuttingxp = ctx.skills.experience(Skills.WOODCUTTING);
		WCLVL = ctx.skills.realLevel(Skills.WOODCUTTING);
		ATKLVL = ctx.skills.realLevel(Skills.WOODCUTTING);
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void onProcess() {
		//Get WC Level
		WCLVL = ctx.skills.realLevel(Skills.WOODCUTTING);
		//Get ATK Level
		ATKLVL = ctx.skills.realLevel(Skills.WOODCUTTING);
		// TODO Check if player has an axe equipped or in their inventory - search bank if they do not - equip if they can
		if(CANUSERUNEAXE == false) {//can wield rune axe
			if(ATKLVL>=ATKREQ && WCLVL>=WCREQ) {//has stat reqs
				CANWIELDRUNEAXE=true;
			}else{
				if(WCLVL>=WCREQ) {
					CANUSERUNEAXE=true;
				}
			}//end has stat reqs
		}//end can wield rune axe
		
		if(CANWIELDRUNEAXE==true) {
			if(!(ctx.equipment.populate().filter("Rune axe").next()!=null)){//axe not equipped
				if(ctx.inventory.populate().filter("Rune axe").next()!=null) { //axe in inv
					if(!ctx.getBank().bankOpen()) {//bank not open
						STATUS = "Wielding Rune Axe";
						ctx.inventory.populate().filter("Rune axe").next().click(1);
						HASAXE=true;
					}//end bank not open
				}else{//axe in inv else
					HASRUNEAXEININVENTORY = false;
				}//end axe in inv
			}else {
				WIELDINGRUNEAXE=true;
				HASAXE=true;
			}
		}else if (CANUSERUNEAXE==true) {
			if(ctx.inventory.populate().filter("Rune axe").next()!=null) {
				HASRUNEAXEININVENTORY = true;
				HASAXE = true;
			}else {
				HASRUNEAXEININVENTORY = false;
			}
		}
		if(HASAXE==false) {
			if(ctx.equipment.populate().filter("Bronze axe").next()==null){//does not have axe
				Bank();
			}else {
				HASAXE=true;
			}
		}
		//Select tree to cut
		if (WCLVL < 15) {
			TREETOCUT = "Tree";
			Routine(Tree);
		}else if(WCLVL >=15 && WCLVL < 30) {
			TREETOCUT = "OAK";
			Routine(Oak);
		}else if (WCLVL >= 30 && WCLVL < 45) {
			TREETOCUT = "Willow";
			Routine(Willow);
		} else if (WCLVL >= 45 && WCLVL < 60) {
			TREETOCUT = "Maple Tree";
			Routine(Maple);
		} else if (WCLVL >= 60 && WCLVL < 75) {
			TREETOCUT = "Yew";
			Routine(Yew);
		} else if (WCLVL >= 75) {
			TREETOCUT = "Magic Tree";
			WalkMagic();
			Routine(Magic);
		}
	}
	
	private void WalkYew() {
		if(!ctx.pathing.inArea(MagicTreeArea)) {
			if(!ctx.inventory.inventoryFull()) {
				ctx.pathing.walkPath(MagicPath);
			}
		}
	}
	
	private void WalkMagic() {
		if(!ctx.pathing.inArea(MagicTreeArea)) {
			if(!ctx.inventory.inventoryFull()) {
				ctx.pathing.walkPath(MagicPath);
			}
		}
	}
	
	private void Bank() {
	//check if we are far from bank and walk to the bank if so
		if(ctx.getPlayers().getLocal().getLocation().distanceTo(BANKTILE) > 3) { 
			if(ctx.getPlayers().getLocal().getLocation().distanceTo(BANKTILE) > 20) {
				STATUS = "Walking to bank from Magics";
				ctx.pathing.walkPath(MagicPath, true);
			}else {
				STATUS = "Walking to bank";
				ctx.pathing.step(BANKTILE);
				ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(BANKTILE) <= 3, 5000);
			}
		}else { //near bank
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
		} // end near bank
		
		//open bank
		if(ctx.getBank().bankOpen()){
			//deposit everything except Rune Axe
			STATUS = "Banking logs";
			if(CANUSERUNEAXE==true || CANWIELDRUNEAXE==true) { //can use rune axe or can wield rune axe
				if(HASRUNEAXEININVENTORY==true) {//has rune axe in inventory
					ctx.getBank().depositAllExcept("Rune Axe");
				}else { //doesn't have axe in inventory but can use
					if(!ctx.inventory.isEmpty()){//if inventory not empty
						ctx.bank.depositInventory();
					}
					if(HASRUNEAXEINBANK==true && WIELDINGRUNEAXE==false) {//rune axe in bank and not wielding
						if(ctx.bank.populate().filter("Rune Axe").next()!=null) {//axe in bank
							ctx.sleep(500);
							ctx.bank.populate().filter("Rune Axe").next().click(1);
							ctx.sleepCondition(() -> ctx.inventory.populate().filter("Rune axe").next()!=null, 500);
						}else {//axe in bank else
							HASRUNEAXEINBANK=false;
						}//end axe in bank
					}//end rune axe not in bank
				}//end axe in inventory
			}else { //cant use or wield rune axe
				if(!ctx.inventory.isEmpty()){//if inventory not empty
					ctx.bank.depositInventory();
				}
				ctx.sleepCondition(() -> ctx.getInventory().populate().isEmpty(), 2000);
				if (HASAXE==false) {
					if(ctx.bank.populate().filter("Iron Axe").next()!=null) {//axe in bank
						ctx.bank.populate().filter("Iron Axe").next().click(1);
					}
				}else if(ctx.bank.populate().filter("Bronze Axe").next()!=null) {//axe in bank
						ctx.bank.populate().filter("Bronze Axe").next().click(1);
				}
			}
			ctx.getBank().closeBank();
		}
	}
	
	private void Routine(WorldPoint worldPoint) {
		if(STATUS == "terminated") {
			//return;
			ctx.stopScript();
		}
		//check that inventory is not full
		if(!ctx.inventory.inventoryFull())
		{
			 //check if we are close to the trees we are cutting
			if(ctx.getPlayers().getLocal().getLocation().distanceTo(worldPoint) < 10){
				//Check if we are idle
				if(ctx.getPlayers().getLocal().getAnimation() == -1) {
					//Get the next tree to cut
					SimpleObject tree = ctx.objects.populate().filter(TREETOCUT).nearest().next();
					//check if there is a tree that we can interact with
		        	if(tree != null && tree.validateInteractable()) {
		        		 //Chop tree
		        		STATUS = "Chopping " + TREETOCUT + ".";
		        		tree.click("chop down");
		        		ctx.sleep(1000);
		        		ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);
		        	} //end tree check
				} //end idle check
			}else{//If we are not close to the tree, walk to it
				STATUS = "Walking to " + TREETOCUT + ".";
				ctx.pathing.step(worldPoint);
				ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(worldPoint) <= 10, 1000);
			}//end close to tree check
		}else { //inventory is full
			Bank();
		}
	}
	
	@Override
	public void onTerminate() {
	// TODO Auto-generated method stub
	STATUS = "terminated";
	}
}