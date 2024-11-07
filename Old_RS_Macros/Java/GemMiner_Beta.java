import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

//import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.queries.SimpleItemQuery;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "0x6B", category = Category.MINING, description = "Mines gem rocks at Shilo Village.", name = "0xGem Miner Beta", servers = { "Zenyte" }, version = "0.9", discord = "0x6b#7707")

public class GemMiner_Beta extends Script {
	
	private WorldPoint BANKTILE = new WorldPoint(2784, 2912, 0);
	private WorldPoint GEMROCKS = new WorldPoint(2824, 2998, 0);
	private WorldPoint BANK_AREA_UPPER_COORDS = new WorldPoint(2848, 2960,0);
	private WorldPoint BANK_AREA_LOWER_COORDS = new WorldPoint(2856, 2951 ,0);
	private WorldPoint GEM_AREA_UPPER_COORDS = new WorldPoint(2820, 3003,0);
	private WorldPoint GEM_AREA_LOWER_COORDS = new WorldPoint(2829, 2994,0);
	
	
	WorldPoint[] GemPath = {
			new WorldPoint(2852, 2955, 0),
			new WorldPoint(2851, 2959, 0),
			new WorldPoint(2844, 2962, 0),
			new WorldPoint(2839, 2965, 0),
			new WorldPoint(2833, 2967, 0),
			new WorldPoint(2831, 2970, 0),
			new WorldPoint(2831, 2976, 0),
			new WorldPoint(2829, 2979, 0),
			new WorldPoint(2828, 2984, 0),
			new WorldPoint(2828, 2989, 0),
			new WorldPoint(2827, 2994, 0),
			new WorldPoint(2825, 2997, 0)
	    };
	
	private WorldArea BankArea = new WorldArea(BANK_AREA_UPPER_COORDS,BANK_AREA_LOWER_COORDS);
	private WorldArea GemArea = new WorldArea(GEM_AREA_UPPER_COORDS,GEM_AREA_LOWER_COORDS);
	
	
	private Integer totalminingxp= null;
	private Integer totalcraftingxp= null;
	private Integer startminingxp= null;
	private Integer startcraftingxp= null;
	private String formattedminingXP="";
	private String formattedcraftingXP="";
	private Integer hourlyminingxp= null;
	private Integer hourlycraftingxp= null;
	private String STATUS = null;
	private Integer MININGLVL= 1;
	private Integer CRAFTLVL= 1;
	private String SCRIPTNAME = "0xGem Miner";
	private String SCRIPTER = "0x6B";
	long start = System.currentTimeMillis();
	
