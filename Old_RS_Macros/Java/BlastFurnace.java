import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

import net.runelite.api.Skill;
import net.runelite.api.Varbits;
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

@ScriptManifest(author = "0x6B", category = Category.MINIGAMES, description = "Buys iron and coal at the Blast Furnace and makes bars", 
name = "0xBlast_Furnace_Steel", servers = { "Zenyte" }, version = "0.01", discord = "0x6b#7707")

public class BlastFurnace extends Script {
	//Script Variables
	private String version = "1.0";
	private String STATUS = null;
	private String SCRIPTNAME = "0x";
	private String SCRIPTER = "0x6B";
	private boolean DEBUG = false;
	
	//Player Variables
	private Integer totalxp= 0;
	private Integer startxp= 0;
	private String formattedhourlyXP="";
	private Integer hourlyxp= 0;
	private String formattedtotalXP="";
	private Integer LVL= 1;
	
	//Time Variables
	private long startTime, upTime;
	private long start = System.currentTimeMillis();
	
	//Blast Furnace variables
	private Integer COFFER_GOLD = 0; // Varbit BLAST_FURNACE_COFFER
	private Integer BF_BAR_DISPENSER = 0; //Varbit BAR_DISPENSER - 0, 1, 2, 3 - 3 is done, 0 is waiting
	private Integer BF_IRON = 0; //Varbit BLAST_FURNACE_IRON
	private Integer BF_COAL = 0; //Varbit BLAST_FURNACE_COAL
	private Integer BF_STEEL_BAR = 0; //Varbit BLAST_FURNACE_STEEL_BAR
	
	//Script Variables
	private Integer COINS_IN_INV = 0;
	private Integer IRON_ORE_IN_INV = 0;
	private Integer COAL_IN_INV = 0;
	private Integer COINS_TO_ADD = 25000; //How much money to add to the coffer
	
	//NPC Variables
		//Ore dude
		private Integer ORDAN_ID  = 1560;
		private Integer IRON_NPC = 2926; //used by conveyor belt
		private Integer COAL_NPC = 2932; //used by conveyor belt
		
	//Object Variables
		//Bank
		private Integer BANK_CHEST = 26707;
		//Coffer
		private Integer COFFER_ID = 29330;
		//Conveyor Belt
		private Integer CONVEYOR_BELT = 9100;
		//Bar Dispenser
		private Integer BAR_DISPENSER = 9092;
	
	//Item Variables
		//Coins
		private Integer COINS_ID = 995; 
		//Iron Ore
		private Integer IRON_ORE_ID = 440;
		//Coal
		private Integer COAL_ID = 453;
		//Steel Bar
		private Integer STEEL_BAR_ID = 2353;
		// Item to buy from shop
		//private Integer ITEM_TO_BUY = null;
	
	//WorldPoint Variables
		//By Bank
		private WorldPoint BANK_TILE = new WorldPoint(1948, 4957, 0);
		//By Ordan
		private WorldPoint ORDAN_TILE = new WorldPoint(1937, 4966, 0);
		//By Belt
		private WorldPoint BELT_TILE = new WorldPoint(1942, 4967, 0);
		//By Coffer
		private WorldPoint COFFER_TILE = new WorldPoint(1946, 4958, 0);
		//By Dispenser
		private WorldPoint DISPENSER_TILE = new WorldPoint(1940, 4962, 0);
		
	@SuppressWarnings("deprecation")
	
	@Override
	public void paint(Graphics Graphs) {
			upTime = System.currentTimeMillis() - startTime;
			totalxp = ctx.skills.experience(Skills.SMITHING) - startxp;
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
			g.drawString("Total Smithing XP Gained: " + formattedtotalXP, 10, 260);
			g.drawString("Smithing XP per hour: " + formattedhourlyXP, 10, 275);
			if(LVL == 69) {g.drawString("Skill Level: " + LVL + "     ( ͡° ͜ʖ ͡°)", 10, 290);}else {
				g.drawString("Smithing Level: " + LVL, 10, 290);
			}
			//g.drawString("COFFER: " + COFFER_GOLD ,10,305);
			g.drawString("Uptime: " + ctx.paint.formatTime(upTime),10,320);
			g.setColor(Color.WHITE);
			g.drawString("Status: " + STATUS, 10, 335);
			g.setColor(Color.DARK_GRAY);
			g.drawString("V" + version, 178, 335);
			
			if(DEBUG == true) {
				g.setColor(Color.BLACK);
				//g.fillRect(345, 5, 150, 110); //debug black box
				g.setColor(Color.GREEN);
				g.drawString("DEBUG", 350, 20);
				g.setColor(Color.CYAN);
				g.drawString("COFFER_GOLD: [ " + COFFER_GOLD + " ]", 350, 40);
				g.drawString("BF_BAR_DISPENSER: [ " + BF_BAR_DISPENSER + " ]", 350, 50);
				g.drawString("BF_IRON: [ " + BF_IRON + " ]", 350, 60);
				g.drawString("BF_COAL: [ " + BF_COAL + " ]", 350, 70);
				g.drawString("BF_STEEL_BAR: [ " + BF_STEEL_BAR + " ]", 350, 80);
				g.drawString("COINS_IN_INV: [ " + COINS_IN_INV + " ]", 350, 90);
				g.drawString("IRON_ORE_IN_INV: [ " + IRON_ORE_IN_INV + " ]", 350, 100);
				g.drawString("COAL_IN_INV: [ " + COAL_IN_INV + " ]", 350, 110);
				}
	}
	
