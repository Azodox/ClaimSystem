package fr.azodox.particle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Particle {

    @NotNull
    private final ParticleType type;
    @Nullable
    private final ParticleData data;

    public Particle(@NotNull ParticleType type) {
        this(type, null);
    }

    public Particle(@NotNull ParticleType type, @Nullable ParticleData data) {
        this.type = Objects.requireNonNull(type, "type");
        this.data = data;
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
