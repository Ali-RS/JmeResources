<%@ include file="../overall_header.jsp" %>

<div class="row">
    <div class="col-sm-12">
        <h3>Edit Potential Asset</h3>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">

        <form action="/potentialasset/${user.username}/${potentialAsset.packageName}/edit/" method="post" enctype="multipart/form-data">

            <div class="form-group">
                <label for="description">Description</label>
                <input type="text" class="form-control" id="description" name="description" value="${potentialAsset.versionDescription}" />
            </div>

            <div class="form-group">
                <label for="showcaseImage">Showcase Image</label>
                <input type="text" class="form-control" id="showcaseImage" name="showcaseImage" value="${potentialAsset.showcaseImage}" />
            </div>

            <div class="form-group">
                <label for="readme">Readme</label>
                <textarea style="height: 300px;" class="form-control" id="readme" name="readme"><c:out value="${potentialAsset.packageReadme}" /></textarea>
            </div>

            <button type="submit" class="btn btn-success">Save Changes</button>

        </form>

    </div>
</div>

<%@ include file="../overall_footer.jsp" %>