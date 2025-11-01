package org.patchnote.patchnote.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 서버 동작을 확인하기 위한 간단한 테스트 컨트롤러입니다.
 *
 * @RestController 어노테이션은 두 가지 의미를 갖습니다:
 * 1. 이 클래스가 Spring의 컴포넌트임을 나타냅니다 (@Component의 특수한 형태)
 * 2. 모든 메서드의 반환값이 HTTP 응답 본문으로 직접 변환됩니다 (@ResponseBody가 자동 적용됨)
 *
 * 예를 들어 String을 반환하면 text/plain으로, 객체를 반환하면 JSON으로 자동 변환됩니다.
 */
@RestController
@RequestMapping("/api")  // 이 컨트롤러의 모든 경로는 /api로 시작합니다
class HelloController {

    /**
     * 가장 기본적인 GET 엔드포인트입니다.
     *
     * @GetMapping은 HTTP GET 요청을 처리합니다.
     * 여기서는 "/api/hello" 경로로 오는 요청을 받습니다.
     *
     * 브라우저에서 http://localhost:8080/api/hello 로 접속하면
     * 이 메서드가 실행되어 "Hello from PatchNote!" 문자열이 반환됩니다.
     */
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello from PatchNote!"
    }

    /**
     * 서버 상태를 확인하는 엔드포인트입니다.
     *
     * 실제 프로덕션 환경에서는 Actuator의 /actuator/health를 사용하지만,
     * 여기서는 학습 목적으로 간단한 헬스체크를 직접 만들어봅니다.
     */
    @GetMapping("/health")
    fun health(): Map<String, Any> {
        // Map을 반환하면 Spring이 자동으로 JSON으로 변환합니다
        // {"status": "UP", "service": "patchnote-backend", "timestamp": 1234567890}
        return mapOf(
            "status" to "UP",
            "service" to "patchnote-backend",
            "timestamp" to System.currentTimeMillis()
        )
    }
}