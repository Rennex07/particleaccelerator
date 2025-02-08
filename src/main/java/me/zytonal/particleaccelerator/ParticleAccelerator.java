package me.zytonal.particleaccelerator;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class ParticleAccelerator extends JavaPlugin implements Listener {
    private HashMap<UUID, Integer> dataN;
    private final Random rndX = new Random();

    @Override
    public void onEnable() {
        dataN = new HashMap<>();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent eData) {
        Player pData = eData.getPlayer();
        if (!dataN.containsKey(pData.getUniqueId())) {
            int cVal = rndX.nextInt(8) + 1;
            dataN.put(pData.getUniqueId(), cVal);
        }
        initVortex(pData);
    }

    private void initVortex(Player pData) {
        new BukkitRunnable() {
            double vAngle = 0;
            final double radiusR = 1;

            @Override
            public void run() {
                if (!pData.isOnline()) {
                    cancel();
                    return;
                }

                vAngle += Math.PI/16;
                double xPos = radiusR * Math.cos(vAngle);
                double yPos = vAngle/3;
                double zPos = radiusR * Math.sin(vAngle);
                Location lData = pData.getLocation().add(xPos, yPos, zPos);

                Particle.DustOptions dOpt = new Particle.DustOptions(getColorFromNumber(dataN.get(pData.getUniqueId())), 2.5F);
                pData.getWorld().spawnParticle(Particle.REDSTONE, lData, 1, 0, 0, 0, 0, dOpt);

                if (yPos > 3) {
                    vAngle = 0;
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    private Color getColorFromNumber(int nVal) {
        return switch (nVal) {
            case 1 -> Color.fromRGB(255, 85, 85);
            case 2 -> Color.fromRGB(85, 255, 85);
            case 3 -> Color.fromRGB(85, 255, 255);
            case 4 -> Color.fromRGB(85, 85, 255);
            case 5 -> Color.fromRGB(255, 85, 255);
            case 6 -> Color.fromRGB(255, 255, 85);
            case 7 -> Color.fromRGB(255, 255, 255);
            case 8 -> Color.fromRGB(255, 170, 0);
            default -> Color.WHITE;
        };
    }
}
