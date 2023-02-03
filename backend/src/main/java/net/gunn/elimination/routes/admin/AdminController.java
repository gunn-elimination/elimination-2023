package net.gunn.elimination.routes.admin;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.auth.EliminationUserService;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.model.Kill;
import net.gunn.elimination.repository.AnnouncementRepository;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.AnnouncementController;
import net.gunn.elimination.routes.SSEController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.*;

import static net.gunn.elimination.auth.Roles.BANNED;
import static net.gunn.elimination.auth.Roles.PLAYER;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
	private final AnnouncementRepository announcementRepository;
	private final EliminationManager eliminationManager;
	private final UserRepository userRepository;

	private final AnnouncementController announcementController;
	private final Optional<SSEController> sseController;
	private final EliminationUserService userService;

	public AdminController(AnnouncementRepository announcementRepository, EliminationManager eliminationManager, UserRepository userRepository, AnnouncementController announcementController, EliminationUserService userService, Optional<SSEController> sseController) {
		this.announcementRepository = announcementRepository;
		this.eliminationManager = eliminationManager;
		this.userRepository = userRepository;
		this.announcementController = announcementController;
		this.sseController = sseController;
		this.userService = userService;
	}

	@PostMapping("/announcement")
	@Transactional
	public void announcement(
		HttpServletRequest req,
		HttpServletResponse response,
		@RequestParam("title") String title,
		@RequestParam("body") String body,
		@RequestParam long startTime,
		@RequestParam(required = false, defaultValue = "253402329599000") long endTime,
		@RequestParam(required = false) String active
	) throws IOException {
		var announcement = new Announcement(title, body, new Date(startTime), new Date(endTime), active != null && active.equals("on"));
		announcementRepository.save(announcement);
		sseController.ifPresent(SSEController::signalAnnouncementChange);

		response.setStatus(HttpServletResponse.SC_CREATED);
		response.sendRedirect(req.getHeader("Referer"));
	}

	@PostMapping("/announcement/{id}")
	@Transactional
	public void announcement(
		HttpServletRequest req,
		HttpServletResponse response,
		@PathVariable("id") long id,
		@RequestParam("title") String title,
		@RequestParam("body") String body,
		@RequestParam long startTime,
		@RequestParam(required = false, defaultValue = "253402329599000") long endTime,
		@RequestParam(required = false) String active,
		@RequestParam(required = false, defaultValue = "action") String action
	) throws IOException {
		if (action.equals("delete")) {
			announcementRepository.deleteById(id);
		} else {
			var announcement = announcementRepository.findById(id).orElseThrow();
			announcement.setTitle(title);

			announcement.setBody(body);

			announcement.setStartDate(new Date(startTime));
			announcement.setEndDate(new Date(endTime));

			announcement.setActive(active != null && active.equals("on"));

			announcementRepository.deleteById(id);
			announcementRepository.save(announcement);
			sseController.ifPresent(SSEController::signalAnnouncementChange);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(req.getHeader("Referer"));
	}


	@DeleteMapping("/user/{email}")
	@Transactional
	public void user(
		HttpServletRequest req,
		HttpServletResponse response,
		@PathVariable("email") String email
	) throws IOException {
		var subject = userRepository.findByEmail(email).orElseThrow().getSubject();
		eliminationManager.unlink(subject);
		var user = userRepository.findByEmail(email).orElseThrow();
		user.removeRole(PLAYER);
		user.addRole(BANNED);
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(req.getHeader("Referer"));
	}

	@GetMapping("/regenerateCodes")
	@Transactional
	public String regenerateCodes(
		HttpServletRequest req,
		HttpServletResponse response
	) throws IOException {
		eliminationManager.regenerateCodes();
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(req.getHeader("Referer"));
		return "OK";
	}

	@GetMapping("/test/announcement")
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public String testAnnouncement(HttpServletRequest req, HttpServletResponse response) throws IOException {
		sseController.get().signalAnnouncementChange();
		response.setStatus(HttpServletResponse.SC_OK);
		return "OK";
	}

	@GetMapping("/test/elimination")
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public String testElimination(HttpServletRequest req, HttpServletResponse response) throws IOException {
		sseController.get().signalKill(new Kill(new EliminationUser(), new EliminationUser(), Instant.now()));
		response.setStatus(HttpServletResponse.SC_OK);
		return "OK";
	}

	@GetMapping("/test/scoreboard")
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public String testScoreboard(HttpServletRequest req, HttpServletResponse response) throws IOException {
		sseController.get().signalScoreboardChange();
		response.setStatus(HttpServletResponse.SC_OK);
		return "OK";
	}

	@Transactional
	@GetMapping("/awardEliminationOf")
	public String awardElim(@RequestParam("toEliminateEmail") String toEliminateEmail, HttpServletResponse response) {
		var toEliminate = userRepository.findByEmail(toEliminateEmail);

		if (toEliminate.isPresent()) {
			EliminationUser eliminated = toEliminate.get();
			EliminationUser eliminator = eliminated.getTargettedBy();
			String elimCode = eliminated.getEliminationCode();

			try {
				eliminationManager.attemptElimination(eliminator, elimCode);

				response.setStatus(HttpServletResponse.SC_OK);
				return "OK, eliminated " + eliminated.getEmail();
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "failed to eliminate";
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "victim not found";
		}
	}

	@GetMapping("/reshuffleAllTargetsYesImVerySure")
	public void reshuffleTargets() {
		eliminationManager.reshuffleChain();
	}

	@GetMapping("/validateGame")
	public Map validateGame() {
		return eliminationManager.validateGame();
	}

	@Profile(value = "test")
	@GetMapping("/insertTestDataAAAA")
	public void insertTestData() {
		userService.setupNewUser(new EliminationUser(
			"bbbb", "ky28059@pausd.us", "Kevin", "Yu", "no-no", new HashSet<>()));
		userService.setupNewUser(new EliminationUser(
			"cccc", "ap40132@pausd.us", "Alec", "Petridis", "maybe-maybe", new HashSet<>()));
	}

	@Transactional
	@GetMapping("/getAllGhosts")
	public Set getAllGhosts() {
		Set ghosts = new HashSet();
		for (EliminationUser user : userRepository.findAll()) {
			if (user.getTargettedBy() != null && user.isEliminated()) {
				ghosts.add(user);
			}
		}
		return ghosts;
	}

	@Transactional
	@GetMapping("/fixAllGhostsLol")
	public String fixGhosts() {
		List<EliminationUser> chasingGhosts = new LinkedList();
		for (EliminationUser user : userRepository.findAll()) {
			if (user.getTargettedBy() != null && user.isEliminated()) {
				chasingGhosts.add(user.getTargettedBy());
			}
		}

		// find two poor unsuspecting players

		// random point to start looking in chain
		Random random = new Random();
		var page = random.nextInt(userRepository.countEliminationUsersByRolesContaining(PLAYER) - 1);
		var pageRequest = PageRequest.of(page, 1);
		var randUser = userRepository.findEliminationUsersByRolesContaining(
			PLAYER,
			pageRequest
		).getContent().get(0);

		EliminationUser insertionPoint = null;
		EliminationUser currentlySearching = randUser;
		// find first link where user has 0 elims and target has 0 elims (two unsuspecting poor people)
		do {
			if (currentlySearching.eliminatedCount() == 0 && currentlySearching.getTarget().eliminatedCount() == 0) {
				insertionPoint = currentlySearching;
				break; // might as well be safe idk
			}
			currentlySearching = currentlySearching.getTarget();
		} while (insertionPoint == null && currentlySearching != randUser);

		if (insertionPoint == null) {
			return "COULDN'T FIND INSERTION POINT";
		}

		// shuffle chasingGhosts
		Collections.shuffle(chasingGhosts);
		// add the insertion point to the beginning
		chasingGhosts.add(0, insertionPoint);

//		return chasingGhosts.toString();

		String log = chasingGhosts.toString();

		// reinsert them all
		while (chasingGhosts.size() > 1) {
			eliminationManager.insertUserToChain(chasingGhosts.get(1), chasingGhosts.remove(0));
		}

		return "DONE!\n"+log;
	}
}
