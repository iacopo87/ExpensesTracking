package pazzaglia.it.expensestracking.shared;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IO on 01/10/2016.
 */

public class Validator {

    private static void setErrorMessage(boolean isValid, TextView textView, String errorMessage){
        if (!isValid) {
            textView.setError(errorMessage);
        } else {
            textView.setError(null);
        }
    }

    public static boolean isPasswordValid(String s, TextView t){
        boolean isValid = !(s.isEmpty() || s.length() < 4 || s.length() > 10);
        setErrorMessage(isValid, t, "Between 4 and 10 alphanumeric characters");
        return isValid;
    }


    public static boolean isEmailValid(String email, TextView txtEmail){
        boolean isValid = !(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        setErrorMessage(isValid, txtEmail, "Enter a valid email address");
        return isValid;
    }

    public static boolean isStringValid(String name, TextView txtName){
        boolean isValid = !(name.isEmpty() ||name.length() < 3);
        setErrorMessage(isValid, txtName, "At least three characters");
        return isValid;
    }

    public static boolean isAmountValid(String amount, TextView txtAmount){
        boolean isValid = !(amount == null || amount.isEmpty());
        String errorString = "Invalid amount";
        setErrorMessage(isValid, txtAmount,errorString);
        try {
            Double.parseDouble(amount);
        } catch (Exception e){
            setErrorMessage(false, txtAmount,errorString);
            isValid = false;
        }
        return isValid;
    }

    public static boolean isDateValid(String date, TextView txtDate){
        boolean isValid = true;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setLenient(false);
        try {
            Date d = df.parse(date);
        } catch (ParseException e) {
            setErrorMessage(false, txtDate,"Invalid date. The format is yyyy-MM-dd HH:mm:ss");
            isValid = false;
        }
        return isValid;
    }
}
