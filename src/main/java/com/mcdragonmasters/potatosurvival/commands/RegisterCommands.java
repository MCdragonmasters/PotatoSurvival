package com.mcdragonmasters.potatosurvival.commands;

import com.mcdragonmasters.potatosurvival.commands.homes.HomeCommand;
import com.mcdragonmasters.potatosurvival.commands.homes.SetHomeCommand;
import com.mcdragonmasters.potatosurvival.commands.warps.DeleteWarpCommand;
import com.mcdragonmasters.potatosurvival.commands.warps.RenameWarpCommand;
import com.mcdragonmasters.potatosurvival.commands.warps.SetWarpCommand;
import com.mcdragonmasters.potatosurvival.commands.warps.WarpCommand;

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
    }
}
