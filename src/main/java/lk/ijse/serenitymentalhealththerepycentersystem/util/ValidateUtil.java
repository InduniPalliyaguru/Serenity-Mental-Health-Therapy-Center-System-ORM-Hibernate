package lk.ijse.serenitymentalhealththerepycentersystem.util;

//import com.jfoenix.controls.JFXPasswordField;
//import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.util.regex.Pattern;

public class ValidateUtil {

    private static final Pattern NAME_PATTERN = Pattern.compile("^(Mr\\.|Mrs\\.|Ms\\.|Dr\\.)?\\s?[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[A-Za-z0-9\\s,./-]{5,100}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    // Sri Lankan mobile phone pattern (e.g., 071-1234567, 0711234567, +94711234567)
    private static final Pattern SL_MOBILE_PATTERN = Pattern.compile("^(?:0|\\+94)7[01245678][0-9]{7}$");
    // Sri Lankan landline pattern (e.g., 011-1234567, 0111234567, +94111234567)
    private static final Pattern SL_LANDLINE_PATTERN = Pattern.compile("^(?:0|\\+94)[123456789][0-9]{8}$");
    // International phone pattern (general format)
    private static final Pattern INTERNATIONAL_PHONE_PATTERN = Pattern.compile("^\\+[1-9][0-9]{1,14}$");
    // Default phone pattern (for backward compatibility)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?:0|\\+94)7[01245678][0-9]{7}$");
    private static final Pattern TEXT_PATTERN = Pattern.compile("^[A-Za-z0-9.,!?\\s'-]{1,300}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("(?i)\\b(0[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM)\\b");


    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("^P\\d{3}$");
    private static final Pattern THERAPY_SESSION_ID_PATTERN = Pattern.compile("^TS\\d{3}$");
    private static final Pattern THERAPY_PROGRAM_ID_PATTERN = Pattern.compile("^TP\\d{3}$");
    private static final Pattern THERAPIST_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^U\\d{3}$");
    private static final Pattern PAYMENT_ID_PATTERN = Pattern.compile("^PAY-\\d{4}$");

    private static final Pattern FEES_PATTERN = Pattern.compile("^[1-9]\\d{0,6}(\\.\\d{2})?$"); // e.g., 10000.00 or 20000
    private static final Pattern DURATION_PATTERN = Pattern.compile(
            "^\\d+\\s+(day|days|week|weeks|month|months|year|years)$", Pattern.CASE_INSENSITIVE
    );

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



    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidSLMobileNumber(String phone) {
        return phone != null && SL_MOBILE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidSLLandlineNumber(String phone) {
        return phone != null && SL_LANDLINE_PATTERN.matcher(phone).matches();
    }


    public static boolean isValidInternationalNumber(String phone) {
        return phone != null && INTERNATIONAL_PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidPhoneNumberAny(String phone) {
        if (phone == null) return false;
        return isValidSLMobileNumber(phone) || 
               isValidSLLandlineNumber(phone) || 
               isValidInternationalNumber(phone);
    }

    public static boolean isValidText(String text) {
        return text != null && TEXT_PATTERN.matcher(text).matches();
    }

    public static boolean isValidTime(String time) {
        return time != null && TIME_PATTERN.matcher(time).matches();
    }

    public static boolean isValidFee(String fee) {
        return fee != null && FEES_PATTERN.matcher(fee).matches();
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && DURATION_PATTERN.matcher(duration.trim()).matches();
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


//    public static void setFocusColorForJFXTextField(JFXTextField field, boolean isValid) {
//        if (field == null) return;
//        if (!isValid) {
//            field.setFocusColor(Paint.valueOf("red"));
//        } else {
//            field.setFocusColor(Paint.valueOf("#4059a9")); // Default JFXTextField focus color
//        }
//    }
//
//
//    public static void setFocusColorForJFXPasswordField(JFXPasswordField field, boolean isValid) {
//        if (field == null) return;
//        if (!isValid) {
//            field.setFocusColor(Paint.valueOf("red"));
//        } else {
//            field.setFocusColor(Paint.valueOf("#4059a9")); // Default JFXPasswordField focus color
//        }
//    }
//
//
//    public static void setFocusColorForTextField(TextField field, boolean isValid) {
//        if (field == null) return;
//        if (!isValid) {
//            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
//        } else {
//            field.setStyle("-fx-border-color: transparent;");
//        }
//    }
//
//    public static void setFocusColorForTextArea(TextArea field, boolean isValid) {
//        if (field == null) return;
//        if (!isValid) {
//            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
//        } else {
//            field.setStyle("-fx-border-color: transparent;");
//        }
//    }
//
//
//    public static void setFocusColorForDatePicker(DatePicker field, boolean isValid) {
//        if (field == null) return;
//        if (!isValid) {
//            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
//        } else {
//            field.setStyle("-fx-border-color: transparent;");
//        }
//    }

}
