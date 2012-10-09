/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.opentransactions.jni.core;

public final class PackType {
  public final static PackType PACK_MESSAGE_PACK = new PackType("PACK_MESSAGE_PACK", otapiJNI.PACK_MESSAGE_PACK_get());
  public final static PackType PACK_PROTOCOL_BUFFERS = new PackType("PACK_PROTOCOL_BUFFERS");
  public final static PackType PACK_TYPE_ERROR = new PackType("PACK_TYPE_ERROR");

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static PackType swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + PackType.class + " with value " + swigValue);
  }

  private PackType(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private PackType(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private PackType(String swigName, PackType swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static PackType[] swigValues = { PACK_MESSAGE_PACK, PACK_PROTOCOL_BUFFERS, PACK_TYPE_ERROR };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}
