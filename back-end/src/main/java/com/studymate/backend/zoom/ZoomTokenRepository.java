package com.studymate.backend.zoom;

import com.studymate.backend.zoom.domain.ZoomMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoomTokenRepository extends JpaRepository<ZoomMeeting, Long> {
}
