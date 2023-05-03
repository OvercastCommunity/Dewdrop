package tc.oc.occ.dewdrop.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.Dependency;
import tc.oc.occ.dewdrop.matches.DewdropMatchManager;

public class TestCommand extends BaseCommand {

  @Dependency private DewdropMatchManager matchManager;

}
