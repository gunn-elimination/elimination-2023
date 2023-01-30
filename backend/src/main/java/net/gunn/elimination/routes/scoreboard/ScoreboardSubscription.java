package net.gunn.elimination.routes.scoreboard;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

record ScoreboardSubscription(SseEmitter emitter, int limit) {
}
