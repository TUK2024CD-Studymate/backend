package com.studymate.backend.zoom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZoomMeetingTrackingFieldsDTO {
    public String field;

    public String value;

    public Boolean visible;


    @Override
    public String toString() {
        return "ZoomMeetingTrackingFieldsDTO [field=" + getField() + ", value=" + getValue() + ", visible=" + getVisible() + "]";
    }
}
