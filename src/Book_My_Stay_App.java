public class Book_My_Stay_App {

    private String guestName;

    /** Requested room type. */
    private String roomType;


    public void Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    /** @return requested room type */
    public String getRoomType() {
        return roomType;
    }
}