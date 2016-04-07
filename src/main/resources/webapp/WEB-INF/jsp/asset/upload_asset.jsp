<%@ include file="../overall_header.jsp" %>

<script type="text/javascript" src="/lib/filestyle-1.2.1/bootstrap-filestyle.min.js"></script>

<script type="text/javascript">

    function checkFileSize(maxSize) {

        var fileSize = document.getElementById("assetFile").files[0].size; // in bytes

        if(fileSize < maxSize) {
            return true;
        }

        bootbox.alert('file size is more than ' + maxSize + ' bytes');
        return false;
    }

    $(document).ready(function() {

        $('[data-toggle="tooltip"]').tooltip({ placement: "auto top" });

        $("#uploadFile").change(function() {
            switch (this.value.match(/\.([^\.]+)$/)[1]) {
                case 'jar': break;
                default:
                {
                    $(":file").filestyle('clear');
                    bootbox.alert('Only jar files (.jar) are allowed to be uploaded.');
                    $(this).trigger("change");
                    return false;
                }
            }

            // this is checked server-side too, but saves the user the hassle to check beforehand
            var maxSize = 52428800 // 50mb

            if (!checkFileSize(maxSize)) {
                $(":file").filestyle('clear');
                bootbox.alert("The size of the .jar must not exceed 50mb. Upload failed.");
                $(this).trigger("change");
                return false;
            }
        });
    });

</script>

<div class="row">
    <div class="col-sm-12">
        <h2>Upload Asset</h2>
        <hr>
        Please ensure that you have put all required files inside a single <span class="code codefont">.jar</span>. Assets can only be models, images, sounds, etc. Do
        not include any <span class="code codefont">.class</span> files, and form of advertisement, or licenses in your <span class="code codefont">.jar</span> file.
        <hr>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">

        <form id="upload-asset-form" action="/api/asset/upload/" method="post" enctype="multipart/form-data">

            <div class="form-group">
                <label for="packageName" data-toggle="tooltip" title="Package name may contain numbers, underscores and hypens only. No spaces allowed.">Package Name</label>
                <input class="form-control" name="packageName" id="packageName" type="text" pattern="[\w&-]+" required>
            </div>

            <div class="form-group">
                <label for="licenseType">License</label>
                <select class="form-control" name="licenseType" id="licenseType" required>
                    <option>CC0-1.0</option>
                    <option>Another License</option>
                    <option>Some other license</option>
                    <option>GPL</option>
                </select>
            </div>

            <div class="form-group">
                <label for="showcaseImage" data-toggle="tooltip" title="Please use an image that is at least 720p (1280x720) or higher.">Showcase Image</label>
                <input type="text" class="form-control" name="showcaseImage" id="showcaseImage" required>
            </div>

            <div class="form-group">
                <label for="versionDescription" data-toggle="tooltip" title="A short description of your asset.">Short Description</label>
                <input type="text" name="versionDescription" id="versionDescription" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="packageReadme" data-toggle="tooltip" title="Markdown support only!">Readme</label>
                <textarea class="form-control" name="packageReadme" id="packageReadme" style="height: 300px;" required></textarea>
            </div>

            <div class="form-group">
                <label for="assetFile">Select Your Asset - Maximum Size: 50mb</label>
                <input type="file" name="assetFile" id="assetFile" class="filestyle" data-icon="false" data-buttonBefore="true" valid-file data-buttonName="btn-primary" accept=".jar" required>
            </div>

            <div class="checkbox">
                <label>
                    <input type="checkbox" name="termsAccepted" required>
                    I have read and understood that I am only allowed to upload a set of specific files according to <a href="#">this article</a>.
                    I have provided as much information about the asset to the best of my knowledge, and are satisfied that my work is
                    of a standard that jMonkey will deem appropriate.
                </label>
            </div>

            <button type="submit" class="btn btn-primary">Upload My Asset</button>

        </form>

    </div>
</div>

<%@ include file="../overall_footer.jsp" %>