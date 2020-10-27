package edu.exam.tcp.server.utils;

public class Constants {

    // UDP CONFIGURATION CONSTANTS
    public static final String UDP_ADAPTER_ID = "udpAdapter";
    public static final String UDP_SOCKET_EXPRESSION = "@udpAdapter.socket";
    public static final String UDP_ROUTE_BY_OPTION_EXPRESSION = "payload.meta.option";

    // TCP CONFIGURATION CONSTANTS
    public static final String HEADERS_BY_OPTION_EXPRESSION = "headers.option";
    public static final String HEADERS_BY_CONNECTION_ID_EXPRESSION =  "payload.meta.connectionId";

    // MESSAGING CHANNELS CONFIGURATION CONSTANTS
    public static final String UDP_IN_CHANNEL = "udpIn";
    public static final String UDP_OUT_CHANNEL = "udpOut";
    public static final String TCP_IN_CHANNEL = "tcpIn";
    public static final String TCP_OUT_CHANNEL = "tcpOut";
    public static final String LOGIN_IN_CHANNEL = "loginIn";
    public static final String LOGIN_OUT_CHANNEL = "loginOut";
    public static final String LOGOUT_IN_CHANNEL = "logoutIn";
    public static final String AUTH_IN_CHANNEL = "authIn";
    public static final String AUTH_OUT_CHANNEL = "authOut";
    public static final int POLLING_RATE = 500;

    // ERROR CONSTANTS
    public static final String ACCESS_DENIED_PREFIX = "Access denied: ";
    public static final String UNEXPECTED_UDP_RESPONSE = "Unexpected UDP response";
    public static final String INVALID_AUTH_TOKEN = "Authentication token is not valid";

}
