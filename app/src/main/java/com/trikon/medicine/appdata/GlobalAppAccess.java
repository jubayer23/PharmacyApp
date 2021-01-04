package com.trikon.medicine.appdata;




public class GlobalAppAccess {

    public static final String KEY_ORDER_NOTIFICATION = "notification_order";
    public static final String APP_NAME = "PharmacyApp";
    private static final String BASE_URL = "http://144.217.186.122:8080/api";
    public static final String BASE_URL_2 = "http://144.217.186.122:8080";

    public static final String URL_LOGIN = BASE_URL + "/login";
    public static final String URL_REGISTRATION = BASE_URL + "/register";
    public static final String URL_EVENT_LIST = BASE_URL + "/getEvents";
    public static final String URL_ADD_EVENT = BASE_URL + "/addEvent";
    public static final String URL_ADD_PRESCRIPTION = BASE_URL + "/addPrescription";
    public static final String URL_GET_GENERICS = BASE_URL + "/getGenerics";
    public static final String URL_GET_COMPANIES = BASE_URL + "/getCompanies";
    public static final String URL_GET_THANA = BASE_URL + "/getThanas";
    public static final String URL_GET_DISTRIC = BASE_URL + "/getDistricts";
    public static final String URL_GET_DIVISION = BASE_URL + "/getDivisions";
    public static final String URL_SEARCH_MEDICINE = BASE_URL + "/searchOffer";
    public static final String URL_RESET_PASSWORD = BASE_URL + "/support";
    public static final String URL_PENDING_ORDER = BASE_URL + "/getPendingOrders";
    public static final String URL_START_ORDER = BASE_URL + "/startOrder";
    public static final String URL_GET_LICENSE_HISTORY = BASE_URL + "/ParkingSentinel/GetLicenseHistory";
    public static final String URL_CREATE_SIGHTING = BASE_URL + "/ParkingSentinel/CreateSighting";



    //public static final String DATE_FORMAT_LOCAL = "hh:mmaa dd MMM yyyy";
    public static final String DATE_FORMAT_LOCAL = "dd MMM yyyy HH:mm";

    public static final  int SUCCESS = 1;
    public static  final  int ERROR = 0;


    public static final String CAT_FAVOURITE = "Favourite";
    public static final String CAT_FAVOURITE_ID = "-100";
    public static final String CAT_EMERGENCY = "Emergency";
    public static final String CAT_EMERGENCY_ID = "Emergency";

}
