/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.opentransactions.jni.core;

public class TradeListMarket extends Storable {
  private long swigCPtr;

  protected TradeListMarket(long cPtr, boolean cMemoryOwn) {
    super(otapiJNI.TradeListMarket_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TradeListMarket obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        otapiJNI.delete_TradeListMarket(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public long GetTradeDataMarketCount() {
    return otapiJNI.TradeListMarket_GetTradeDataMarketCount(swigCPtr, this);
  }

  public TradeDataMarket GetTradeDataMarket(long nIndex) {
    long cPtr = otapiJNI.TradeListMarket_GetTradeDataMarket(swigCPtr, this, nIndex);
    return (cPtr == 0) ? null : new TradeDataMarket(cPtr, false);
  }

  public boolean RemoveTradeDataMarket(long nIndexTradeDataMarket) {
    return otapiJNI.TradeListMarket_RemoveTradeDataMarket(swigCPtr, this, nIndexTradeDataMarket);
  }

  public boolean AddTradeDataMarket(TradeDataMarket disownObject) {
    return otapiJNI.TradeListMarket_AddTradeDataMarket(swigCPtr, this, TradeDataMarket.getCPtr(disownObject), disownObject);
  }

  public static TradeListMarket ot_dynamic_cast(Storable pObject) {
    long cPtr = otapiJNI.TradeListMarket_ot_dynamic_cast(Storable.getCPtr(pObject), pObject);
    return (cPtr == 0) ? null : new TradeListMarket(cPtr, false);
  }

  public static Storable ot_dynamic_cast_box(TradeListMarket pUnboxed) {
    long cPtr = otapiJNI.TradeListMarket_ot_dynamic_cast_box(TradeListMarket.getCPtr(pUnboxed), pUnboxed);
    return (cPtr == 0) ? null : new Storable(cPtr, false);
  }

}