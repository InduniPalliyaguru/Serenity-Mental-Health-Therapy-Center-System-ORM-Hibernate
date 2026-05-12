package lk.ijse.serenitymentalhealththerepycentersystem.util;

public class KeepUserIDUtil {

    private static KeepUserIDUtil instance;
    private String currentUserId;

    private KeepUserIDUtil() {}

    public static synchronized KeepUserIDUtil getInstance() {
        if (instance == null) {
            instance = new KeepUserIDUtil();
        }
        return instance;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void clear() {
        this.currentUserId = null;
    }

}
