package oop_motorph.model;

public class Position {
    private Integer positionId;
    private String positionName;

    public Position(Integer positionId, String positionName) {
        this.positionId = positionId;
        this.positionName = positionName;
    }

    public Position(String positionName) {
        this(null, positionName);
    }

    // Getters and Setters
    public Integer getPositionId() { return positionId; }
    public void setPositionId(Integer positionId) { this.positionId = positionId; }
    public String getPositionName() { return positionName; }
    public void setPositionName(String positionName) { this.positionName = positionName; }
}
