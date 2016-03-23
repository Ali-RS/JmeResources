/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmonkey.server.website.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.permission.AdminPermission;
import org.jmonkey.database.permission.PermissionManager;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.SessionInspector;

import javax.servlet.http.Cookie;

/**
 *
 * @author jayfella
 */
public class TemplatedWebPage {
    
    private final StringBuilder html;
    private String pageTitle = "jMonkey";
    private Cookie[] cookies;
    
    public TemplatedWebPage(final String htmlIn) throws IOException, TemplateFileNotFoundException {
        
        File page_template = Paths.get(JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getActiveThemePath().toString(), "page_template.html").toFile();
        
        if (!page_template.exists()) {
            throw new TemplateFileNotFoundException("file 'page_template.html' does not exist.");
        }
        
        this.html = new StringBuilder().append(new String(Files.readAllBytes(page_template.toPath())));
        
        this.html.replace(this.html.indexOf(PageVariable.PAGE_BODY.getVariableName()), 
                this.html.indexOf(PageVariable.PAGE_BODY.getVariableName()) + PageVariable.PAGE_BODY.getVariableName().length(), 
                htmlIn);
    }
    
    public TemplatedWebPage(File file) throws IOException, TemplateFileNotFoundException {
        this(new String(Files.readAllBytes(file.toPath())));
    }

    public TemplatedWebPage useSessionData(Cookie[] cookies) {
        this.cookies = cookies;

        return this;
    }

    public TemplatedWebPage AddLineToHeader(String line) {
        
        int varpos = this.html.indexOf(PageVariable.HEADER_INPUT.getVariableName());
        
        this.html.insert(varpos, line + "\n");
        
        return this;
    }
    
    public TemplatedWebPage addLinesToHeader(List<String> lines) {
        
        lines.stream().forEach((line) -> {
            this.AddLineToHeader(line);
        });
        
        return this;
    }
    
    public TemplatedWebPage setPageTitle(String title) {
        
        this.pageTitle = title;
        return this;
    }

    private void replaceVariable(PageVariable var, String replacement) {

        this.html.replace(this.html.indexOf(var.getVariableName()),
                this.html.indexOf(var.getVariableName()) + var.getVariableName().length(),
                replacement);
    }

    private void replaceVariables(PageVariable var, String replacement) {
        while (this.html.indexOf(var.getVariableName()) != -1) {
            this.replaceVariable(var, replacement);
        }
    }

    private void removeVariable(PageVariable var) {
        this.html.delete(this.html.indexOf(var.getVariableName()), this.html.indexOf(var.getVariableName()) + var.getVariableName().length());
    }

    private void removeVariables(PageVariable var) {

        while (this.html.indexOf(var.getVariableName()) != -1) {
            removeVariable(var);
        }
    }

    private void removeSection(PageVariable varStart, PageVariable varEnd) {
        this.html.delete(this.html.indexOf(varStart.getVariableName()), this.html.indexOf(varEnd.getVariableName()) + varEnd.getVariableName().length());
    }

    private void removeSections(PageVariable varStart, PageVariable varEnd) {
        while (this.html.indexOf(varStart.getVariableName()) != -1 && this.html.indexOf(varEnd.getVariableName()) != -1) {
            removeSection(varStart, varEnd);
        }
    }

    public String build() throws IOException {
        
        this.replaceVariable(PageVariable.PAGE_TITLE,
                JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().getPageTitlePrepend()
                + " " + this.pageTitle + " "
                + JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().getPageTitleAppend());

        this.removeVariable(PageVariable.HEADER_INPUT);

        User user = new SessionInspector().getUser(cookies);
        boolean isAdmin = new PermissionManager().hasPermission(user, AdminPermission.class);

        if (user == null) {

            this.replaceVariables(PageVariable.USERNAME, "guest");
            this.removeSections(PageVariable.ADMIN_OPENTAG, PageVariable.ADMIN_CLOSETAG);
            this.removeSections(PageVariable.LOGGEDIN_OPENTAG, PageVariable.LOGGEDIN_CLOSETAG);

            this.removeVariables(PageVariable.GUEST_OPENTAG);
            this.removeVariables(PageVariable.GUEST_CLOSETAG);
        }
        else
        {
            this.replaceVariables(PageVariable.USERNAME, user.getUsername());

            this.removeVariables(PageVariable.LOGGEDIN_OPENTAG);
            this.removeVariables(PageVariable.LOGGEDIN_CLOSETAG);

            if (!isAdmin) {
                this.removeSections(PageVariable.ADMIN_OPENTAG, PageVariable.ADMIN_CLOSETAG);
            }
            else
            {
                this.removeVariables(PageVariable.ADMIN_OPENTAG);
                this.removeVariables(PageVariable.ADMIN_CLOSETAG);
            }

            this.removeSections(PageVariable.GUEST_OPENTAG, PageVariable.GUEST_CLOSETAG);
        }

        return this.html.toString();
    }
    
}
