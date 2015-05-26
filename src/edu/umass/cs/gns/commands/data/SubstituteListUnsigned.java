/*
 * Copyright (C) 2014
 * University of Massachusetts
 * All Rights Reserved 
 *
 * Initial developer(s): Westy.
 */
package edu.umass.cs.gns.commands.data;

import edu.umass.cs.gns.commands.CommandModule;
import static edu.umass.cs.gns.newApp.clientCommandProcessor.commandSupport.Defs.*;
import edu.umass.cs.gns.newApp.clientCommandProcessor.commandSupport.UpdateOperation;

/**
 *
 * @author westy
 */
public class SubstituteListUnsigned extends AbstractUpdateList {

  public SubstituteListUnsigned(CommandModule module) {
    super(module);
  }

  @Override
  public UpdateOperation getUpdateOperation() {
    return UpdateOperation.SINGLE_FIELD_SUBSTITUTE;
  }

  @Override
  public String getCommandName() {
    return SUBSTITUTELIST;
  }

  @Override
  public String[] getCommandParameters() {
    return new String[]{GUID, FIELD, VALUE, OLDVALUE};
  }

  @Override
  public String getCommandDescription() {
    return "Replaces oldvalue with newvalue in the key value pair for the given GUID. "
            + "Value is a list of items formated as a JSON list."
            + " Field must be world writeable as this command does not specify the writer and is not signed.";
  }
}
