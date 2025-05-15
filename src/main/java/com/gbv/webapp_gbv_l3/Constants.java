package com.gbv.webapp_gbv_l3;

public class Constants {
    public static final String ACTION_PARAM_NAME = "action";
    public static final String INSERT_ACTION = "insert";
    public static final String DELETE_ACTION = "delete";
    public static final String UPDATE_ACTION = "update";
    public static final String OPEN_EDIT_PAGE_ACTION = "open-edit-page";
    public static final String FILTER_ACTION = "filter";
    public static final String MAIN_SERVLET_PATH = "main-servlet";
    public static final String MESSAGE_ATTRIBUTE_NAME = "message";
    public static final String ALL_FIELDS_REQUIRED_MSG = "Заповніть усі поля";
    public static final String FILTER_PARAM_NAMEDEP = "namedep";
    public static final String FILTER_PARAM_NAMESDEP = "namesdep";
    public static final String FILTER_PARAM_CODEDEP = "codedep";
    public static final String FILTER_PARAM_EMAIL = "email";
    public static final String FILTER_PARAM_PHONE = "phone";
    public static final String CODEDEP_REGEX = "^\\[0-9]{3}$";
    public static final String CODEDEP_REGEX_CHECK = " REGEXP '^\\[0-9]{3}$'";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public static final String EMAIL_REGEX_CHECK = " REGEXP '^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$'";
    public static final String PHONE_MASK_REGEX = "^[0-9]{2}-[0-9]{3}$";
    public static final String PHONE_MASK_REGEX_CHECK = " REGEXP '^[0-9]{2}-[0-9]{3}$'";

    private Constants() {
    }
}
