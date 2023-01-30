//package net.gunn.elimination.routes.admin;
//
//import net.gunn.elimination.EliminationManager;
//import net.gunn.elimination.model.EliminationUser;
//import net.gunn.elimination.routes.user.UserRepository;
//import net.gunn.elimination.routes.SSEController;
//import net.gunn.elimination.routes.kills.Kill;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//public class AdminController {
//    private final EliminationManager eliminationManager;
//    private final UserRepository userRepository;
//
//	private final Optional<SSEController> sseController;
//
//    public AdminController(EliminationManager eliminationManager, UserRepository userRepository, Optional<SSEController> sseController) {
//        this.eliminationManager = eliminationManager;
//        this.userRepository = userRepository;
//		this.sseController = sseController;
//	}
//
//    @DeleteMapping("/user/{email}")
//	@Transactional
//    public void user(
//        HttpServletRequest req,
//        HttpServletResponse response,
//        @PathVariable("email") String email
//    ) throws IOException {
//        var subject = userRepository.findByEmail(email).orElseThrow().getSubject();
//        eliminationManager.unlink(subject);
//        userRepository.deleteBySubject(subject);
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.sendRedirect(req.getHeader("Referer"));
//    }
//
//	@GetMapping("/regenerateCodes")
//	@Transactional
//	public String regenerateCodes(
//		HttpServletRequest req,
//		HttpServletResponse response
//	) throws IOException {
//		eliminationManager.regenerateCodes();
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.sendRedirect(req.getHeader("Referer"));
//		return "OK";
//	}
//
//	@GetMapping("/test/announcement")
//	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
//	public String testAnnouncement(HttpServletRequest req, HttpServletResponse response) throws IOException {
//		sseController.get().signalAnnouncementChange();
//		response.setStatus(HttpServletResponse.SC_OK);
//		return "OK";
//	}
//
//	@GetMapping("/test/elimination")
//	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
//	public String testElimination(HttpServletRequest req, HttpServletResponse response) throws IOException {
//		sseController.get().signalKill(new Kill(new EliminationUser(), new EliminationUser()));
//		response.setStatus(HttpServletResponse.SC_OK);
//		return "OK";
//	}
//
//	@GetMapping("/test/scoreboard")
//	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
//	public String testScoreboard(HttpServletRequest req, HttpServletResponse response) throws IOException {
//		sseController.get().signalScoreboardChange();
//		response.setStatus(HttpServletResponse.SC_OK);
//		return "OK";
//	}
//}
