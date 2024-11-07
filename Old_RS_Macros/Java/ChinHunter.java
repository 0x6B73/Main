import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.robot.script.Script;

@ScriptManifest(author = "Sewer Boi", category = Category.HUNTER, description = "Hunts red/grey Chompers", name = "Chomper Stomper", servers = { "Zenyte" }, version = "1.0", discord = "")

public class ChinHunter extends Script {

private WorldPoint Middle = null;
private WorldPoint North = null;
private WorldPoint East = null;
private WorldPoint South = null;
private WorldPoint West = null;
private Integer totalxp= null;
private Integer startxp= null;
private Integer hourlyxp= null;
private Integer hunterlvl= null;
private Integer maxtraps = null;
private String status = null;
long start = System.currentTimeMillis();


@Override
public void paint(Graphics Graphs) {
Graphics2D g = (Graphics2D) Graphs;
        g.setColor(Color.BLACK);
        g.fillRect(5, 257, 200, 80);
        g.setColor(Color.RED);
        g.drawRect(5, 257, 200, 80);
        g.setColor(Color.YELLOW);
        g.drawString("Chomper Stomper", 7, 272);
        g.setColor(Color.WHITE);
        g.drawString("Total XP: " + totalxp, 7, 287);
        g.drawString("XP per hour: " + hourlyxp, 7, 302);      
        g.drawString("Level: " + hunterlvl + " (" + maxtraps + " traps)", 7, 317);
        g.setColor(Color.GREEN);
        g.drawString("Sewer boi product", 7, 332);
        if (maxtraps == 1) {
        ctx.paint.drawTileMatrix(g, Middle, Color.CYAN);
        } else if (maxtraps ==2) {
        ctx.paint.drawTileMatrix(g, Middle, Color.CYAN);
        ctx.paint.drawTileMatrix(g, West, Color.CYAN);
        } else if (maxtraps ==3) {
        ctx.paint.drawTileMatrix(g, Middle, Color.CYAN);
        ctx.paint.drawTileMatrix(g, West, Color.CYAN);
        ctx.paint.drawTileMatrix(g, East, Color.CYAN);
        } else if (maxtraps ==4) {
        ctx.paint.drawTileMatrix(g, Middle, Color.CYAN);
            ctx.paint.drawTileMatrix(g, North, Color.CYAN);
            ctx.paint.drawTileMatrix(g, East, Color.CYAN);
            ctx.paint.drawTileMatrix(g, West, Color.CYAN);
        } else if (maxtraps ==5) {
        ctx.paint.drawTileMatrix(g, Middle, Color.CYAN);
            ctx.paint.drawTileMatrix(g, North, Color.CYAN);
            ctx.paint.drawTileMatrix(g, East, Color.CYAN);
            ctx.paint.drawTileMatrix(g, South, Color.CYAN);
            ctx.paint.drawTileMatrix(g, West, Color.CYAN);
        }


        totalxp = ctx.skills.experience(Skills.HUNTER) - startxp;
        long end = System.currentTimeMillis();
        float sec = (end - start) / 1000F;
        hourlyxp = (int) ((totalxp / sec) * 3600);
        
}

@Override
public void onChatMessage(ChatMessage arg0) {
// TODO Auto-generated method stub

}

@Override
public void onExecute() {
// TODO Auto-generated method stub
Middle = ctx.players.getLocal().getLocation();
North = new WorldPoint(Middle.getX(), Middle.getY() + 1, Middle.getPlane());
East = new WorldPoint(Middle.getX() + 1, Middle.getY(), Middle.getPlane());
South = new WorldPoint(Middle.getX(), Middle.getY() - 1, Middle.getPlane());
West = new WorldPoint(Middle.getX() - 1, Middle.getY(), Middle.getPlane());
ctx.viewport.pitch(true);
startxp = ctx.skills.experience(Skills.HUNTER);
hunterlvl = ctx.skills.realLevel(Skills.HUNTER);
}

@Override
public void onProcess() {
// TODO Auto-generated method stub

hunterlvl = ctx.skills.realLevel(Skills.HUNTER);
//hunterlvl = 60;

if (hunterlvl < 20) {
maxtraps = 1;
Routine(Middle);
} else if (hunterlvl >= 20 && hunterlvl < 40) {
maxtraps = 2;
Routine(Middle);
Routine(West);
} else if (hunterlvl >= 40 && hunterlvl < 60) {
maxtraps = 3;
Routine(East);
Routine(Middle);
Routine(West);
} else if (hunterlvl >= 60 && hunterlvl < 80) {
maxtraps = 4;
Routine(East);
Routine(Middle);
Routine(West);
Routine(North);
} else if (hunterlvl >= 80) {
maxtraps = 5;
Routine(East);
Routine(Middle);
Routine(West);
Routine(North);
Routine(South);
}

// Routine(East);
// Routine(Middle);
// Routine(West);
// Routine(North);
// Routine(South); 

//final WorldPoint[] Compass = new WorldPoint[]{East,Middle,West,North,South};

// for (WorldPoint worldPoint : Compass) {
// if(ctx.objects.populate().filter(9384,9380,9385).filter(worldPoint).isEmpty() && ctx.groundItems.filter(10008).filter(worldPoint).isEmpty()) {
// ctx.pathing.clickSceneTile(worldPoint, true, true);
// ctx.sleep(1500);
// if (ctx.players.getLocal().getPlayer().getWorldLocation().distanceTo(worldPoint) == 0) {
// ctx.inventory.populate().filter(10008).next().click(0);
// ctx.sleep(3500);
// }
// }
// if(!ctx.objects.populate().filter(9384,9385).filter(worldPoint).isEmpty()) {
// ctx.objects.populate().filter(9384,9385).filter(worldPoint).next().click("Reset");
// ctx.sleep(8000);
// }
// if(!ctx.groundItems.filter(10008).filter(worldPoint).isEmpty()) {
// ctx.objects.populate().filter(10008).filter(worldPoint).next().click("Lay");
// ctx.sleep(4000);
// }
// } 
}

private void Routine(WorldPoint worldPoint) {
//9380 Default
//9382,9383,9390,9391,9393,9392 Closing animated
//9384 Shaking
//9385 Failed
if(status == "terminated") {
//return;
ctx.stopScript();
}

if(ctx.objects.populate().filter(9383,9384,9380,9385).filter(worldPoint).isEmpty()) {
	ctx.sleep(1500); //Sleep is required because the box trap has alot of closing id's cba to find them all.
	if(ctx.objects.populate().filter(9384,9380,9385).filter(worldPoint).isEmpty()) {
		if(ctx.groundItems.filter(10008).filter(worldPoint).isEmpty()) {
			ctx.pathing.clickSceneTile(worldPoint, true, true);
			ctx.sleep(1500);
			if (ctx.players.getLocal().getPlayer().getWorldLocation().distanceTo(worldPoint) == 0) {
				ctx.inventory.populate().filter(10008).next().click(0);
				ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == 5208, 5000);
				ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == -1, 5000); 
			}
		}
	}
}
if(!ctx.objects.populate().filter(9383,9384,9385).filter(worldPoint).isEmpty()) {



boolean success = ctx.objects.populate().filter(9383,9384,9385).filter(worldPoint).next().click(1);
if (success == false) {
return;
}

ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == 5208, 5000);
ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == -1, 5000);
}
if(!ctx.groundItems.filter(10008).filter(worldPoint).isEmpty()) {
boolean success = ctx.groundItems.populate().filter(10008).filter(worldPoint).next().click("Lay");
if (success == false) {
return;
}

ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == 5208, 5000);
ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == -1, 5000);
}
}

@Override
public void onTerminate() {
// TODO Auto-generated method stub
status = "terminated";
}

}