package me.nullicorn.gradientchat;

import java.awt.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GradientChatPlugin extends JavaPlugin implements Listener {

  // The colors used here are arbitrary; you can use whatever ones you want, or create them
  // dynamically while your plugin is running.
  private static final GradientChatTransformer gradient = new GradientChatTransformer(
      new Color(204, 43, 94),
      new Color(117, 58, 136)
  );

  @EventHandler
  private void handleChat(AsyncPlayerChatEvent event) {
    // Apply our gradient to the chat message
    String original = event.getMessage();
    String colorful = gradient.apply(original);

    // Replace the plain message with our colored version.
    event.setMessage(colorful);
  }

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
  }
}
