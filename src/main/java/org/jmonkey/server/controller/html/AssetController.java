package org.jmonkey.server.controller.html;

import org.eclipse.jetty.http.HttpStatus;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.AssetInfo;
import org.jmonkey.database.asset.AssetManager;
import org.jmonkey.database.asset.PotentialAsset;
import org.jmonkey.database.user.SessionInspector;
import org.jmonkey.database.user.User;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.ResourceNotFoundException;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author jayfella
 */

@Controller
@RequestMapping("/asset/")
public class AssetController {

    @RequestMapping(value = "/upload/", method = RequestMethod.GET)
    @ResponseBody
    public String displayIndex(HttpServletRequest request, HttpServletResponse response) throws TemplateFileNotFoundException, IOException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "upload_asset.html")
                .toFile();

        if (!htmlFile.exists()) {
            throw new ResourceNotFoundException();
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("Upload Resource")
                .useSessionData(request.getCookies())
                .build();
    }

    @RequestMapping(value = "/upload/", method = RequestMethod.POST)
    @ResponseBody
    public PotentialAsset uploadPotentialAsset(
            @RequestParam("packageName") String packageName,
            @RequestParam("licenseType") String licenseType,
            @RequestParam("showcaseImage") String showcaseImage,
            @RequestParam("versionDescription") String versionDescription,
            @RequestParam("packageReadme") String packageReadme,
            @RequestParam("assetFile") MultipartFile assetFile,
            @RequestParam("termsAccepted") boolean termsAccepted,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (user == null) {
            response.sendError(403, "NOT_LOGGED_IN");
        }

        if (packageName.isEmpty()) { response.sendError(403, "NO_PACKAGE_NAME"); }
        if (licenseType.isEmpty()) { response.sendError(403, "NO_LICENSE"); }
        if (showcaseImage.isEmpty()) { response.sendError(403, "NO_SHOWCASE_IMAGE"); }
        if (versionDescription.isEmpty()) { response.sendError(403, "NO_VERSION_DESCRIPTION"); }
        if (packageReadme.isEmpty()) { response.sendError(403, "NO_README"); }
        if (assetFile.isEmpty()) { response.sendError(403, "UPLOADED_FILE_EMPTY"); }
        if (!termsAccepted) { response.sendError(403, "TERMS_NOT_ACCEPTED"); }

        Path uploadsPath = JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getUploadsPath();
        Path serverFilePath = Paths.get(uploadsPath.toString(), "assets", packageName + ".jar");

        try (FileOutputStream fos = new FileOutputStream(serverFilePath.toFile()); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(assetFile.getBytes());
        }

        PotentialAsset pa = new PotentialAsset(
                user.getDiscourseId(),
                packageName,
                licenseType,
                showcaseImage,
                versionDescription,
                packageReadme,
                serverFilePath.toString());

        AssetManager assetManager = new AssetManager();
        assetManager.savePotentialAsset(pa);

        return pa;
    }

    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    @ResponseBody
    public String displayUserAssets(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateFileNotFoundException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "user",
                "my_assets.html")
                .toFile();

        if (!htmlFile.exists()) {
            throw new ResourceNotFoundException();
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("Upload Resource")
                .AddLineToHeader("<script src=\"/theme/lib/filestyle-1.2.1/bootstrap-filestyle.min.js\"></script>")
                .useSessionData(request.getCookies())
                .build();
    }

    @RequestMapping(value = "/{user}/{packageName}/", method = RequestMethod.GET)
    @ResponseBody
    public String displayUserAsset(@PathVariable String user, @PathVariable String packageName, HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateFileNotFoundException {

        File htmlFile = Paths.get(
                JmeResourceWebsite.getInstance().getConfiguration().getWebsiteConfig().getHtmlPath().toString(),
                "user_asset.html")
                .toFile();

        if (!htmlFile.exists()) {
            throw new ResourceNotFoundException();
        }

        return new TemplatedWebPage(htmlFile)
                .setPageTitle("View Asset")
                .useSessionData(request.getCookies())
                .AddLineToHeader("<meta name='asset-data' data-user='" + user + "' data-package='" + packageName + "' />")
                .build();
    }

    @RequestMapping(value = "/{user}/{packageName}/edit/")
    @ResponseBody
    public String editUserAsset(@PathVariable String username, @PathVariable String packageName, HttpServletRequest request, HttpServletResponse response) throws IOException, InsufficientPermissionException {

        User user = new SessionInspector().getUser(request.getCookies());

        if (user == null) {

            throw new InsufficientPermissionException();
        }

        AssetInfo assetInfo = new AssetManager().getAssetInfo(user.getUsername(), packageName);

        if (assetInfo == null) {
            response.sendError(HttpStatus.NOT_FOUND_404, "User '" + username + "' does not have an asset with package name '" + packageName + "'.");
        }

        if (assetInfo.getUser().getDiscourseId() != user.getDiscourseId()) {
            throw new InsufficientPermissionException();
        }


    }

}
