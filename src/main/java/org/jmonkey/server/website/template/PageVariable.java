package org.jmonkey.server.website.template;

/**
 *
 * @author jayfella
 */
public enum PageVariable {
        
    HEADER_INPUT("{header_input}"),
    PAGE_TITLE("{page_title}"),
    PAGE_BODY("{page_body}"),

    GUEST_OPENTAG("{guest}"),
    GUEST_CLOSETAG("{/guest}"),

    LOGGEDIN_OPENTAG("{logged_in}"),
    LOGGEDIN_CLOSETAG("{/logged_in}"),

    ADMIN_OPENTAG("{admin}"),
    ADMIN_CLOSETAG("{/admin}"),

    USERNAME("{username}");

    private final String name;

    private PageVariable(String name) {
        this.name = name;
    }

    public String getVariableName() { return this.name; }
}