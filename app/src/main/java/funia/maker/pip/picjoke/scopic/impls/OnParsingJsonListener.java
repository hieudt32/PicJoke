package funia.maker.pip.picjoke.scopic.impls;

import java.util.ArrayList;

import funia.maker.pip.picjoke.scopic.model.Image;
import funia.maker.pip.picjoke.scopic.task.ParsingJsonTask;


public interface OnParsingJsonListener {
    void onCompleteParsingJson(ParsingJsonTask parsingJsonTask, ArrayList arr);
}
