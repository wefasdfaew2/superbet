package fr.ele.services.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.ele.model.ref.BookMaker;

@Controller
@RequestMapping(BookMakerController.URI)
public class BookMakerController extends AbstractRefController {

    static final String URI = "bookmaker";

    @Override
    public String getActivityName() {
        return "BookMaker";
    }

    @Override
    public String getActivityUrlBase() {
        return URI;
    }

    @Override
    protected Class handledModelClass() {
        return BookMaker.class;
    }

}
