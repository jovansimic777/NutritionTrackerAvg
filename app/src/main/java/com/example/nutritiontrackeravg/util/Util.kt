package com.example.nutritiontrackeravg.util

import android.util.Patterns

class Util {

    companion object {
        fun isValidEmail(target: CharSequence?): Boolean {
            return if (target == null) {
                false
            }else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }
        fun isValidUsername(username: String?): Boolean {
            return if (username == null) {
                false
            } else {
                username.length >= 3
            }
        }
        fun isValidPassword(password: String?): Boolean {
            return if (password == null || password.length == 0) {
                false
            } else true
            //TODO debug
//        if (password.length() < 5) {
//            return false;
//        }
//        if (password.matches(".*[~#^|$%&*!].*")) {
//            return false;
//        }
//
//        if (!password.matches(".*[A-Z].*")) {
//            return false;
//        }
//
//        if (!password.matches(".*\\d.*")) {
//            return false;
//        }
        }

        fun formatNameToSnakeLowerCase(name: String): String {
            var formattedName = ""
            formattedName = name.lowercase();
            formattedName = formattedName.replace(" ", "_");
            return formattedName
        }
    }
}