	@Override
	public void onChatMessage(ChatMessage arg0) {
	
	}
	
	@Override
	public void onExecute() {
		STATUS = "Initializing";
		LVL = ctx.skills.realLevel(Skills.SMITHING);
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void onProcess() {
		//set variables
		//STATUS = "Settings Variables";
		COFFER_GOLD = ctx.getClient().getVar(Varbits.BLAST_FURNACE_COFFER);
		BF_BAR_DISPENSER = ctx.getClient().getVar(Varbits.BAR_DISPENSER);
		BF_IRON = ctx.getClient().getVar(Varbits.BLAST_FURNACE_IRON_ORE);
		BF_COAL = ctx.getClient().getVar(Varbits.BLAST_FURNACE_COAL);
		BF_STEEL_BAR = ctx.getClient().getVar(Varbits.BLAST_FURNACE_STEEL_BAR);
		COINS_IN_INV = ctx.inventory.populate().filter(COINS_ID).population(true); // count the number of coins in the inventory
		IRON_ORE_IN_INV = ctx.inventory.populate().filter(IRON_ORE_ID).population(true);
		COAL_IN_INV = ctx.inventory.populate().filter(COAL_ID).population(true);
		//check if player has money
		if(ctx.inventory.populate().filter(STEEL_BAR_ID).population()>0) {
			Bank();
			
		}else {
			if(COINS_IN_INV > 0) { //player has money
				STATUS = "Player Has Money";
				if(COFFER_GOLD > 0) { //check if money is in coffer
					STATUS = "Money in coffer";
					//check if bars are ready to be picked up
					if(ctx.npcs.populate().filter(COAL_NPC,IRON_NPC).population() >0 || BF_BAR_DISPENSER == 2) { //look for coal / iron on belt
						STATUS="Waiting for belt / furnace";
						ctx.sleep(100,500);
					}else {//else look for coal / iron on belt
						if(BF_BAR_DISPENSER == 3) {
							//if yes pick up bars
							STATUS="Picking up bars";
							if(ctx.getPlayers().getLocal().getLocation().distanceTo(DISPENSER_TILE) < 10){
								//Check if we are idle
								if(ctx.getPlayers().getLocal().getAnimation() == -1) {
									//Find dispenser
									SimpleObject DISPENSER = ctx.objects.populate().filter(BAR_DISPENSER).nearest().next();
									//check if we can interact with the dispenser
						        	if(DISPENSER != null && DISPENSER.validateInteractable()) {
						        		 //Check Dispenser
						        		STATUS = "Clicking Bar Dispenser";
						        		DISPENSER.click(1);
						        		ctx.sleep(750,1500);
						        		ctx.dialogue.clickDialogueOption(1);
						        	} //end dispenser check
								} //end idle check
							}else{//If we are not close to the dispenser, walk to it
								STATUS = "Walking to dispenser";
								ctx.pathing.step(DISPENSER_TILE);
								ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(DISPENSER_TILE) <= 10, 1000);
							}//end close to dispenser check
						}else{//else - no bars to be picked up
							//check if there is enough coal in the furnace
							if(BF_COAL >= 27) {
								STATUS = "Coal in Furnace";
								//check if there is enough iron in the furnace
								if(BF_IRON >=27) {
									//Uhhhhhh this shouldn't happen?
									
								}else{//else check if iron
									//check if iron in inventory
									if(IRON_ORE_IN_INV >0) {
										STATUS = "Adding iron to belt";
										//add iron to belt
										Click_Belt();
									}else {//else - no iron in inventory
										STATUS = "Buying Iron";
										//TODO buy iron
										Buy_Item(IRON_ORE_ID);
									}//end check if iron in inventory
								}//end check if iron in BF
				
							}else{//else check if coal - no coal in furnace
								// check if coal is in inventory
								if(COAL_IN_INV >0) {
									STATUS = "Adding Coal to belt";
									//add coal to belt
									Click_Belt();
								}else{// else - no coal in inventory
									STATUS = "Buying Coal";
									//TODO buy coal
									Buy_Item(COAL_ID);
								}//end check if coal in inv
							}//end check if coal in BF
						}//end check bars to pick up
					}//end look for coal / iron on belt

				}else{// else - no money in coffer
					//check if has enough money for coffer
					if(COINS_IN_INV >= COINS_TO_ADD) {
						STATUS="Can addord Coffer";
						PayCoffer();
					}else {
						STATUS="NOT ENOUGH MONEY FOR COFFER";
						//ctx.stopScript();
					}
					//add money to coffer
					STATUS = "Adding Money to coffer";
					PayCoffer();
				}
			}else{//else - player does not have money
					//stop script
			}//end check if player has money
		}
	}
	
