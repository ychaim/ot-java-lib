/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentransactions.jni.utility;

import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentransactions.jni.core.OTAPI_Basic;
import org.opentransactions.jni.core.Storable;
import org.opentransactions.jni.core.StoredObjectType;
import org.opentransactions.jni.core.StringMap;
import org.opentransactions.jni.core.SwigPasswordCallback;
import org.opentransactions.jni.core.otapi;
import org.opentransactions.jni.core.otapiJNI;

/**
 *
 * @author Cameron Garnham
 */
public class Load {

    private static Load ptrThis = null;

    public synchronized static Load It() {
        if (null == ptrThis) {
            ptrThis = new Load();
        }
        return ptrThis;
    }
    private boolean isNativeLoaded = false;
    private boolean isInitialized = false;
    private boolean isPasswordImageSet = false;
    private boolean isPasswordCallbackSet = false;
    private boolean isWalletLoaded = false;

    public interface IJavaPath {
        String GetJavaPathFromUser(String message);
    }
    
    public interface IPasswordImage {

        String GetPasswordImageFromUser(String message);

        boolean SetPasswordImage(String path);
    }
    
    
    

    // The code here will only ever happen once per instance
    private Load() {
    }

    boolean GetIsLoaded() {
        return isWalletLoaded;
    }

    public void InitNative(IJavaPath javaPathCallback, String optionalPath) throws LoadingOpenTransactionsFailure {
        final Logger l = Logger.getLogger(Load.class.getName());

        String extra_path = optionalPath.isEmpty() ? "" : optionalPath;

        if (isNativeLoaded) {
            throw new LoadingOpenTransactionsFailure("Native Already Loaded!");
        }

        for (;;) {

            try {
                Collection<String> pathsSet = null;

                if (extra_path.isEmpty()) {
                    pathsSet = Tools.appendPathToRuntime(Tools.getDefaultLibPath(Tools.getOS()));
                } else {
                    pathsSet = Tools.appendPathToRuntime(extra_path);
                }

                l.log(Level.INFO, null, pathsSet);

            } catch (IllegalAccessException ex) {
                l.log(Level.SEVERE, null, ex);
                System.exit(-1); // bad excetion
            } catch (NoSuchFieldException ex) {
                l.log(Level.SEVERE, null, ex);
                System.exit(-1); // bad excetion
            }

            try {
                Tools.loadNative(Tools.getOS());
            } catch (UnsatisfiedLinkError ex) {

                l.log(Level.SEVERE, null, ex);
                extra_path = javaPathCallback.GetJavaPathFromUser("Failed To Find OT");
                continue;
            }
            break; //success
        }
        isNativeLoaded = true;
    }

    public void Init() throws LoadingOpenTransactionsFailure {
        final Logger l = Logger.getLogger(Load.class.getName());

        if (!isNativeLoaded) {
            throw new LoadingOpenTransactionsFailure("Native Libs Not Loaded!");
        }
        if (isInitialized) {
            throw new LoadingOpenTransactionsFailure("Is Already Initialized");
        }

        long pAPI = otapiJNI.new_OTAPI_Basic();

        if (0 != pAPI) {
            l.log(Level.INFO, "Output: {0}", Long.toString(pAPI));
            l.log(Level.INFO, "Load.initOTAPI: SUCCESS invoking OT_API_Init()");

        } else // Failed in OT_API_Init().
        {
            throw new LoadingOpenTransactionsFailure("calling new_OTAPI_Basic() failed");
        }
        isInitialized = true;
    }

    public void SetupPasswordImage(IPasswordImage passwordImage) throws LoadingOpenTransactionsFailure {

        if (!isInitialized) {
            throw new LoadingOpenTransactionsFailure("Is Not Initialized");
        }
        if (isPasswordImageSet) {
            throw new LoadingOpenTransactionsFailure("Password Image Already Set!");
        }

        String imagePath = "";
        boolean bHaveImage = false;

        // First Lets Check if we already have a password image. (we don't need annother one)
        if (otapi.Exists("moneychanger", "settings.dat")) {

            Storable storable = null;
            StringMap stringMap = null;

            storable = otapi.QueryObject(StoredObjectType.STORED_OBJ_STRING_MAP, "moneychanger", "settings.dat");

            if (null != storable) {

                stringMap = StringMap.ot_dynamic_cast(storable);
                imagePath = stringMap.GetValue("ImagePath");

                File f = new File(imagePath);
                if (f.exists()) {
                    // Good we have a password Image
                    bHaveImage = true;
                }
            }
        }

        // We don't have an image... lets get it from the user, then save it.
        if (!bHaveImage) {

            for (;;) {
                imagePath = passwordImage.GetPasswordImageFromUser("passwordImage");

                File f = new File(imagePath);
                if (f.exists()) {
                    // Good we have a password Image
                    break;
                }
            }

            StringMap stringMap = null;  // we are about to create this object
            Storable storable = otapi.CreateObject(StoredObjectType.STORED_OBJ_STRING_MAP);

            if (storable != null) {
                stringMap = StringMap.ot_dynamic_cast(storable);
                System.out.println("stringMap:" + stringMap);

                if (stringMap != null) {
                    //stringMap.SetValue("ImagePath", "~/.ot/default.gif");
                    stringMap.SetValue("ImagePath", imagePath);
                    bHaveImage = otapi.StoreObject(stringMap, "moneychanger", "settings.dat");
                }
            }
        }

        if (bHaveImage) {
            passwordImage.SetPasswordImage(imagePath);
        } else {
            throw new LoadingOpenTransactionsFailure("Password image not Set!");
        }
        isPasswordImageSet = true;
    }

    public void SetupPasswordCallback(SwigPasswordCallback passwordCallback) throws LoadingOpenTransactionsFailure {

        if (!isPasswordImageSet) {
            throw new LoadingOpenTransactionsFailure("Must Set Password Image First!");
        }
        if (isPasswordCallbackSet) {
            throw new LoadingOpenTransactionsFailure("Already Have Set Password Callback!");
        }

        if (null == passwordCallback) {
            throw new IllegalArgumentException("passwordCallback is null");
        }

        if (!SwigPasswordCallback.SetCallback(passwordCallback)) {
            throw new LoadingOpenTransactionsFailure("Unable to Set Password Callback");
        }

        isPasswordCallbackSet = true;
    }

    public void LoadWallet() throws LoadingOpenTransactionsFailure {

        if (!isPasswordCallbackSet) {
            throw new LoadingOpenTransactionsFailure("Must Set Password Callback First!");
        }
        if (isWalletLoaded) {
            throw new LoadingOpenTransactionsFailure("Already Loaded!");
        }

        if (!OTAPI_Basic.LoadWallet()) {
            throw new LoadingOpenTransactionsFailure("Unable to Load Wallet");
        }

        isWalletLoaded = true;
    }

    static public class LoadingOpenTransactionsFailure extends Exception {

        private final String _message;

        public LoadingOpenTransactionsFailure(final String message) {
            _message = message;
        }

        public final String getError() {
            return _message;
        }
    }
}