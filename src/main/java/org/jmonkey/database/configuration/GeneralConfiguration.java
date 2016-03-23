package org.jmonkey.database.configuration;

import javax.persistence.*;

/**
 * @author jayfella
 */

@Embeddable
public class GeneralConfiguration extends DatabaseSavedConfiguration {

    private String userAgent;
    private String pageTitlePrepend;
    private String pageTitleAppend;

    @Column(name = "general_user_agent", length = 128)
    public String getUserAgent() { return this.userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    @Column(name = "general_page_title_prepend", length = 128)
    public String getPageTitlePrepend() { return this.pageTitlePrepend; }
    public void setPageTitlePrepend(String pageTitlePrepend) { this.pageTitlePrepend = pageTitlePrepend; }

    @Column(name = "general_page_title_append", length = 128)
    public String getPageTitleAppend() { return this.pageTitleAppend; }
    public void setPageTitleAppend(String pageTitleAppend) { this.pageTitleAppend = pageTitleAppend; }

}