	//user variables
	// TODO - Make these options part of a GUI checkbox
	private Boolean DROPLOWGEMS = true;
	private Boolean CUTGEMS = true;
	
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
	        g.drawString("Total Mining XP Gained: " + totalminingxp, 7, 257);
	        g.drawString("Mining XP per hour: " + formattedminingXP, 7, 272);
	        g.drawString("Total Crafting XP Gained: " + totalcraftingxp, 7, 287);
	        g.drawString("Crafting XP per hour: " + formattedcraftingXP, 7, 302);
	        if(MININGLVL == 69) {g.drawString("Mining Level: " + MININGLVL + "     ( ͡° ͜ʖ ͡°)"+ "  |  Craft Lvl: " + CRAFTLVL, 7, 317);}else {
	        	g.drawString("Mining Level: " + MININGLVL + "  |  Crafting Level: " + CRAFTLVL, 7, 317);
	        }
	        g.setColor(Color.WHITE);
	        g.drawString("Status: " + STATUS, 7, 332);
	        totalminingxp = ctx.skills.experience(Skills.MINING) - startminingxp;
	        totalcraftingxp = ctx.skills.experience(Skills.CRAFTING) - startcraftingxp;
	        long end = System.currentTimeMillis();
	        float sec = (end - start) / 1000F;
	        hourlyminingxp = (int) ((totalminingxp / sec) * 3600);
	        hourlycraftingxp = (int) ((totalcraftingxp / sec) * 3600);
	        formattedminingXP = NumberFormat.getNumberInstance(Locale.US).format(hourlyminingxp);
	        formattedcraftingXP = NumberFormat.getNumberInstance(Locale.US).format(hourlycraftingxp);
	}
	
	@Override
	public void onChatMessage(ChatMessage arg0) {
	// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onExecute() {
		startminingxp = ctx.skills.experience(Skills.MINING);
		startcraftingxp = ctx.skills.experience(Skills.CRAFTING);
		MININGLVL = ctx.skills.realLevel(Skills.MINING);
		CRAFTLVL = ctx.skills.realLevel(Skills.CRAFTING);
		this.paint(null);
	}
	
	@Override
	public void onProcess() {
		//Get Mining Level
		MININGLVL = ctx.skills.realLevel(Skills.MINING);
		
		if(CUTGEMS == true && ctx.inventory.populate().filter("Chisel").next()!=null) {
			//opal
			STATUS = "Checking Opal";
			if(ctx.inventory.populate().filter("Uncut Opal").next() != null) {
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutOpal = ctx.inventory.populate().filter("Uncut Opal").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutOpal.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutOpal);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut Opal").next() == null, 15000);
			}
			//jade
			STATUS = "Checking Jade";
			if(CRAFTLVL >= 13 && ctx.inventory.populate().filter("Uncut jade").next()  != null) {
				STATUS = "Cutting Jade";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutJade = ctx.inventory.populate().filter("Uncut Jade").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutJade.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutJade);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut Jade").next() == null, 15000);
			}
			//red topaz
			STATUS = "Checking Red Topaz";
			if(CRAFTLVL >= 16 && ctx.inventory.populate().filter("Uncut red topaz").next()  != null) {
				STATUS = "Cutting Red Topaz";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutTopaz = ctx.inventory.populate().filter("Uncut red topaz").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutTopaz.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutTopaz);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut red topaz").next() == null, 15000);
			}
			//sapphire
			STATUS = "Checking Sapphire";
			if(CRAFTLVL >= 16 && ctx.inventory.populate().filter("Uncut sapphire").next()  != null) {
				STATUS = "Cutting Sapphire";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutSapphire = ctx.inventory.populate().filter("Uncut sapphire").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutSapphire.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutSapphire);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut sapphire").next() == null, 1500);
			}
			//emerald
			STATUS = "Checking Emerald";
			if(CRAFTLVL >= 27 && ctx.inventory.populate().filter("Uncut emerald").next()  != null) {
				STATUS = "Cutting Emerald";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutEmerald = ctx.inventory.populate().filter("Uncut emerald").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutEmerald.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutEmerald);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut emerald").next() == null, 15000);
			}
			//ruby
			STATUS = "Checking Ruby";
			if(CRAFTLVL >= 34 && ctx.inventory.populate().filter("Uncut ruby").next()  != null) {
				STATUS = "Cutting Ruby";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutRuby = ctx.inventory.populate().filter("Uncut ruby").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutRuby.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutRuby);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut ruby").next() == null, 15000);
			}
			//diamond
			STATUS = "Checking Diamond";
			if(CRAFTLVL >= 43 && ctx.inventory.populate().filter("Uncut diamond").next()  != null) {
				STATUS = "Cutting Diamond";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutDiamond = ctx.inventory.populate().filter("Uncut diamond").next();
				Chisel.click(1);
				ctx.sleep(100, 450);
				UncutDiamond.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutDiamond);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut diamond").next() == null, 15000);
			}
			//dragonstone
			STATUS = "Checking Dragonstone";
			if(CRAFTLVL >= 55 && ctx.inventory.populate().filter("Uncut dragonstone").next()  != null) {
				STATUS = "Cutting Dragonstone";
				SimpleItem Chisel = ctx.inventory.populate().filter(1755).next();
				SimpleItem UncutDragonstone = ctx.inventory.populate().filter("Uncut dragonstone").next();
				Chisel.click(1);
				UncutDragonstone.click(1);
				//ctx.inventory.itemOnItem(Chisel, UncutDragonstone);
				ctx.sleep(500, 1250);
				ctx.dialogue.clickDialogueOption(1);
				ctx.sleep(100, 250);
				ctx.sleepCondition(() -> ctx.inventory.populate().filter("Uncut dragonstone").next() == null, 15000);
				//ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);
			}
			ctx.inventory.dropItems(ctx.inventory.populate().filter("Crushed gem"));
			//end cutting
		}//end cut gems
		
		//have space in inventory
		STATUS = "Waiting";
		//STATUS = "Checking inventory";
		if(!ctx.inventory.inventoryFull()) {
			//check if in gem area
			if(ctx.pathing.inArea(GemArea)) {
				//in gem area - IDLE ANIMATION CHECK
				if(ctx.getPlayers().getLocal().getAnimation() == -1) {
					//drop low gems
					if(DROPLOWGEMS == true) {
						//search for gems
						if(ctx.inventory.populate().filter("Uncut Red Topaz","Uncut Jade","Uncut Opal")!=null) {
			        		STATUS = "Dropping useless gems";
							ctx.inventory.dropItems(ctx.inventory.populate().filter("Uncut Opal","Uncut Jade","Uncut Red Topaz","Opal","Jade","Red Topaz"));
						}
					}//end drop low gems
					SimpleObject GemRock = ctx.objects.populate().filter(7464,7463).nearest().next();
					STATUS = "Looking for rock to mine";
					//check if there is a rock to mine
			        if(GemRock != null && GemRock.validateInteractable()) {
		        		//Mine Rock
		        		STATUS = "Mining";
		        		GemRock.click("Mine");
		        		ctx.sleep(1000);
		        		ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);	        		
		        	}//end gemrock check
				}//end IDLE ANIMATION CHECK
		}else{ //not in gem area
				// walk to gem area
				STATUS = "Walking to gem area";
				ctx.pathing.walkPath(GemPath);
				ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);
			}
		}else {//Inventory full, walk back to bank
			STATUS = "Walking to bank";
			ctx.pathing.walkPath(GemPath, true);
			if(ctx.pathing.inArea(BankArea)) {
				STATUS = "Talking to banker boi";
				SimpleNpc bank = ctx.getNpcs().populate().filter("Banker").nearest().next();
				//check if we can interact with the banker
				if(bank != null && bank.validateInteractable()){
					//Open bank
					STATUS = "Praising reptilian overlords";
					bank.click("Bank");
					ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 2000);
				} // end check if interact with banker
			} // end close to bank
			//open bank
			if(ctx.getBank().bankOpen()){
				STATUS = "Banking Gems";
				if(CUTGEMS == true) {
					ctx.getBank().depositAllExcept("chisel","Rune pickaxe");
				}else {
					ctx.getBank().depositAllExcept("Rune pickxe");
				}
				ctx.sleepCondition(() -> ctx.getInventory().populate().isEmpty(), 10000);
				ctx.getBank().closeBank();
			}
					
		}
	}

	@Override
	public void onTerminate() {
		STATUS = "terminated";
	}
}