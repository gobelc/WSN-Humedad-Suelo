package jhumedadsuelos;
/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'EnvioSolicitudMsg'
 * message type.
 */

public class EnvioSolicitudMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 9;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 237;

    /** Create a new EnvioSolicitudMsg of size 9. */
    public EnvioSolicitudMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new EnvioSolicitudMsg of the given data_length. */
    public EnvioSolicitudMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EnvioSolicitudMsg with the given data_length
     * and base offset.
     */
    public EnvioSolicitudMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EnvioSolicitudMsg using the given byte array
     * as backing store.
     */
    public EnvioSolicitudMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EnvioSolicitudMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public EnvioSolicitudMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EnvioSolicitudMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public EnvioSolicitudMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EnvioSolicitudMsg embedded in the given message
     * at the given base offset.
     */
    public EnvioSolicitudMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new EnvioSolicitudMsg embedded in the given message
     * at the given base offset and length.
     */
    public EnvioSolicitudMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <EnvioSolicitudMsg> \n";
      try {
        s += "  [targetId=0x"+Long.toHexString(get_targetId())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [motivo=0x"+Long.toHexString(get_motivo())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [parameters=0x"+Long.toHexString(get_parameters())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [parametersLong=0x"+Long.toHexString(get_parametersLong())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: targetId
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'targetId' is signed (false).
     */
    public static boolean isSigned_targetId() {
        return false;
    }

    /**
     * Return whether the field 'targetId' is an array (false).
     */
    public static boolean isArray_targetId() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'targetId'
     */
    public static int offset_targetId() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'targetId'
     */
    public static int offsetBits_targetId() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'targetId'
     */
    public int get_targetId() {
        return (int)getUIntBEElement(offsetBits_targetId(), 16);
    }

    /**
     * Set the value of the field 'targetId'
     */
    public void set_targetId(int value) {
        setUIntBEElement(offsetBits_targetId(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'targetId'
     */
    public static int size_targetId() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'targetId'
     */
    public static int sizeBits_targetId() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: motivo
    //   Field type: short, unsigned
    //   Offset (bits): 16
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'motivo' is signed (false).
     */
    public static boolean isSigned_motivo() {
        return false;
    }

    /**
     * Return whether the field 'motivo' is an array (false).
     */
    public static boolean isArray_motivo() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'motivo'
     */
    public static int offset_motivo() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'motivo'
     */
    public static int offsetBits_motivo() {
        return 16;
    }

    /**
     * Return the value (as a short) of the field 'motivo'
     */
    public short get_motivo() {
        return (short)getUIntBEElement(offsetBits_motivo(), 8);
    }

    /**
     * Set the value of the field 'motivo'
     */
    public void set_motivo(short value) {
        setUIntBEElement(offsetBits_motivo(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'motivo'
     */
    public static int size_motivo() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'motivo'
     */
    public static int sizeBits_motivo() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: parameters
    //   Field type: int, unsigned
    //   Offset (bits): 24
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'parameters' is signed (false).
     */
    public static boolean isSigned_parameters() {
        return false;
    }

    /**
     * Return whether the field 'parameters' is an array (false).
     */
    public static boolean isArray_parameters() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'parameters'
     */
    public static int offset_parameters() {
        return (24 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'parameters'
     */
    public static int offsetBits_parameters() {
        return 24;
    }

    /**
     * Return the value (as a int) of the field 'parameters'
     */
    public int get_parameters() {
        return (int)getUIntBEElement(offsetBits_parameters(), 16);
    }

    /**
     * Set the value of the field 'parameters'
     */
    public void set_parameters(int value) {
        setUIntBEElement(offsetBits_parameters(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'parameters'
     */
    public static int size_parameters() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'parameters'
     */
    public static int sizeBits_parameters() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: parametersLong
    //   Field type: long, unsigned
    //   Offset (bits): 40
    //   Size (bits): 32
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'parametersLong' is signed (false).
     */
    public static boolean isSigned_parametersLong() {
        return false;
    }

    /**
     * Return whether the field 'parametersLong' is an array (false).
     */
    public static boolean isArray_parametersLong() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'parametersLong'
     */
    public static int offset_parametersLong() {
        return (40 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'parametersLong'
     */
    public static int offsetBits_parametersLong() {
        return 40;
    }

    /**
     * Return the value (as a long) of the field 'parametersLong'
     */
    public long get_parametersLong() {
        return (long)getUIntBEElement(offsetBits_parametersLong(), 32);
    }

    /**
     * Set the value of the field 'parametersLong'
     */
    public void set_parametersLong(long value) {
        setUIntBEElement(offsetBits_parametersLong(), 32, value);
    }

    /**
     * Return the size, in bytes, of the field 'parametersLong'
     */
    public static int size_parametersLong() {
        return (32 / 8);
    }

    /**
     * Return the size, in bits, of the field 'parametersLong'
     */
    public static int sizeBits_parametersLong() {
        return 32;
    }

}
