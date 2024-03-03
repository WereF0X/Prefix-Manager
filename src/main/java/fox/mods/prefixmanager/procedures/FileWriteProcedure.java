package fox.mods.prefixmanager.procedures;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import fox.mods.prefixmanager.network.PrefixManagerModVariables;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

@Mod.EventBusSubscriber
public class FileWriteProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		File config = new File("");
		com.google.gson.JsonObject mainJsonObj = new com.google.gson.JsonObject();
		com.google.gson.JsonObject subJsonObj = new com.google.gson.JsonObject();
		if (PrefixManagerModVariables.MapVariables.get(world).createdFile == false) {
			PrefixManagerModVariables.MapVariables.get(world).createdFile = true;
			PrefixManagerModVariables.MapVariables.get(world).syncData(world);
			config = new File((FMLPaths.GAMEDIR.get().toString() + "/mods/prefix-manager"), File.separator + "config.json");
			if (!config.exists()) {
				try {
					config.getParentFile().mkdirs();
					config.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			mainJsonObj.addProperty("is-username-colored", false);
			mainJsonObj.add("subJsonObject", subJsonObj);
			{
				Gson mainGSONBuilderVariable = new GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(config);
					fileWriter.write(mainGSONBuilderVariable.toJson(mainJsonObj));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
