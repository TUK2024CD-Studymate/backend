package com.studymate.backend.zoom.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ZoomMeetingObjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String uuid;

    private String assistant_id;

    private String host_email;

    private String registration_url;

    private String topic;

    private Integer type;

    private String start_time;

    private Integer duration;

    private String schedule_for;

    private String timezone;

    private String created_at;

    private String password;

    private Boolean default_password;

    public Boolean getDefault_password() {
        return default_password;
    }

    public void setDefault_password(Boolean default_password) {
        this.default_password = default_password;
    }

    private String agenda;

    private String start_url;

    private String join_url;

    private String h323_password;

    private Integer pmi;

    private ZoomMeetingRecurrenceDTO recurrence;

    private List<ZoomMeetingTrackingFieldsDTO> tracking_fields;

    private List<ZoomMeetingOccurenceDTO> occurrences;

    private ZoomMeetingSettingsDTO settings;

    @Override
    public String toString() {
        return "ZoomMeetingObjectDTO [agenda=" + agenda + ", assistant_id=" + assistant_id + ", created_at="
                + created_at + ", duration=" + duration + ", h323_password=" + h323_password + ", host_email="
                + host_email + ", id=" + id + ", join_url=" + join_url + ", occurrences=" + occurrences + ", password="
                + password + ", pmi=" + pmi + ", recurrence=" + recurrence + ", registration_url=" + registration_url
                + ", schedule_for=" + schedule_for + ", settings=" + settings + ", start_time=" + start_time
                + ", start_url=" + start_url + ", timezone=" + timezone + ", topic=" + topic + ", tracking_fields="
                + tracking_fields + ", type=" + type + ", uuid=" + uuid + "]";
    }

}