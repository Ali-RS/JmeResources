<%@ include file="../overall_header.jsp" %>



<div class="row">
    <div class="col-sm-12">

        <h3><c:out value="${assetInfo.asset.packageName}" /></h3>
        <hr>

        <form action="/asset/${assetInfo.user.username}/${assetInfo.asset.packageName}/${assetInfo.versions[0].version}/edit/" method="post" enctype="multipart/form-data">

            <div class="form-group">
                <label for="versionDesc">Description</label>
                <input type="text" class="form-control" id="versionDesc" name="versionDesc" value="${assetInfo.versions[0].description}" />
            </div>

            <div class="form-group">
                <label for="versionImage">Showcase Image</label>
                <input type="text" class="form-control" id="versionImage" name="versionImage" value="${assetInfo.versions[0].showcaseImage}" />
            </div>

            <div class="form-group">
                <label for="readme">Readme</label>
                <textarea style="height: 300px;" class="form-control" id="readme" name="readme"><c:out value="${assetInfo.asset.readme}"/></textarea>
            </div>

            <button type="submit" class="btn btn-primary">Save Changes</button>

        </form>


    </div>

</div>


<%@ include file="../overall_footer.jsp" %>