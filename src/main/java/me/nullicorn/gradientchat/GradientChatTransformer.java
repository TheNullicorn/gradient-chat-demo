package me.nullicorn.gradientchat;

import java.awt.Color;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.md_5.bungee.api.ChatColor;

public class GradientChatTransformer implements UnaryOperator<String> {

  /**
   * The color of characters towards the left side of strings given this gradient.
   * <p>
   * This would be the characters closer to index <code>0</code> in a string.
   */
  private final Color left;

  /**
   * The color of characters towards the right side of strings given this gradient.
   * <p>
   * This would be the characters whose indices are closer to <code>String.length()</code> in a
   * string.
   */
  private final Color right;

  public GradientChatTransformer(Color leftColor, Color rightColor) {
    this.left = Objects.requireNonNull(leftColor);
    this.right = Objects.requireNonNull(rightColor);
  }

  /**
   * Determines the value of a single color channel (red, green, or blue) at specific point between
   * the two colors of the gradient.
   * <p>
   * If <code>percent</code> is <code>0.0</code> (0%), the {@link #left} color's value for the
   * channel is returned. If it is <code>1.0</code> (100%), the {@link #right}'s value is returned.
   * If it's anywhere in between, it will be a mix of the two values based on its percentage between
   * them.
   *
   * @param percent       The character's distance from the left-hand side, as a percentage from
   *                      <code>0.0</code> to <code>1.0</code>.
   * @param channelGetter A function that returns a {@link Color color's} value for the desired
   *                      channel, such as {@link Color#getRed()}.
   * @return The value for the channel that should be used when coloring the character.
   */
  private int getChannelValue(float percent, Function<Color, Integer> channelGetter) {
    // Get both colors' values for the channels; e.g. both reds, both greens, or both blues.
    int leftChannel = channelGetter.apply(left);
    int rightChannel = channelGetter.apply(right);

    // Determine how far between the two channels the `percent` puts us.
    float distance = percent * (rightChannel - leftChannel);

    // Offset the `distance` by the first (left) channel's value so that we don't go outside the gradient's range of colors.
    return (int) (leftChannel + distance);
  }

  @Override
  public String apply(String message) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < message.length(); i++) {
      // Check how far the character is into the string.
      float percent = (float) i / (message.length());

      // Get the value of the red, green, and blue channels at our % in the gradient.
      Color charColor = new Color(
          getChannelValue(percent, Color::getRed),
          getChannelValue(percent, Color::getGreen),
          getChannelValue(percent, Color::getBlue)
      );

      // Add the character (prefixed by its new color) to the result.
      // Keep in mind we use net.md_5.bungee.api.ChatColor, not org.bukkit.ChatColor.
      result
          .append(ChatColor.of(charColor))
          .append(message.charAt(i));
    }

    return result.toString();
  }
}
