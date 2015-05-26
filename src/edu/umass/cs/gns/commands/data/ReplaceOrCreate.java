/*
 * Copyright (C) 2014
 * University of Massachusetts
 * All Rights Reserved 
 *
 * Initial developer(s): Westy.
 */
package edu.umass.cs.gns.commands.data;

import edu.umass.cs.gns.commands.CommandModule;
import edu.umass.cs.gns.newApp.clientCommandProcessor.commandSupport.UpdateOperation;
import static edu.umass.cs.gns.newApp.clientCommandProcessor.commandSupport.Defs.*;

/**
 *
 * @author westy
 */
public class ReplaceOrCreate extends AbstractUpdate {

  public ReplaceOrCreate(CommandModule module) {
    super(module);
  }

  @Override
  public UpdateOperation getUpdateOperation() {
    return UpdateOperation.SINGLE_FIELD_REPLACE_ALL_OR_CREATE;
  }

  @Override
  public String getCommandName() {
    return REPLACEORCREATE;
  }

  @Override
  public String[] getCommandParameters() {
    return new String[]{GUID, FIELD, VALUE, WRITER, SIGNATURE, SIGNATUREFULLMESSAGE};
  }

  @Override
  public String getCommandDescription() {
    return "Adds a key value pair to the GNS for the given GUID if it doesn not exist otherwise "
            + "replaces the value of this key value pair for the given GUID."
             + " Field must be writeable by the WRITER guid.";         
  }
}
