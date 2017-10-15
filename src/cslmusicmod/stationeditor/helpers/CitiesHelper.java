package cslmusicmod.stationeditor.helpers;

import org.apache.commons.lang3.SystemUtils;

import java.nio.file.Paths;

public final class CitiesHelper {

    private CitiesHelper() {

    }

    public static String getLocalModFolder() {
        if(SystemUtils.IS_OS_LINUX) {
            return SystemUtils.getUserHome().toPath().resolve(".local/share/Colossal Order/Cities_Skylines/Addons/Mods").toString();
        }
        else if(SystemUtils.IS_OS_WINDOWS) {
            String dataFolder = System.getenv("LOCALAPPDATA");
            return Paths.get(dataFolder).resolve("Colossal Order\\Cities_Skylines\\Addons\\Mods").toString();
        }
        else if(SystemUtils.IS_OS_MAC_OSX) {
            return SystemUtils.getUserHome().toPath().resolve("Library/Application Support/Colossal Order/Cities_Skylines/Addons/Mods").toString();
        }
        else {
            return "";
        }
    }

}
