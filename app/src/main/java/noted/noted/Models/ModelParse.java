package noted.noted.Models;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by Anna on 30-Dec-15.
 */
public class ModelParse {
    public void init(Context context) {
        Parse.initialize(context);
    }

}