	public void PayCoffer() {
		STATUS = "Paying Coffer";
		if(ctx.getPlayers().getLocal().getLocation().distanceTo(COFFER_TILE) < 50){
			//Find coffer
			STATUS = "Finding Coffer";
			SimpleObject COFFER = ctx.objects.populate().filter(COFFER_ID).nearest().next();
			//check if we can interact with the dispenser
        	if(COFFER != null && COFFER.validateInteractable()) {
        		if(!ctx.dialogue.dialogueOpen() && !ctx.dialogue.pendingInput()) {
	        		STATUS = "Clicking Coffer";
	        		COFFER.click(1);
	        		ctx.sleep(1000,2000);
        		}
        		if(ctx.dialogue.populate().filterContains("Deposit").next() != null) {
        			STATUS = "Found Deposit Option";
        			ctx.dialogue.populate().filterContains("Deposit").next().click();
            		ctx.sleep(1500);
        		}else if(ctx.dialogue.pendingInput()) {//make sure we are typing in coffer box
        			STATUS = "Typing in Coffer";
        			ctx.keyboard.sendKeys(COINS_TO_ADD.toString());
            		ctx.sleep(1000,2000);
    		}
        		
        	}else {
        		STATUS = "Cannot Find Coffer";
        		ctx.sleep(500);
        	}
        		//TODO enter number
        } //end if close check
	}

	public void Bank() {
		STATUS = "Banking";
		//TODO
		//Check if we are idle
		if(ctx.getPlayers().getLocal().getAnimation() == -1) {
			//Find dispenser
			SimpleObject BANK = ctx.objects.populate().filter(BANK_CHEST).nearest().next();
			//check if we can interact with the dispenser
        	if(BANK != null && BANK.validateInteractable()) {
        		 //Check Dispenser
        		if(!ctx.bank.bankOpen()) {
	        		STATUS = "Clicking Bank";
	        		BANK.click("Use");
	        		ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 4000);
        		}
        		ctx.bank.depositAllExcept("COINS");
        		ctx.bank.closeBank();
        	} //end dispenser check
		} //end idle check
	}
	
	public void Click_Belt() {
		STATUS = "Clicking Belt";
		if(ctx.getPlayers().getLocal().getLocation().distanceTo(BELT_TILE) < 10){
			//Check if we are idle
			if(ctx.getPlayers().getLocal().getAnimation() == -1) {
				//Find dispenser
				SimpleObject BELT = ctx.objects.populate().filter(CONVEYOR_BELT).nearest().next();
				//check if we can interact with the dispenser
	        	if(BELT != null && BELT.validateInteractable()) {
	        		 //Check Dispenser
	        		STATUS = "Clicking Conveyor Belt";
	        		BELT.click(1);
	        		ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 4000);
	        	} //end dispenser check
			} //end idle check
		}
	}
	
	public void Buy_Item(Integer ITEM_TO_BUY) {
		if (BF_STEEL_BAR == 0)
		{
			SimpleNpc ORDAN = ctx.npcs.populate().filter(ORDAN_ID).nearest().next();
			if(ORDAN != null && ORDAN.validateInteractable()) {
				if(!ctx.getShop().shopOpen()){
					STATUS = "Trading Odan";
					ORDAN.click("Trade");
				}
				ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 4000);
				ctx.getShop().populate().filter(ITEM_TO_BUY).next().click("Buy 50");
				ctx.getShop().closeShop();
				if(ctx.getInventory().populate().filter(ITEM_TO_BUY).population()>0) {
					ctx.getShop().closeShop();
				}
			}
		}
	}
	
	@Override
	public void onTerminate() {
	STATUS = "terminated";
	}
}