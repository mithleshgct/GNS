package edu.umass.cs.gns.packet;

import edu.umass.cs.gns.nameserver.NameRecordKey;
import edu.umass.cs.gns.nameserver.ValuesMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 **
 * Packet transmitted between the local name server and a name server. All communications inside of the domain protocol are carried
 * in a single DNS packet. The packet contains the query from a local name server and a response from the name server.
 *
 *
 */
public class DNSPacket extends BasicPacket {

  public final static String HEADER = "header1";
//  public final static String QTYPE = "qtype";
  public final static String QRECORDKEY = "qrecordkey";
  public final static String QNAME = "qname";
  public final static String TIME_TO_LIVE = "ttlAddress";
  public final static String FIELD_VALUE = "fieldValue";
  public final static String RECORD_VALUE = "recordValue";
//  public final static String PRIMARY_NAME_SERVERS = "Primary";
//  public final static String ACTIVE_NAME_SERVERS = "Active";
  public final static String LNS_ID = "lnsId";
  /**
   * Packet header *
   */
  private Header header;
  /**
   * Name in the query *
   */
  private String qname;
  /**
   * The key of the value key pair.
   */
  private final NameRecordKey qrecordKey;
  /**
   * Time interval (in seconds) that the resource record may be cached before it should be discarded
   */
  private int ttl;
  /**
   * The field value being returned *
   */
//  private ArrayList<String> fieldValue;
  /**
   * The value that is getting sent back to the client. MOST OF THE TIME THIS WILL HAVE JUST A SINGLE KEY/VALUE, but sometimes we
   * return the entire record. When it's a single key/value the key will be the same as the qrecordKey.
   */
  private ValuesMap recordValue;
//  /**
//   * A list of primary name servers from the name *
//   */
//  private HashSet<Integer> primaryNameServers;
//  /**
//   * A list of active name servers for the name *
//   */
//  private Set<Integer> activeNameServers;
  /**
   * Used by traffic status *
   */
  private int lnsId = -1;

//  /**
//   **
//   * Constructs a packet for querying a name server for name information.
//   *
//   * @param header Packet header
//   * @param qname Host name in the query
//  
//   */
//  public DNSPacket(Header header, String qname, NameRecordKey recordKey) {
//    this.header = header;
//    this.qrecordKey = recordKey;
//    this.qname = qname;
//    this.lnsId = -1;
//  }
  public DNSPacket(Header header, String qname, NameRecordKey recordKey, int sender) {
    this.header = header;
    this.qrecordKey = recordKey;
    this.qname = qname;
    this.lnsId = sender;
  }

  /**
   **
   * Constructs a packet from a JSONObject that represents a DNS packet
   *
   * @param json JSONObject that represents a DNS packet
   * @throws JSONException
   */
  public DNSPacket(JSONObject json) throws JSONException {
    this.header = new Header(json.getJSONObject(HEADER));
    this.qrecordKey = NameRecordKey.valueOf(json.getString(QRECORDKEY));
    this.qname = json.getString(QNAME);

    if (header.getQr() == DNSRecordType.RESPONSE && header.getRcode() != DNSRecordType.RCODE_ERROR) {
      this.ttl = json.getInt(TIME_TO_LIVE);
//      this.primaryNameServers = (HashSet<Integer>) toSetInteger(json.getJSONArray(PRIMARY_NAME_SERVERS));
//      this.activeNameServers = toSetInteger(json.getJSONArray(ACTIVE_NAME_SERVERS));
      //this.fieldValue = new ArrayList<String>(JSONUtils.JSONArrayToSetString(json.getJSONArray(FIELD_VALUE)));
      if (json.has(RECORD_VALUE))
        this.recordValue = new ValuesMap(json.getJSONObject(RECORD_VALUE));


    }
    this.lnsId = json.getInt(LNS_ID);
  }

  public DNSPacket(int id, String name, NameRecordKey key, ArrayList<String> fieldValue, int TTL) {
    this.header = new Header(id, DNSRecordType.RESPONSE, DNSRecordType.RCODE_NO_ERROR);
    this.qname = name;
    this.qrecordKey = key;
    //this.fieldValue = fieldValue;
    // recordValue is only used to return an +ALL+ field value
    this.recordValue = new ValuesMap();
    this.recordValue.put(key.getName(), fieldValue);
    this.ttl = TTL;

//    this.primaryNameServers = entry.getPrimaryNameServer();
//    this.activeNameServers = entry.getActiveNameServers();

    this.lnsId = -1;
  }

