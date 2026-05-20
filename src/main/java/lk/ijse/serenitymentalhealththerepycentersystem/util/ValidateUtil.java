package lk.ijse.serenitymentalhealththerepycentersystem.util;

import java.util.regex.Pattern;

public class ValidateUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("(?i)\\b(0[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM)\\b");

    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("^P\\d{3}$");
    private static final Pattern THERAPY_SESSION_ID_PATTERN = Pattern.compile("^TS\\d{3}$");
    private static final Pattern THERAPY_PROGRAM_ID_PATTERN = Pattern.compile("^MT[0-9]{4}$");
    private static final Pattern THERAPIST_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^U\\d{3}$");
    private static final Pattern PAYMENT_ID_PATTERN = Pattern.compile("^PAY-\\d{4}$");

    public static boolean isValidId(String id, String type) {
        if (id == null || type == null) return false;

        Pattern pattern;
        switch (type.toUpperCase()) {
            case "PATIENT":
                pattern = PATIENT_ID_PATTERN;
                break;
            case "THERAPY_SESSION":
                pattern = THERAPY_SESSION_ID_PATTERN;
                break;
            case "THERAPY_PROGRAM":
                pattern = THERAPY_PROGRAM_ID_PATTERN;
                break;
            case "THERAPIST":
                pattern = THERAPIST_ID_PATTERN;
                break;
            case "USER":
                pattern = USER_ID_PATTERN;
                break;
            case "PAYMENT":
                pattern = PAYMENT_ID_PATTERN;
                break;
            default:
                return false;
        }
        return pattern.matcher(id).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidTime(String time) {
        return time != null && TIME_PATTERN.matcher(time).matches();
    }

    public static boolean isRequiredField(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public static boolean areRequiredFields(String... fields) {
        if (fields == null) return false;
        for (String field : fields) {
            if (!isRequiredField(field)) {
                return false;
            }
        }
        return true;
    }


}
