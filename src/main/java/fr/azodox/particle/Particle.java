package fr.azodox.particle;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Particle {

    @NotNull
    private final ParticleType type;
    @Nullable
    private final Player player;
    @Nullable
    private final ParticleData data;

    public Particle(@NotNull ParticleType type) {
        this(null, type, null);
    }

    public Particle(@Nullable Player player, @NotNull ParticleType type, @Nullable ParticleData data) {
        this.player = player;
        this.type = Objects.requireNonNull(type, "type");
        this.data = data;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public ParticleType getType() {
        return type;
    }

    @Nullable
    public ParticleData getData() {
        return data;
    }
}
