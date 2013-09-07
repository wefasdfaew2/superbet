package fr.ele.services.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.ele.model.BookMakerSynchronization;
import fr.ele.ui.model.MetaMapping;
import fr.ele.ui.model.MetaRegistry;
import fr.ele.ui.mvc.AbstractActivityController;
import fr.ele.ui.mvc.annotation.Activity;
import fr.ele.ui.mvc.annotation.Group;

@Controller
@Group("admin")
public class AdminController extends AbstractActivityController {

    @Autowired
    private MetaRegistry metaRegistry;

    @RequestMapping("synchronize")
    @Activity(name = "Synchronize Bookmakers")
    public String synchronize(Locale locale, Model model) {
        MetaMapping metaMapping = metaRegistry
                .getMetaMapping(BookMakerSynchronization.class);
        model.addAttribute("model", metaMapping);
        model.addAttribute("resource", "admin/syncs");
        mapActivities(model);
        return "synchronizeView";
    }
}