  public DNSPacket(int id, String name, NameRecordKey key, ValuesMap entireRecord, int TTL) {
    this.header = new Header(id, DNSRecordType.RESPONSE, DNSRecordType.RCODE_NO_ERROR);
    this.qname = name;
    this.qrecordKey = key;
    //this.fieldValue = fieldValue;
    this.recordValue = entireRecord;
    this.ttl = TTL;
//    this.primaryNameServers = entry.getPrimaryNameServer();
//    this.activeNameServers = entry.getActiveNameServers();
    this.lnsId = -1;
  }

  /**
   **
   * Converts this packet's query section to a JSONObject
   *
   * @return JSONObject that represents a DNS packet's query section
   * @throws JSONException
   */
  public JSONObject toJSONObjectQuestion() throws JSONException {
    JSONObject json = new JSONObject();

    json.put(HEADER, getHeader().toJSONObject());
    json.put(QRECORDKEY, getQrecordKey().getName());
    json.put(QNAME, getQname());
    json.put(LNS_ID, lnsId);

    return json;
  }

  /**
   **
   * Converts this packet's response section to a JSONObject
   *
   * @return JSONObject that represents a DNS packet's response section
   * @throws JSONException
   */
  @Override
  public JSONObject toJSONObject() throws JSONException {
    JSONObject json = new JSONObject();

    json.put(HEADER, getHeader().toJSONObject());
    json.put(QRECORDKEY, getQrecordKey().getName());
    json.put(QNAME, getQname());
    json.put(TIME_TO_LIVE, getTTL());
//    if (fieldValue != null) {
//      json.put(FIELD_VALUE, new JSONArray(fieldValue));
//    }
    if (recordValue != null)
	      json.put(RECORD_VALUE, recordValue.toJSONObject());
    
//    json.put(PRIMARY_NAME_SERVERS, new JSONArray(getPrimaryNameServers()));
//    json.put(ACTIVE_NAME_SERVERS, new JSONArray(getActiveNameServers()));
    json.put(LNS_ID, lnsId);
    return json;
  }

  /**
   *
   * Returns true if the packet is a query, false otherwise
   */
  public boolean isQuery() {
    return getHeader().getQr() == DNSRecordType.QUERY;
  }

  /**
   * Returns true if the packet is a response, false otherwise
   */
  public boolean isResponse() {
    return getHeader().getQr() == DNSRecordType.RESPONSE;
  }

  /**
   **
   * Returns true if the packet contains a response error, false otherwise
   *
   */
  public boolean containsAnyError() {
    return getHeader().getRcode() == DNSRecordType.RCODE_ERROR
            || getHeader().getRcode() == DNSRecordType.RCODE_ERROR_INVALID_ACTIVE_NAMESERVER;
  }

  public boolean containsInvalidActiveNSError() {
    return getHeader().getRcode() == DNSRecordType.RCODE_ERROR_INVALID_ACTIVE_NAMESERVER;
  }

  /**
   **
   * Returns the ID for this query from the packet header. Used by the requester to match up replies to outstanding queries
   *
   */
  public int getQueryId() {
    return getHeader().getId();
  }

  @Override
  public String toString() {
    try {
      return isQuery() ? toJSONObjectQuestion().toString() : toJSONObject().toString();
    } catch (JSONException e) {
      return "DNSPacket{" + "header=" + getHeader() + ", qname=" + getQname() + ", qrecordKey=" + getQrecordKey() + '}';
    }
  }

//  public boolean isActiveNameServerListEmpty() {
//    return getActiveNameServers() == null || getActiveNameServers().isEmpty();
//  }
  /**
   * @return the header
   */
  public Header getHeader() {
    return header;
  }

  /**
   * @param header the header to set
   */
  public void setHeader(Header header) {
    this.header = header;
  }

  /**
   * @return the qname
   */
  public String getQname() {
    return qname;
  }

  public void setQname(String name) {
    this.qname = name;
  }

  /**
   * @return the qrecordKey
   */
  public NameRecordKey getQrecordKey() {
    return qrecordKey;
  }

  /**
   * @return the ttlAddress
   */
  public int getTTL() {
    return ttl;
  }

  /**
   * @param ttlAddress the ttlAddress to set
   */
  public void setTTL(int ttlAddress) {
    this.ttl = ttlAddress;
  }

//  /**
//   * @return the rdata
//   */
//  public ArrayList<String> getFieldValue() {
//    return fieldValue;
//  }
//
  /**
   *
   * @return
   */
  public ValuesMap getRecordValue() {
    return recordValue;
  }

