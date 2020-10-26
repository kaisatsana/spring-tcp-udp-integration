package edu.exam.udp.utils;

public class Constants {

    // UDP SERVER CONFIGURATION CONSTANTS
    public static final String EMPLOYEE_ADAPTER_ID = "employee";
    public static final String EMPLOYEE_SOCKET_EXPRESSION = "@employee.socket";
    public static final String EMPLOYEE_HISTORY_ADAPTER_ID = "employeeHistory";
    public static final String EMPLOYEE_HISTORY_SOCKET_EXPRESSION = "@employeeHistory.socket";

    // UDP MESSAGING CONFIGURATION CONSTANTS
    public static final String ROUTE_BY_OPTION_EXPRESSION = "payload.meta.option";
    public static final String UDP_IN_CHANNEL = "udpIn";
    public static final String UDP_OUT_CHANNEL = "udpOut";
    public static final String VALIDATE_CREDENTIALS_CHANNEL = "validateCredentials";
    public static final String GET_HISTORY_BY_EMPLOYEE_ID_CHANNEL = "getHistoryByEmployeeId";
    public static final String GET_HISTORY_BY_EMPLOYEE_LAST_NAME_CHANNEL = "getHistoryByEmployeeLastName";
    public static final String MESSAGING_EXECUTOR = "executor";
    public static final int POLLING_RATE = 500;

    // ERROR CONSTANTS
    public static final String UNEXPECTED_REQUEST_FORMAT = "Unexpected request format";
    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid login and/or password";

    // DATETIME
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy";

}
