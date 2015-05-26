package edu.umass.cs.gns.nsdesign.packet;

import edu.umass.cs.gns.newApp.clientCommandProcessor.commandSupport.Defs;
import edu.umass.cs.gns.nio.JSONNIOTransport;
import edu.umass.cs.gns.nsdesign.packet.Packet.PacketType;
import edu.umass.cs.gns.reconfiguration.InterfaceRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Packet format sent from a client and handled by a local name server.
 *
 */
public class CommandPacket extends BasicPacket implements InterfaceRequest {

  private final static String CLIENTREQUESTID = "reqID";
  private final static String LNSREQUESTID = "LNSreqID";
  private final static String SENDERADDRESS = JSONNIOTransport.DEFAULT_IP_FIELD;
  private final static String SENDERPORT = JSONNIOTransport.DEFAULT_PORT_FIELD;
  private final static String COMMAND = "command";
  
  public final static String BOGUS_SERVICE_NAME = "unknown";

  /**
   * Identifier of the request.
   */
  private final int clientRequestId;
  /**
   * LNS identifier - filled in at the LNS.
   */
  private int LNSRequestId;
  /**
   * The IP address of the sender as a string
   */
  private final String senderAddress;
  /**
   * The TCP port of the sender as an int
   */
  private final int senderPort;
  /**
   * The JSON form of the command. Always includes a COMMANDNAME field.
   * Almost always has a GUID field or NAME (for HRN records) field.
   */
  private final JSONObject command;

  /**
   *
   * @param requestId
   * @param command
   */
  public CommandPacket(int requestId, String senderAddress, int senderPort, JSONObject command) {
    this.setType(PacketType.COMMAND);
    this.clientRequestId = requestId;
    this.LNSRequestId = -1; // this will be filled in at the LNS
    this.senderAddress = senderAddress;
    this.senderPort = senderPort;
    this.command = command;
  }

  public CommandPacket(JSONObject json) throws JSONException {
    this.type = Packet.getPacketType(json);
    this.clientRequestId = json.getInt(CLIENTREQUESTID);
    if (json.has(LNSREQUESTID)) {
     this.LNSRequestId = json.getInt(LNSREQUESTID);
    } else {
     this.LNSRequestId = -1;
    }
    this.senderAddress = json.getString(SENDERADDRESS);
    this.senderPort = json.getInt(SENDERPORT);
    this.command = json.getJSONObject(COMMAND);
  }

  /**
   * Converts the command object into a JSONObject.
   *
   * @return
   * @throws org.json.JSONException
   */
  @Override
  public JSONObject toJSONObject() throws JSONException {
    JSONObject json = new JSONObject();
    Packet.putPacketType(json, getType());
    json.put(CLIENTREQUESTID, this.clientRequestId);
    if (this.LNSRequestId != -1) {
      json.put(LNSREQUESTID, this.LNSRequestId);
    }
    json.put(COMMAND, this.command);
    json.put(SENDERADDRESS, this.senderAddress);
    json.put(SENDERPORT, this.senderPort);
    return json;
  }

  public int getClientRequestId() {
    return clientRequestId;
  }
  
  public int getLNSRequestId() {
    return LNSRequestId;
  }

  public void setLNSRequestId(int LNSRequestId) {
    this.LNSRequestId = LNSRequestId;
  }

  public String getSenderAddress() {
    return senderAddress;
  }

  public int getSenderPort() {
    return senderPort;
  }

  public JSONObject getCommand() {
    return command;
  }

  @Override
  public String getServiceName() {
    try {
      if (command != null) {
        if (command.has(Defs.GUID)) {
          return command.getString(Defs.GUID);
        }
        if (command.has(Defs.NAME)) {
          return command.getString(Defs.NAME);
        }
      }
    } catch (JSONException e) {
      // Just ignore it
    }
    return BOGUS_SERVICE_NAME;
  }
  
  public String getCommandName() {
    try {
      if (command != null) {
        if (command.has(Defs.COMMANDNAME)) {
          return command.getString(Defs.COMMANDNAME);
        }
      }
    } catch (JSONException e) {
      // Just ignore it
    }
    return "unknown";
  }
}
