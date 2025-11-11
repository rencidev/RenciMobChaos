package io.github.rencidev.renciMobChaos;

import io.github.rencidev.renciMobChaos.listeners.BlockListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RenciMobChaos extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getLogger().info("RenciMobChaos enabled!");

    }

    @Override
    public void onDisable() {

        getLogger().info("RenciMobChaos disabled!");

    }
}
