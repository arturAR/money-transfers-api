package model.dto;

public class ErrorMessage {
    public final String msg;
    public String desc;

    public ErrorMessage(String msg, String desc) {
        this.msg = msg;
        this.desc = desc;
    }

    public ErrorMessage(String msg) {
        this.msg = msg;
    }
}
