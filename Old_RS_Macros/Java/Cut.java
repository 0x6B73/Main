import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;


import java.awt.*;

import net.runelite.api.coords.WorldPoint;

@ScriptManifest(author = "Snek", name = "[TEST] Cutter", category = Category.WOODCUTTING, version = "0.02", description = "Cuts Willows at Seers", discord = "Snek#7707", servers = {"Zenyte" })
public class Cut extends Script {
	
	private final WorldPoint BANK_TILE = new WorldPoint(2724, 3493, 0); // The bank tile we want to step to for banking
	private final WorldPoint TREE_TILE = new WorldPoint(2730, 3500, 0);
	
	
	static State state;
	
	public enum State {
		WALKING,
		TALKING,
		BANKING,
		CUTTING,
		SEARCHING,
		UNDEFINED
    }
	
	
	@Override
	public void onExecute() {
	}
    
    @Override
    public void onProcess() {
    	
    	
    	if(!ctx.inventory.inventoryFull()){ //check if inventory is not full
    		if(ctx.getPlayers().getLocal().getLocation().distanceTo(TREE_TILE) < 10){ //Check if we are close to the trees
	    		if(ctx.getPlayers().getLocal().getAnimation() == -1) { //check if we are idle
	    			SimpleObject tree = ctx.objects.populate().filter("Maple Tree").nearest().next();
	    			state = State.SEARCHING;
	            	if(tree != null && tree.validateInteractable()) { //check if there is a tree that we can interact with
	            		tree.click("chop down");
	            		state = State.CUTTING;
	            		ctx.sleep(1000);
	            		ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);
	            	} //end tree check
	    		} //end idle check
	    	}else {//walk to trees
	    		ctx.pathing.step(TREE_TILE);
    			ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(TREE_TILE) <= 10, 15000);
	    	}
    	}
    	else { //inv full
    		if(ctx.getPlayers().getLocal().getLocation().distanceTo(BANK_TILE) > 3) { //check if we are far from bank
    			state = State.WALKING;
    			ctx.pathing.step(BANK_TILE);
    			ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(BANK_TILE) <= 3, 5000);
    		}else { //close to bank
    			//interact with banker
    			SimpleNpc bank = ctx.getNpcs().populate().filter("Banker").nearest().next();
    			if(bank != null && bank.validateInteractable()) { //check if we can interact with the banker
    				state = State.TALKING;
    				bank.click("Bank");
    				ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);
    			} // end check if interact with banker
    		} // end close to bank
    		if(ctx.getBank().bankOpen()){
    			state = State.BANKING;
    			ctx.getBank().depositInventory();
    			ctx.getBank().depositAllExcept("Axe");
    			ctx.sleepCondition(() -> ctx.getInventory().populate().isEmpty(), 5000);
    			ctx.getBank().closeBank();
    		}
    	}
    }
    
    @Override
    public void onTerminate() {

    }

    @Override
    public void onChatMessage(ChatMessage chatMessage) {

    }
    
    private State getState() {
    	return state; 
    }

    @Override
    public void paint(Graphics g) {
    	g.setColor(Color.CYAN);
    	g.drawString("State:" + getState(), 12, 90);
    }
}