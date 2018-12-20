package newbienewsletter.document;

import newbienewsletter.details.Details;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Document {
    private String filename;
    private Details details;
}