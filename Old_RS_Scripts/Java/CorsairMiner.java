
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.queries.SimplePlayerQuery;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.hooks.wrappers.SimplePlayer;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "Sewer Boi", category = Category.MONEYMAKING, description = "Mines Runite/Adamantite rocks in the Corsair cove.", discord = "", name = "Corsair Miner", servers = { "Zenyte" }, version = "0.1")

public class CorsairMiner extends Script{

	WorldArea CORSAIR_MINE_AREA = new WorldArea(new WorldPoint(1944, 9023, 1), new WorldPoint(1931, 9000, 1));
	WorldArea EDGEVILLE_BANK_AREA = new WorldArea(new WorldPoint(3075, 3508, 0), new WorldPoint(3107, 3181, 0));
	
	int ZENYTE_PORTAL_ID = 35000;
	SimpleObject ZENYTE_PORTAL;
	WorldPoint ZENYTE_PORTAL_LOCATION = new WorldPoint(3096, 3501, 0);
	
	int BANKER_ID[] = {100029,10030};
	SimpleNpc BANKER;
	
	int RUNITE_ROCK_ID = 7461;
	SimpleObject RUNITE_ROCK;
	
	int ADAMANTITE_ROCK_ID = 7493;
	SimpleObject ADAMANTITE_ROCK;
	
	int CURRENT_MINING_XP;
	int START_MINING_XP;
	int MINED_RUNITE_ORE;
	int MINED_ADAMANTITE_ORE;
	
	int RUNE_PROFIT;
	int ADAMANT_PROFIT;
	
	int COAL_PRICE = 67;
	int NATURE_RUNE_PRICE = 230;
	
	int RUNE_PLATELEGS_ACLH_PRICE = 38400;
	int ADAMANT_PLATEBODY_ALCH_PRICE = 9488;
	
	int TEMP_HOLDER;
	
	private int mX, mY;
	private long angle;
	private BasicStroke cursorStroke = new BasicStroke(2);
	private Color cursorColor = Color.WHITE;
	private AffineTransform oldTransform;
	
	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
	
	private long startTime, upTime;
	
