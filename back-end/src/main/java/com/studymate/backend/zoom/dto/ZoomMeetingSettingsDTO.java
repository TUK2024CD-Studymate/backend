package com.studymate.backend.zoom.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ZoomMeetingSettingsDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private Boolean host_video;

    private Boolean participant_video;

    private Boolean cn_meeting;

    private Boolean in_meeting;

    private Boolean join_before_host;

    private Boolean mute_upon_entry;

    private Boolean watermark;

    private Boolean use_pmi;

    private Integer approval_type;

    private Integer registration_type;

    private String audio;

    private String auto_recording;

    private String alternative_hosts;

    private Boolean close_registration;

    private Boolean waiting_room;

    List<String> global_dial_in_countries;

    List<ZoomGlobalDialInNumbersDTO> global_dial_in_numbers;

    private Boolean registrants_email_notification;

    private String contact_name;

    private String contact_email;

    private Boolean registrants_confirmation_email;

    private Boolean meeting_authentication;

    private String authentication_option;

    private String authenticated_domains;

    private String authentication_name;

    private Boolean show_share_button;

    private Boolean allow_multiple_devices;

    private String encryption_type;

    private List<String> meeting_invitees;

    public List<ZoomInterpreterDTO> interpreters;

    @Override
    public String toString() {
        return "ZoomMeetingSettingsDTO [allow_multiple_devices=" + allow_multiple_devices + ", alternative_hosts="
                + alternative_hosts + ", approval_type=" + approval_type + ", audio=" + audio
                + ", authenticated_domains=" + authenticated_domains + ", authentication_name=" + authentication_name
                + ", authentication_option=" + authentication_option + ", auto_recording=" + auto_recording
                + ", close_registration=" + close_registration + ", cn_meeting=" + cn_meeting + ", contact_email="
                + contact_email + ", contact_name=" + contact_name + ", encryption_type=" + encryption_type
                + ", global_dial_in_countries=" + global_dial_in_countries + ", global_dial_in_numbers="
                + global_dial_in_numbers + ", host_video=" + host_video + ", in_meeting=" + in_meeting
                + ", interpreters=" + interpreters + ", join_before_host=" + join_before_host
                + ", meeting_authentication=" + meeting_authentication + ", mute_upon_entry=" + mute_upon_entry
                + ", participant_video=" + participant_video + ", registrants_confirmation_email="
                + registrants_confirmation_email + ", registrants_email_notification=" + registrants_email_notification
                + ", registration_type=" + registration_type + ", show_share_button=" + show_share_button + ", use_pmi="
                + use_pmi + ", waiting_room=" + waiting_room + ", watermark=" + watermark + "]";
    }
}
