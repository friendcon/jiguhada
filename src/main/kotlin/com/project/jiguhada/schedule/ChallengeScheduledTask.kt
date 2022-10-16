package com.project.jiguhada.schedule

import com.project.jiguhada.service.ChallengeService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class ChallengeScheduledTask(
    private val challengeService: ChallengeService
) {

    /**
     * 초 분 시 일 월 요일 : 매일 0:00 에 실행
     * 1. 챌린지 시작 상태로 변경
     * 2. 챌린지 종료 상태로 변경
     */

    @Scheduled(cron = "0 0 0 1/1 * ?")
    fun updateChallengeStatus() {
        challengeService.updateChallengeStatusStart() // 챌린지 상태 변경 (BEFORE -> INPROGRESS)
        challengeService.updateChallengeStatusEnd() // 챌린지 상태 변경 (INPROGRESS -> END)
    }
}