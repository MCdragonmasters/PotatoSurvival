package com.mcdragonmasters.potatosurvival.commands;

import com.mcdragonmasters.potatosurvival.commands.homes.HomeCommand;
import com.mcdragonmasters.potatosurvival.commands.homes.SetHomeCommand;
import com.mcdragonmasters.potatosurvival.commands.warps.*;

public class RegisterCommands {
    public static void register() {
        PvPCommand.register();
        EnderChestCommand.register();

        HomeCommand.register();
        SetHomeCommand.register();

        WarpCommand.register();
        SetWarpCommand.register();
        DeleteWarpCommand.register();
        RenameWarpCommand.register();
        ShareWarpCommand.register();

        //NickCommand.register();
    }
}
