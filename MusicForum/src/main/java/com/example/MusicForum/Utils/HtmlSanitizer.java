package com.example.MusicForum.Utils;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {

    private final PolicyFactory policy;

    public HtmlSanitizer() {
        
        this.policy = Sanitizers.FORMATTING
        .and(Sanitizers.LINKS)
        .and(Sanitizers.BLOCKS)
        .and(Sanitizers.IMAGES);
    }

    public String sanitize(String html) {
        if (html == null) return null;
        return policy.sanitize(html);
    }
}