package com.cmms.demo.domain;


public enum ActionName {
    CREATE("CREATE", "Tạo mới"),
    UPDATE("UPDATE", "Cập nhật"),
    REGISTER("REGISTER", "Đăng ký"),
    DELETE("DELETE", "Xóa"),
    VIEW("VIEW", "Xem tất cả"),
    CHANGEPASS("CHANGEPASS", "Đổi mật khẩu"),
    RESET("RESET", "Tạo lại mật khẩu"),
    VIEWONE("VIEWONE", "Xem một số");
    private String code;
    private String text;

    ActionName(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public static String getApiNameByCode(String code) {
        for (ActionName name : ActionName.values()) {
            if (name.code.equalsIgnoreCase(code)) {
                return name.text;
            }
        }
        return code;
    }

    public static String getApiNameByText(String text) {
        for (ActionName name : ActionName.values()) {
            if (name.text.equalsIgnoreCase(text)) {
                return name.code;
            }
        }
        return text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
