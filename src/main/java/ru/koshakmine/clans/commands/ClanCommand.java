package ru.koshakmine.clans.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.koshakmine.clans.Clans;

public class ClanCommand extends Command {

    private Clans main;

    public ClanCommand(Clans main) {
        super("clan");
        this.setAliases(new String[]{"c"});
        this.main = main;
    }
    
    @Override
    public boolean execute(CommandSender sender, String labelm, String[] args) {
        if (sender instanceof Player player) {
            main.getForms().sendMainForm(player);
        } else {
            sender.sendMessage("Только в игре!");
        }
        return true;
    }
}
