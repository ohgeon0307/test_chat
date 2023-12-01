package dto;

public class ChatDto {
    private int uidx;
    private String sender;  // 추가: sender 정보
    private String message;

    // 기본 생성자
    public ChatDto() {
    }

    // 매개변수를 받는 생성자
    public ChatDto(int uidx, String sender, String message) {
        this.uidx = uidx;
        this.sender = sender;
        this.message = message;
    }

    public int getUidx() {
        return uidx;
    }

    public void setUidx(int uidx) {
        this.uidx = uidx;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}