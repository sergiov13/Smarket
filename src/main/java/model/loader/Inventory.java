package model.loader;

import java.util.ArrayList;
import java.util.List;


public enum Inventory {
	CATEGORY("\\Category.config"),
	DEPARTMENT("\\Department.config"),
	PRODUCT("\\Product.config"),
	INVENTORY("\\Inventory.config");

    public static final List<Inventory> all = new ArrayList<Inventory>(){{
        add(CATEGORY);
        add(DEPARTMENT);
        add(PRODUCT);
        add(INVENTORY);
    }};

    private String value;

    Inventory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
