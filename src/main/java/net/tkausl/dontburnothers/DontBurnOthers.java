package net.tkausl.dontburnothers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DontBurnOthers extends JavaPlugin implements Listener {

    int distSquared;
    String message;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.distSquared = getConfig().getInt("range") * getConfig().getInt("range");
        this.message = getConfig().getString("message");
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getMaterial() != Material.FLINT_AND_STEEL && event.getMaterial() != Material.LAVA_BUCKET) {
            return;
        }
        if (event.getPlayer().hasPermission("dontburnothers.ignore")) {
            return;
        }
        Location block = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
        Player player = event.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p == player || p.getWorld() != player.getWorld()) {
                continue;
            }
            double dist = block.distanceSquared(p.getLocation());
            if (dist < distSquared) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.getConfig().getString("message").replace("&", "§").replace("%other%", p.getDisplayName()));
            }
        }
    }
}
