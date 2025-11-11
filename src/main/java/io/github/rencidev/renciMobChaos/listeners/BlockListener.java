package io.github.rencidev.renciMobChaos.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlockListener implements Listener {

    JavaPlugin plugin;
    public BlockListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Random rng = new Random();
        List<Material> blocks = List.<Material>of(Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE);
        Block block = event.getBlock();

        String spawnMobMessage = plugin.getConfig().getString("messages.mob-spawn");
        int chance = plugin.getConfig().getInt("spawn-chance") / 100;
        List<EntityType> mobs = plugin.getConfig().getStringList("mobs").stream()
                .map(str -> {
                    try {
                        return EntityType.valueOf(str.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Unkown mob!");
                        return null;
                    }
                })
                .filter(type -> type != null && type.isAlive() && type.isSpawnable())
                .toList();
        EntityType mob = mobs.get(rng.nextInt(mobs.size()));

        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if (blocks.contains(block.getType())) {

                if (rng.nextDouble() >= chance) {

                    int radius = plugin.getConfig().getInt("radius");
                    double offsetX = rng.nextInt(radius * 2 + 1) - radius;
                    double offsetZ = rng.nextInt(radius * 2 + 1) - radius;
                    Location spawnLoc = block.getLocation().clone()
                            .add(0.5 + offsetX, 1, 0.5 + offsetZ);

                    block.getWorld().spawnEntity(spawnLoc, mob);

                    block.getWorld().spawnParticle(Particle.EXPLOSION, spawnLoc.add(0.5, 0.5, 0.5), 25);
                    block.getWorld().playSound(block.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 0.6f, 1.8f);

                    // minimessage
                    var componentMobSpawn = MiniMessage.miniMessage().deserialize(
                            Objects.requireNonNull(spawnMobMessage),
                            Placeholder.unparsed("mob", mob.name().toLowerCase())
                    );

                    event.getPlayer().sendMessage(componentMobSpawn);
                }

            }
        }
    }

}
