<%@ include file="../overall_header.jsp" %>

<div class="row">
    <div class="col-sm-12">
        
        <h3>Bintray Configuration</h3>
        <hr>
        
        <form class="form-horizontal" action="/admin/bintray/" method="post" enctype="multipart/form-data">
            
            <div class="form-group">
                <label for="bintrayUser" class="col-sm-2 control-label">User</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="bintrayUser" name="bintrayUser" value="${config.user}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="bintrayApiKey" class="col-sm-2 control-label">Api Key</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="bintrayApiKey" name="bintrayApiKey" value="${config.apiKey}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="bintraySubject" class="col-sm-2 control-label">Subject</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="bintraySubject" name="bintraySubject" value="${config.subject}">
                </div>
            </div>
            
            <div class="form-group">
                <label for="bintrayRepo" class="col-sm-2 control-label">Repository</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="bintrayRepo" name="bintrayRepo" value="${config.repo}">
                </div>
            </div>
            
            <div class="col-sm-offset-2 col-sm-10">
                <button style="margin-left: -10px;" type="submit" class="btn btn-success">Save Changes</button>
            </div>
        </form>
        
    </div>
</div>

<%@ include file="../overall_footer.jsp" %>