package fr.weefle.chestfiller;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Chestfiller extends JavaPlugin {

    ArrayList<Block> blocks = new ArrayList<>();

    @Override
    public void onEnable() {
        ProtectedRegion region = getRegion("test");
        // Plugin startup logic


        double minx = region.getMinimumPoint().getBlockX();
        double miny = region.getMinimumPoint().getBlockY();
        double minz = region.getMinimumPoint().getBlockZ();
        double maxx = region.getMaximumPoint().getBlockX();
        double maxy = region.getMaximumPoint().getBlockY();
        double maxz = region.getMaximumPoint().getBlockZ();
        World world = Bukkit.getWorld("world");
        for (double x = minx; x < maxx; x++) {
            for (double y = miny; y < maxy; y++) {
                for (double z = minz; z < maxz; z++) {
                    Location loc = new Location(world, x, y, z);
                    Block block = world.getBlockAt(loc);
                    blocks.add(block); //TODO: Replace with name of world used on server.
                    if(block.getType().equals(Material.CHEST)){
                        Chest chest = (Chest)block.getState();
                        chest.getBlockInventory().clear();
                        ItemStack[] stacks = new ItemStack[(chest.getBlockInventory().getContents()).length];
                        int i = 0;
                        Random random = new Random();
                        stacks[0] = new ItemStack(Material.EMERALD, random.nextInt(64));
                        stacks[1] = new ItemStack(Material.GOLDEN_APPLE, random.nextInt(64));
                        stacks[2] = new ItemStack(Material.DIAMOND, random.nextInt(64));
                        stacks[3] = new ItemStack(Material.ELYTRA, random.nextInt(64));
                        while (i < chest.getBlockInventory().getContents().length) {
                            int rand = random.nextInt((chest.getInventory().getContents()).length - 1);
                            stacks[rand] = stacks[random.nextInt(stacks.length)];
                            i++;
                        }
                        chest.getInventory().setContents(stacks);
                    }

                }
            }
        }

        }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ProtectedRegion getRegion(String name){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(new BukkitWorld(Bukkit.getWorld("world")));
        if (regions != null) {
            return regions.getRegion(name);
        } else {
            // The world has no region support or region data failed to load
        }

        return regions.getRegion("");
    }
}
