package net.gunn.elimination.routes.announcements;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
interface AnnouncementRepository extends CrudRepository<Announcement, Integer> {
	/**
	 * Finds all announcements that are currently active.
	 *
	 * @return A list of announcements that are currently active.
	 */
	@Query("""
		select a
		from Announcement a
		where a.startDate <= current_timestamp and a.endDate >= current_timestamp
		order by a.startDate desc
		""")
	List<Announcement> findActiveAnnouncements();

	/**
	 * Finds all announcements.
	 *
	 * @return A list of all announcements.
	 */
	@Query("""
		select a
		from Announcement a
		order by a.startDate desc
		""")
	List<Announcement> findAllAnnouncements();

	/**
	 * Checks if an announcement with the given title exists.
	 *
	 * @param title The title of the announcement to check for.
	 * @return True if an announcement with the given title exists, false otherwise.
	 */
	@Query("""
		select count(a) > 0
		from Announcement a
		where a.title = :title
		""")
	boolean existsByTitle(String title);
}