  /**
   *
   * @param recordValue
   */
  public void setRecordValue(ValuesMap recordValue) {
    this.recordValue = recordValue;
  }

  /**
   * @param data the data to set
   */
  public void setSingleReturnValue(ArrayList<String> data) {
    if (this.recordValue == null) {
      this.recordValue = new ValuesMap();
    }
    this.recordValue.put(qrecordKey.getName(), data);
  }

  public int getLnsId() {
    return lnsId;
  }

  public void setLnsId(int lnsId) {
    this.lnsId = lnsId;
  }
//  /**
//   * @return the primaryNameServers
//   */
//  public HashSet<Integer> getPrimaryNameServers() {
//    return primaryNameServers;
//  }
//  /**
//   * @param primaryNameServers the primaryNameServers to set
//   */
//  public void setPrimaryNameServers(HashSet<Integer> primaryNameServers) {
//    this.primaryNameServers = primaryNameServers;
//  }
//  /**
//   * @return the activeNameServers
//   */
//  public Set<Integer> getActiveNameServers() {
//    return activeNameServers;
//  }
//  /**
//   * @param activeNameServers the activeNameServers to set
//   */
//  public void setActiveNameServers(Set<Integer> activeNameServers) {
//    this.activeNameServers = activeNameServers;
//  }

  public static void main(String[] args) throws JSONException {
    long t1 = System.currentTimeMillis();
    Header header = new Header(100000, DNSRecordType.QUERY, DNSRecordType.RCODE_NO_ERROR);
    long t2 = System.currentTimeMillis();
    DNSPacket pkt = new DNSPacket(header, "name1", NameRecordKey.EdgeRecord, -1);
    long t3 = System.currentTimeMillis();
    JSONObject pktJson = pkt.toJSONObjectQuestion();
    long t4 = System.currentTimeMillis();
    pkt = new DNSPacket(pktJson);
    long t5 = System.currentTimeMillis();

    Set<Integer> active = new HashSet<Integer>();
    active.add(1);
    active.add(2);
    active.add(3);
    List<Integer> primary = new ArrayList<Integer>();
    primary.add(10);
    primary.add(11);
    primary.add(12);
    ArrayList<String> rdata = new ArrayList<String>();
    rdata.add("2545435345");

    long t6 = System.currentTimeMillis();
//    pkt.setActiveNameServers(active);

//    pkt.setPrimaryNameServers(new HashSet<Integer>(primary));

    ValuesMap record = new ValuesMap();
    record.put("FRED", rdata);
    pkt.setRecordValue(record);

    pkt.setTTL(10);
    long t7 = System.currentTimeMillis();
    pktJson = pkt.toJSONObject();
    long t8 = System.currentTimeMillis();
    pkt = new DNSPacket(pktJson);
    long t9 = System.currentTimeMillis();

    System.out.println("header:" + (t2 - t1) + "ms");
    System.out.println("pkt:" + (t3 - t2) + "ms");
    System.out.println("pktToJson:" + (t4 - t3) + "ms");
    System.out.println("pktFromJson:" + (t5 - t4) + "ms");
    System.out.println("Generate data:" + (t6 - t5) + "ms");
    System.out.println("updatePkt:" + (t7 - t6) + "ms");
    System.out.println("respomsePktToJson:" + (t8 - t7) + "ms");
    System.out.println("jsonToPkt:" + (t9 - t8) + "ms");


    Header h = new Header(100000, DNSRecordType.RESPONSE, DNSRecordType.RCODE_ERROR_INVALID_ACTIVE_NAMESERVER);
    DNSPacket p = new DNSPacket(h, "name1", NameRecordKey.EdgeRecord, -1);
//    p.setRecordValue(record);
    JSONObject json = p.toJSONObject();
    System.out.println(json.toString());
    p = new DNSPacket(json);
    System.out.println(p.toJSONObjectQuestion().toString());
    System.out.println(p.toJSONObjectQuestion().toString());
//    System.out.println(p.getActiveNameServers().toString());
//    p.setActiveNameServers(new HashSet<Integer>());
//    p.getActiveNameServers().add(1);
    json = p.toJSONObject();
    System.out.println(json.toString());
    p = new DNSPacket(json);
    System.out.println(p.toJSONObjectQuestion().toString());
    System.out.println(p.toJSONObjectQuestion().toString());
  }
}
