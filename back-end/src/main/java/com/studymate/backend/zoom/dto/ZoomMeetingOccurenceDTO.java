package com.studymate.backend.zoom.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZoomMeetingOccurenceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String occurrence_id;

    private String start_time;

    private Integer duration;

    private String status;

    @Override
    public String toString() {
        return "ZoomMeetingOccurenceDTO [duration=" + getDuration() + ", occurrence_id=" + getOccurrence_id() + ", start_time="
                + getStart_time() + ", status=" + getStatus() + "]";
    }
}