package cm.packagemanager.pmanager.common.ent.dto;


import java.util.List;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;


// @JsonInclude annotation ensures that only non null values are sent in the response.
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ResponseDTO {

	private final List<AnnounceVO> movies;
	private final int totalItems;
	private final int totalPages;
	private final int currentPage;
	private final Boolean isFirstPage;
	private final Boolean isLastPage;

	private ResponseDTO(final List<AnnounceVO> movies, final int totalItems, final int totalPages, final int currentPage,
	                    final Boolean isFirstPage, final Boolean isLastPage) {
		this.movies = movies;
		this.totalItems = totalItems;
		this.totalPages = totalPages;
		this.currentPage = currentPage;
		this.isFirstPage = isFirstPage;
		this.isLastPage = isLastPage;
	}

	public static ResponseDTO create(final List<AnnounceVO> movies, final int totalItems, final int totalPages,
	                                 final int currentPage, final Boolean isFirstPage, final Boolean isLastPage) {
		return new ResponseDTO(movies, totalItems, totalPages, currentPage, isFirstPage, isLastPage);
	}

	public List<AnnounceVO> getMovies() {
		return movies;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}
}
