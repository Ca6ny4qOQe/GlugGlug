package gg.whirl.gluggglug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class GlugGlugConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("gluggglug.json");

    private static GlugGlugConfig instance;

    public boolean clickTotemToOffhand = false;
    public boolean clickTotemToDoubleHand = false;
    public int doubleHandSlot = 0;
    public boolean blockInvUntotem = true;
    public boolean blockSwapUntotem = false;

    public static GlugGlugConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static GlugGlugConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                instance = GSON.fromJson(reader, GlugGlugConfig.class);
                if (instance == null) {
                    instance = new GlugGlugConfig();
                }
                return instance;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        instance = new GlugGlugConfig();
        instance.save();
        return instance;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
