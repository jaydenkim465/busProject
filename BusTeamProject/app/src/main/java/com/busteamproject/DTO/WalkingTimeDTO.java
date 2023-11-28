package com.busteamproject.DTO;

public class WalkingTimeDTO {

    private String totalDistance;
    private String totalTime;
    private String description;

    public WalkingTimeDTO() {
    }

    public WalkingTimeDTO(String totalDistance, String totalTime, String description) {
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.description = description;
    }

    @Override
    public String toString() {
        return "WalkingTimeDTO{" +
                "totalDistance='" + totalDistance + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
