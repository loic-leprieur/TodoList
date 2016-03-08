package fr.vincent_leprieur.todolist;

/**
 * Created by phil on 24/02/15.
 */
public class Item {
    private long id;
    private String label;

    public Item(String s) {
        label = s;
        id = -1;
    }

    public Item(long id, String label) {
        this.id = id;
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void save(AddItemActivity addItemActivity) {
        if (id == -1)
            saveNewItem(addItemActivity);
    }

    private void saveNewItem(AddItemActivity addItemActivity) {
        TodoBase.addItem(addItemActivity, label);
    }
}
