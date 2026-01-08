package net.devk.business.service.locale;

import jakarta.ejb.Stateless;

import java.util.Locale;

@Stateless
public class LocaleServiceBean {


    public String[] getSystemLanguages() {
        return Locale.getISOLanguages();
    }
}
