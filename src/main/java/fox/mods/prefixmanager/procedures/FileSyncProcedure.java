package fox.mods.prefixmanager.procedures;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import fox.mods.prefixmanager.network.PrefixManagerModVariables;

import com.google.gson.Gson;

@Mod.EventBusSubscriber
public class FileSyncProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		com.google.gson.JsonObject SubObj = new com.google.gson.JsonObject();
		File config = new File("");
		if (PrefixManagerModVariables.MapVariables.get(world).createdFile == true) {
			config = new File((FMLPaths.GAMEDIR.get().toString() + "/mods/prefix-manager"), File.separator + "config.json");
			if (config.exists()) {
				{
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(config));
						StringBuilder jsonstringbuilder = new StringBuilder();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							jsonstringbuilder.append(line);
						}
						bufferedReader.close();
						SubObj = new Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
						SubObj = SubObj.get("subJsonObject").getAsJsonObject();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			PrefixManagerModVariables.MapVariables.get(world).prefixColorAppliesToUsername = SubObj.get("is-username-colored").getAsBoolean();
			PrefixManagerModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