	 private final RenderingHints antialiasing = new RenderingHints(
	            RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	    private Image getImage(String url) {
	        try {
	            return ImageIO.read(new URL(url));
	        } catch(IOException e) {
	            return null;
	        }
	    }


	  private final Image img1 = getImage("https://i.imgur.com/N2tHw9Y.png");
	
	@Override
	public void paint(Graphics Graphs) {
		// TODO Auto-generated method stub
		Graphics2D Graph = (Graphics2D) Graphs;	
		upTime = System.currentTimeMillis() - startTime;
		CURRENT_MINING_XP = ctx.skills.experience(Skills.MINING);
		
		RUNE_PROFIT = (RUNE_PLATELEGS_ACLH_PRICE / 3) - ((COAL_PRICE * 4) + (NATURE_RUNE_PRICE / 5));
		ADAMANT_PROFIT = (ADAMANT_PLATEBODY_ALCH_PRICE / 5) - ((COAL_PRICE * 3) + NATURE_RUNE_PRICE);
		
		Graph.setColor(Color.WHITE);
		Graph.setFont(new Font("default", Font.PLAIN, 12));		
		
		Graph.setRenderingHints(antialiasing);
		Graph.drawImage(img1, -1, -2, null);
		Graph.drawString("Runite ore: " + MINED_RUNITE_ORE + " (" + ctx.paint.valuePerHour(MINED_RUNITE_ORE, startTime) + ")", 140, 265);
		Graph.drawString("Adamantite ore: " + MINED_ADAMANTITE_ORE + " (" + ctx.paint.valuePerHour(MINED_ADAMANTITE_ORE, startTime) + ")", 140, 295);
		Graph.drawString("Mining XP: " + runescapeFormat(CURRENT_MINING_XP-START_MINING_XP) + " (" + runescapeFormat(ctx.paint.valuePerHour(CURRENT_MINING_XP-START_MINING_XP, startTime)) + ")", 140, 325);
		
		Graph.drawString("Rune profit: " + runescapeFormat(MINED_RUNITE_ORE*RUNE_PROFIT) + " (" + runescapeFormat(ctx.paint.valuePerHour((MINED_RUNITE_ORE*RUNE_PROFIT), startTime)) + ")", 335, 265);
		Graph.drawString("Adamant profit: " + runescapeFormat(MINED_ADAMANTITE_ORE*ADAMANT_PROFIT) + " (" + runescapeFormat(ctx.paint.valuePerHour((MINED_ADAMANTITE_ORE*ADAMANT_PROFIT), startTime)) + ")", 335, 295);
		
		Graph.drawString("Total profit: " + runescapeFormat((MINED_ADAMANTITE_ORE*ADAMANT_PROFIT) + (MINED_RUNITE_ORE*RUNE_PROFIT)) + " (" + runescapeFormat(ctx.paint.valuePerHour(((MINED_ADAMANTITE_ORE*ADAMANT_PROFIT) + (MINED_RUNITE_ORE*RUNE_PROFIT)), startTime)) + ")", 335, 325);
		Graph.setColor(Color.BLACK);
		Graph.drawString(ctx.paint.formatTime(upTime), 460, 471);
		
		Graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		
		Graphics2D g = (Graphics2D) Graph;    //replace g1 with Graphics variable name
		oldTransform = g.getTransform();
		mX = ctx.mouse.getBotPosAwt().x;
		mY = ctx.mouse.getBotPosAwt().y;

		g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		//MOUSE TRAIL
		while (!mousePath.isEmpty() && mousePath.peek().isUp())

			mousePath.remove();
		Point clientCursor = ctx.mouse.getBotPosAwt();
		MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y, 2000);
		if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
			mousePath.add(mpp);
		MousePathPoint lastPoint = null;
		for (MousePathPoint a : mousePath) {
			if (lastPoint != null) {
				g.setColor(new Color(255, 255, 255, a.getAlpha()));    //trail color
				g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
			}
			lastPoint = a;
				}

		if (mX != -1) {
			g.setStroke(cursorStroke);
			g.setColor(cursorColor);
			g.drawLine(mX-3, mY-3, mX+2, mY+2);
			g.drawLine(mX-3, mY+2, mX+2, mY-3);

			g.rotate(Math.toRadians(angle+=6), mX, mY);

			g.draw(new Arc2D.Double(mX-12, mY-12, 24, 24, 330, 60, Arc2D.OPEN));
			g.draw(new Arc2D.Double(mX-12, mY-12, 24, 24, 151, 60, Arc2D.OPEN));

		        g.setTransform(oldTransform);
		}
		
			Graph.setColor(Color.GREEN);
			//Graph.setColor(Color.CYAN);


			int i = 40;
			SimplePlayerQuery<SimplePlayer> staff = ctx.antiBan.nearbyStaff().populate();
			for (SimplePlayer simplePlayer : staff) {
				Graph.drawString(simplePlayer.getName() , 10, i);
				i = i + 20;
			}
		
		
		Graph.setStroke(new BasicStroke(0f));
	}

