package fr.ele.csv.mapping;

import fr.ele.core.csv.CsvContext;
import fr.ele.core.csv.CsvProperty;
import fr.ele.model.SuperBetEntity;
import fr.ele.model.ref.BookMaker;

public enum BookmakerCsvProperty implements CsvProperty<BookMaker> {
    ID("id") {

        @Override
        public String getStringValue(CsvContext<BookMaker> context,
                BookMaker object) {
            return context.marshall(object.getId());
        }

        @Override
        public void setValue(CsvContext context, BookMaker object, String value) {
            ((SuperBetEntity) object).setId((Long) context.unmarshall(
                    Long.class, value));
        }
    },
    CODE("code") {

        @Override
        public String getStringValue(CsvContext<BookMaker> context,
                BookMaker object) {
            return context.marshall(object.getCode());
        }

        @Override
        public void setValue(CsvContext context, BookMaker object, String value) {
            object.setCode((String) context.unmarshall(String.class, value));
        }
    },
    URL("url") {

        @Override
        public String getStringValue(CsvContext<BookMaker> context,
                BookMaker object) {
            return context.marshall(object.getUrl());
        }

        @Override
        public void setValue(CsvContext context, BookMaker object, String value) {
            object.setUrl((String) context.unmarshall(String.class, value));
        }
    },
    URL_SYNC("url sync") {

        @Override
        public String getStringValue(CsvContext<BookMaker> context,
                BookMaker object) {
            return context.marshall(object.getUrlSync());
        }

        @Override
        public void setValue(CsvContext context, BookMaker object, String value) {
            object.setUrlSync((String) context.unmarshall(String.class, value));
        }
    },
    SYNC_SERVICE("sync service") {

        @Override
        public String getStringValue(CsvContext<BookMaker> context,
                BookMaker object) {
            return context.marshall(object.getSynchronizerService());
        }

        @Override
        public void setValue(CsvContext context, BookMaker object, String value) {
            object.setSynchronizerService((String) context.unmarshall(
                    String.class, value));
        }
    },
    ENCODING("encoding") {

        @Override
        public String getStringValue(CsvContext<BookMaker> context,
                BookMaker object) {
            return context.marshall(object.getEncoding());
        }

        @Override
        public void setValue(CsvContext context, BookMaker object, String value) {
            object.setEncoding((String) context.unmarshall(String.class, value));
        }
    };

    private final String header;

    private BookmakerCsvProperty(String header) {
        this.header = header;
    }

    @Override
    public String getHeader() {
        return header;
    }

}
