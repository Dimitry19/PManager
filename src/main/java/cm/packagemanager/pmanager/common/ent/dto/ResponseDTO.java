package cm.packagemanager.pmanager.common.ent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;


// @JsonInclude annotation ensures that only non null values are sent in the response.
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ResponseDTO<T> {

    private final List<T> elements;
    private final int totalItems;
    private final int totalPages;
    private final int currentPage;
    private final Boolean isFirstPage;
    private final Boolean isLastPage;

    private ResponseDTO(final List<T> elements, final int totalItems, final int totalPages, final int currentPage,
                        final Boolean isFirstPage, final Boolean isLastPage) {
        this.elements = elements;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.isFirstPage = isFirstPage;
        this.isLastPage = isLastPage;
    }

    public  ResponseDTO create(final List<T> elements, final int totalItems, final int totalPages,
                                     final int currentPage, final Boolean isFirstPage, final Boolean isLastPage) {
        return new ResponseDTO(elements, totalItems, totalPages, currentPage, isFirstPage, isLastPage);
    }

    public List<T> getElements() {
        return elements;
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