	@Override
	public void onChatMessage(ChatMessage arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExecute() {
		// TODO Auto-generated method stub
		START_MINING_XP = ctx.skills.experience(Skills.MINING);
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onProcess() {
		// TODO Auto-generated method stub
		//ctx.updateStatus("CORSAIR_MINE_AREA");
		if(ctx.pathing.inArea(CORSAIR_MINE_AREA)) {
			//ctx.updateStatus("INVENT NOT FULL && NOT ANIMATING");
			if(!ctx.inventory.inventoryFull() && !ctx.players.getLocal().isAnimating()) {
				//ctx.updateStatus("GRABBING RUNITE ROCK");
				RUNITE_ROCK = ctx.objects.populate().filter(RUNITE_ROCK_ID).nearest().next();
				
				//ctx.updateStatus("CHECKING RUNITE ROCK");
				if(RUNITE_ROCK != null && RUNITE_ROCK.validateInteractable()) {
					//ctx.updateStatus("CLICK RUNITE ROCK");
					TEMP_HOLDER = ctx.inventory.populate().filter("Runite ore").population();
					if(RUNITE_ROCK.click("Mine")) {
						//ctx.updateStatus("START WAIT CONDITION RUNITE ROCK");
						ctx.onCondition(() -> ctx.players.getLocal().isAnimating(), 5000);
						ctx.onCondition(() -> !ctx.players.getLocal().isAnimating(), 20000);
						MINED_RUNITE_ORE = MINED_RUNITE_ORE + (ctx.inventory.populate().filter("Runite ore").population() - TEMP_HOLDER);
						//ctx.updateStatus("END WAIT CONDITION RUNITE ROCK");
					}
				}
				//ctx.updateStatus("IF RUNE ROCK IS NULL");
				if(RUNITE_ROCK == null) {
					//ctx.updateStatus("GRABBING ADDY ROCK");
					ADAMANTITE_ROCK = ctx.objects.populate().filter(CORSAIR_MINE_AREA).filter(ADAMANTITE_ROCK_ID).nearest().next();
					//ctx.updateStatus("CHECKING ADDY ROCK");
					if(ADAMANTITE_ROCK != null && ADAMANTITE_ROCK.validateInteractable()) {
						//ctx.updateStatus("MINE ADDY ROCK");
						TEMP_HOLDER = ctx.inventory.populate().filter("Adamantite ore").population();
						if(ADAMANTITE_ROCK.click("Mine")) {
							//ctx.updateStatus("START WAIT ADDY ROCK");
							ctx.onCondition(() -> ctx.players.getLocal().isAnimating(), 5000);
							ctx.onCondition(() -> !ctx.players.getLocal().isAnimating(), 20000);

								MINED_ADAMANTITE_ORE = MINED_ADAMANTITE_ORE + (ctx.inventory.populate().filter("Adamantite ore").population() - TEMP_HOLDER);
							
							//ctx.updateStatus("END WAIT ADDY ROCK");
						}
					}
				}
			}
			//ctx.updateStatus("IF INVENTORY FULL");
			if(ctx.inventory.inventoryFull()) {
				if(ctx.inventory.populate().filter("Zenyte home teleport").next().click(0)) {
					ctx.onCondition(() -> ctx.pathing.inArea(EDGEVILLE_BANK_AREA), 5000);
				}
			}
		}
		//ctx.updateStatus("IF IN EDGEVILLE AREA");
		if(ctx.pathing.inArea(EDGEVILLE_BANK_AREA)) {
			//ctx.updateStatus("IF IN EDGEVILLE AREA TRUE");
			if(ctx.inventory.inventoryFull()) {
				if(!ctx.bank.bankOpen()) {
					//ctx.updateStatus("FIND BANKER");
					BANKER = ctx.npcs.populate().filter(BANKER_ID).nearest().next();
					
					if(BANKER != null && BANKER.validateInteractable()) {
						if(BANKER.click("Bank")) {
							ctx.onCondition(() -> ctx.bank.bankOpen(), 5000);
						}
					}
				}
				if(ctx.bank.bankOpen() ) {
					ctx.bank.depositAllExcept("Zenyte home teleport");
					ctx.bank.closeBank();
				}
			}
			
			if(!ctx.inventory.inventoryFull()) {
				ZENYTE_PORTAL = ctx.objects.populate().filter(ZENYTE_PORTAL_ID).nearest().next();
				
				if(ctx.players.getLocal().distanceTo(ZENYTE_PORTAL) > 5) {
					ctx.pathing.step(ZENYTE_PORTAL_LOCATION);
				} else {
					if(ZENYTE_PORTAL.click("Teleport-previous")) {
						ctx.onCondition(() -> ctx.pathing.inArea(CORSAIR_MINE_AREA), 5000);
					}
				}
				
			}
		}
		//ctx.updateStatus("END SCRIPT");
		//ctx.updateStatus("___________");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		
	}
	
	public static String runescapeFormat(Integer number) {
		String[] suffix = new String[] { "K", "M", "B", "T" };
		int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
		if (size >= 3) {
			while (size % 3 != 0) {
				size = size - 1;
			}
		}
		return (size >= 3) ? +(Math.round((number / Math.pow(10, size)) * 10) / 10d)
				+ suffix[(size / 3) - 1]
				: +number + "";
	}

	class MousePathPoint extends Point {

		private long finishTime;
		private double lastingTime;
		private int alpha = 255;

		public MousePathPoint(int x, int y, int lastingTime) {
			super(x, y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}

	        //added by Swizzbeat
		public int getAlpha() {
			int newAlpha = ((int) ((finishTime - System.currentTimeMillis()) / (lastingTime / alpha)));
			if (newAlpha > 255)
				newAlpha = 255;
			if (newAlpha < 0)
				newAlpha = 0;
			return newAlpha;
		}

		public boolean isUp() {
			return System.currentTimeMillis() >= finishTime;
		}
	}

}
