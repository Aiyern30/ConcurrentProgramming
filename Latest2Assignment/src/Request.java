
public class Request {
    public enum RequestType {
        LANDING,
        EMERGENCY_LANDING,
        TAKEOFF,
        ASSIGN_GATE,
        RELEASE_GATE,
        REFUEL
    }

    private final RequestType type;
    private final Plane plane;

    public Request(RequestType type, Plane plane) {
        this.type = type;
        this.plane = plane;
    }

    public RequestType getType() {
        return type;
    }

    public Plane getPlane() {
        return plane;
    }
}