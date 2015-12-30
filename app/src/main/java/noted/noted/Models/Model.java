package noted.noted.Models;

import android.content.Context;

/**
 * Created by Anna on 30-Dec-15.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;

    ModelSql local = new ModelSql();
    ModelParse remote = new ModelParse();

    private Model(){}

    public static Model getInstance(){
        return instance;
    }

    public void init(Context context){
        this.context = context;
        remote.init(context);
        local.init(context);
    }

    public void addNote(Note note) {
    }
}
