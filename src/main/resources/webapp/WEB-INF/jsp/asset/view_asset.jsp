<%@ include file="../overall_header.jsp" %>

<script type="text/javascript">
    function buildTypeClick(val) {
        document.getElementById("buildTypeButton").innerHTML = val + ' <span class="caret"></span>';
    }
</script>

<div class="row">
    <div class="col-sm-12">
        <h2><c:out value="${assetInfo.asset.packageName}" /></h2>
    </div>
</div>

<div class="row">

    <div class="col-sm-8">
        <div style="background-color: #000">
            <img class="center-block" style="max-width: 100%; max-height: 353px;" src="${assetInfo.versions[0].showcaseImage}" />
        </div>

        <br/><br/>

        <div class="panel panel-default">
            <div class="panel-body">
                <c:out value="${assetInfo.versions[0].description}" />
            </div>
        </div>
    </div>

    <div class="col-sm-4">
        <div class="panel panel-default">
            <div class="panel-body">

                <a href="#" class="btn btn-success" title="Download .jar"><span class="glyphicon glyphicon-download" aria-hidden="true"></span> Download</a>

                <div class="btn-group">
                    <button type="button" id="buildTypeButton" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Maven <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a href="#" onClick="buildTypeClick(this.innerText)">Maven</a></li>
                        <li><a href="#" onClick="buildTypeClick(this.innerText)">Gradle</a></li>
                        <li><a href="#" onClick="buildTypeClick(this.innerText)">Ivy</a></li>
                    </ul>
                </div>

                <hr>

                <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                Uploaded by <c:out value="${assetInfo.user.username}" /> on <fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm" value="${assetInfo.versions[0].dateUploaded}" />

            </div>

        </div>
    </div>

</div>

<div class="row">

    <div class="col-sm-12">
        <br>

        <c:out value="${assetInfo.asset.readme}" />

    </div>

</div>

<%@ include file="../overall_footer.jsp" %>