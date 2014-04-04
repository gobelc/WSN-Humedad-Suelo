package jhumedadsuelos;
/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.

* This class implements a Java interface to the 'DatosMsg'
 * message type.
 */

public class DatosMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 18;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 236;

    /** Create a new DatosMsg of size 18. */
    public DatosMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new DatosMsg of the given data_length. */
    public DatosMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DatosMsg with the given data_length
     * and base offset.
     */
    public DatosMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DatosMsg using the given byte array
     * as backing store.
     */
    public DatosMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DatosMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public DatosMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DatosMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public DatosMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DatosMsg embedded in the given message
     * at the given base offset.
     */
    public DatosMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DatosMsg embedded in the given message
     * at the given base offset and length.
     */
    public DatosMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <DatosMsg> \n";
      try {
        s += "  [source=0x"+Long.toHexString(get_source())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [seqno=0x"+Long.toHexString(get_seqno())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [data=0x"+Long.toHexString(get_data())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [hora=0x"+Long.toHexString(get_hora())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [carga=0x"+Long.toHexString(get_carga())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [sensor=0x"+Long.toHexString(get_sensor())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [metric=0x"+Long.toHexString(get_metric())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [parent=0x"+Long.toHexString(get_parent())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [reply=0x"+Long.toHexString(get_reply())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: source
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'source' is signed (false).
     */
    public static boolean isSigned_source() {
        return false;
    }

    /**
     * Return whether the field 'source' is an array (false).
     */
    public static boolean isArray_source() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'source'
     */
    public static int offset_source() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'source'
     */
    public static int offsetBits_source() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'source'
     */
    public int get_source() {
        return (int)getUIntBEElement(offsetBits_source(), 16);
    }

    /**
     * Set the value of the field 'source'
     */
    public void set_source(int value) {
        setUIntBEElement(offsetBits_source(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'source'
     */
    public static int size_source() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'source'
     */
    public static int sizeBits_source() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: seqno
    //   Field type: int, unsigned
    //   Offset (bits): 16
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'seqno' is signed (false).
     */
    public static boolean isSigned_seqno() {
        return false;
    }

    /**
     * Return whether the field 'seqno' is an array (false).
     */
    public static boolean isArray_seqno() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'seqno'
     */
    public static int offset_seqno() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'seqno'
     */
    public static int offsetBits_seqno() {
        return 16;
    }

    /**
     * Return the value (as a int) of the field 'seqno'
     */
    public int get_seqno() {
        return (int)getUIntBEElement(offsetBits_seqno(), 16);
    }

    /**
     * Set the value of the field 'seqno'
     */
    public void set_seqno(int value) {
        setUIntBEElement(offsetBits_seqno(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'seqno'
     */
    public static int size_seqno() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'seqno'
     */
    public static int sizeBits_seqno() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: data
    //   Field type: int, unsigned
    //   Offset (bits): 32
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'data' is signed (false).
     */
    public static boolean isSigned_data() {
        return false;
    }

    /**
     * Return whether the field 'data' is an array (false).
     */
    public static boolean isArray_data() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'data'
     */
    public static int offset_data() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'data'
     */
    public static int offsetBits_data() {
        return 32;
    }

    /**
     * Return the value (as a int) of the field 'data'
     */
    public int get_data() {
        return (int)getUIntBEElement(offsetBits_data(), 16);
    }

    /**
     * Set the value of the field 'data'
     */
    public void set_data(int value) {
        setUIntBEElement(offsetBits_data(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'data'
     */
    public static int size_data() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'data'
     */
    public static int sizeBits_data() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: hora
    //   Field type: long, unsigned
    //   Offset (bits): 48
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'hora' is signed (false).
     */
    public static boolean isSigned_hora() {
        return false;
    }

    /**
     * Return whether the field 'hora' is an array (false).
     */
    public static boolean isArray_hora() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'hora'
     */
    public static int offset_hora() {
        return (48 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'hora'
     */
    public static int offsetBits_hora() {
        return 48;
    }

    /**
     * Return the value (as a long) of the field 'hora'
     */
    public long get_hora() {
        return (long)getUIntBEElement(offsetBits_hora(), 32);
    }

    /**
     * Set the value of the field 'hora'
     */
    public void set_hora(long value) {
        setUIntBEElement(offsetBits_hora(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'hora'
     */
    public static int size_hora() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'hora'
     */
    public static int sizeBits_hora() {
        return 32;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: carga
    //   Field type: int, unsigned
    //   Offset (bits): 80
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'carga' is signed (false).
     */
    public static boolean isSigned_carga() {
        return false;
    }

    /**
     * Return whether the field 'carga' is an array (false).
     */
    public static boolean isArray_carga() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'carga'
     */
    public static int offset_carga() {
        return (80 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'carga'
     */
    public static int offsetBits_carga() {
        return 80;
    }

    /**
     * Return the value (as a int) of the field 'carga'
     */
    public int get_carga() {
        return (int)getUIntBEElement(offsetBits_carga(), 16);
    }

    /**
     * Set the value of the field 'carga'
     */
    public void set_carga(int value) {
        setUIntBEElement(offsetBits_carga(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'carga'
     */
    public static int size_carga() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'carga'
     */
    public static int sizeBits_carga() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: sensor
    //   Field type: short, unsigned
    //   Offset (bits): 96
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'sensor' is signed (false).
     */
    public static boolean isSigned_sensor() {
        return false;
    }

    /**
     * Return whether the field 'sensor' is an array (false).
     */
    public static boolean isArray_sensor() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'sensor'
     */
    public static int offset_sensor() {
        return (96 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'sensor'
     */
    public static int offsetBits_sensor() {
        return 96;
    }

    /**
     * Return the value (as a short) of the field 'sensor'
     */
    public short get_sensor() {
        return (short)getUIntBEElement(offsetBits_sensor(), 8);
    }

    /**
     * Set the value of the field 'sensor'
     */
    public void set_sensor(short value) {
        setUIntBEElement(offsetBits_sensor(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'sensor'
     */
    public static int size_sensor() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'sensor'
     */
    public static int sizeBits_sensor() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: metric
    //   Field type: int, unsigned
    //   Offset (bits): 104
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'metric' is signed (false).
     */
    public static boolean isSigned_metric() {
        return false;
    }

    /**
     * Return whether the field 'metric' is an array (false).
     */
    public static boolean isArray_metric() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'metric'
     */
    public static int offset_metric() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'metric'
     */
    public static int offsetBits_metric() {
        return 104;
    }

    /**
     * Return the value (as a int) of the field 'metric'
     */
    public int get_metric() {
        return (int)getUIntBEElement(offsetBits_metric(), 16);
    }

    /**
     * Set the value of the field 'metric'
     */
    public void set_metric(int value) {
        setUIntBEElement(offsetBits_metric(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'metric'
     */
    public static int size_metric() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'metric'
     */
    public static int sizeBits_metric() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: parent
    //   Field type: int, unsigned
    //   Offset (bits): 120
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'parent' is signed (false).
     */
    public static boolean isSigned_parent() {
        return false;
    }

    /**
     * Return whether the field 'parent' is an array (false).
     */
    public static boolean isArray_parent() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'parent'
     */
    public static int offset_parent() {
        return (120 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'parent'
     */
    public static int offsetBits_parent() {
        return 120;
    }

    /**
     * Return the value (as a int) of the field 'parent'
     */
    public int get_parent() {
        return (int)getUIntBEElement(offsetBits_parent(), 16);
    }

    /**
     * Set the value of the field 'parent'
     */
    public void set_parent(int value) {
        setUIntBEElement(offsetBits_parent(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'parent'
     */
    public static int size_parent() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'parent'
     */
    public static int sizeBits_parent() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: reply
    //   Field type: short, unsigned
    //   Offset (bits): 136
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'reply' is signed (false).
     */
    public static boolean isSigned_reply() {
        return false;
    }

    /**
     * Return whether the field 'reply' is an array (false).
     */
    public static boolean isArray_reply() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'reply'
     */
    public static int offset_reply() {
        return (136 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'reply'
     */
    public static int offsetBits_reply() {
        return 136;
    }

    /**
     * Return the value (as a short) of the field 'reply'
     */
    public short get_reply() {
        return (short)getUIntBEElement(offsetBits_reply(), 8);
    }

    /**
     * Set the value of the field 'reply'
     */
    public void set_reply(short value) {
        setUIntBEElement(offsetBits_reply(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'reply'
     */
    public static int size_reply() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'reply'
     */
    public static int sizeBits_reply() {
        return 8;
    }

}
