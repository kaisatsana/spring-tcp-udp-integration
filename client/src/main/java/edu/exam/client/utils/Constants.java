package edu.exam.client.utils;

public class Constants {

    // CHANNELS
    public static final String DEFAULT_CHANNEL = "defaultChannel";

    // MESSAGES
    public static final String WELCOME_MESSAGE = "Welcome, %s!";
    public static final String BYE_MESSAGE = "Bye, %s :(";
    public static final String COMMON_UNAUTHORIZED_MESSAGE = "You're not authorized. Please, login before accessing Employee History System.";
    public static final String LOGOUT_UNAUTHORIZED_MESSAGE = "You're not authorized. Please, login before logout.";

    // ERRORS
    public static final String NO_HISTORIES_FOUND_BY_ID_TEMPLATE = "No histories found by id: %d";
    public static final String NO_HISTORIES_FOUND_BY_NAME_TEMPLATE = "No histories found by name: %s";
    public static final String UNEXPECTED_RESPONSE = "Unexpected TCP response";

    // TABLE OUTPUT
    public static final String TABLE_HEADERS_TEMPLATE = "| %-15s | %-15s | %-15s | %-10s | %-10s |\n";
    public static final String EMPLOYEE_COLUMN_NAME = "employee";
    public static final String POSITION_COLUMN_NAME = "position";
    public static final String MANAGER_COLUMN_NAME = "manager";
    public static final String HIRE_COLUMN_NAME = "hire";
    public static final String DISMISS_COLUMN_NAME = "dismiss";
    public static final String MISSING_VALUE = "-";

}
