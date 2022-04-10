package cm.travelpost.tp.ws.requests.announces;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CategoriesDTO {

    @NotNull(message = "La categorie doit etre valorisée")
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
