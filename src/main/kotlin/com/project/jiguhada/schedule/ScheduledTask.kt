package com.project.jiguhada.schedule

import com.project.jiguhada.service.ChallengeService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class ScheduledTask(
    private val challengeService: ChallengeService
) {


}