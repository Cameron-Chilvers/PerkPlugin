package perkplugin.perkplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigUtil {
    private File file;
    private FileConfiguration config;

    public ConfigUtil(Plugin plugin, String path){
        this(plugin.getDataFolder().getAbsolutePath() + '/' + path);
    }

    public ConfigUtil(String path){
        this.file = new File(path);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean save(){
        try{
            this.config.save(this.file);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void deleteFile(){
        if (file.exists()) {
            if (file.delete()) {
                Bukkit.getLogger().info(file.getName() + " inventory deleted successfully.");
            } else {
                Bukkit.getLogger().info(file.getName() + " Failed to Delete.");
            }
        } else {
            Bukkit.getLogger().info(file.getName() + " Does Not exist.");
        }
    }


    public File getFile(){
        return this.file;
    }

    public FileConfiguration getConfig(){
        return this.config;
    }
}