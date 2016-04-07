<%@ include file="../overall_header.jsp" %>

<script type="text/javascript">

    function resubmitPotentialAsset(submitUrl) {

        $.ajax({
            type: "POST",
            url: submitUrl,
            success: function() {
                location.reload();
            }
        });
    }

</script>

<div class="row">
    <div class="col-sm-10">
        <h2>My Assets</h2>
        <hr>
    </div>
    <div class="col-sm-2">
        <a href="/asset/upload/" class="btn btn-success"><span class="glyphicon glyphicon-upload" aria-hidden="true"></span> Upload New Asset</a>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">

        <h4>Pending Approval</h4>

        <c:if test="${not empty pendingAssets}">
            <table class="table table-condensed table-striped">

                <tbody>
                    <c:forEach items="${pendingAssets}" var="pendingAsset">

                        <tr>
                            <td><c:out value="${pendingAsset.packageName}" /></td>
                            <td><c:out value="${pendingAsset.dateUploaded}" /></td>

                            <td>
                                <c:if test="${pendingAsset.rejected eq true}">

                                    <div class="btn-group">
                                        <a class="btn btn-xs btn-primary" href="/potentialasset/${user.username}/${pendingAsset.packageName}/edit/">edit</a>
                                        <button class="btn btn-xs btn-success" onclick="resubmitPotentialAsset('/potentialasset/${user.username}/${pendingAsset.packageName}/resubmit/')">re-submit</button>
                                    </div>

                                </c:if>
                            </td>

                            <td>
                                <c:if test="${pendingAsset.rejected eq true}">
                                    <span class="badge btn-warning">REJECTED</span>
                                </c:if>
                                <c:if test="${pendingAsset.rejected eq false}">
                                    <span class="badge btn-success">Awaiting Approval</span>
                                </c:if>
                            </td>
                        </tr>

                    </c:forEach>
                </tbody>

            </table>
        </c:if>

        <c:if test="${empty pendingAssets}">
            You do not have any assets awaiting approval.
        </c:if>

    </div>
</div>

<div class="row">
    <br/><br/>
</div>

<div class="row">
    <div class="col-sm-12">

        <h4>Approved Assets</h4>
        <hr>

        <c:if test="${not empty assets}">

            <table class="table table-condensed table-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Versions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${assets}" var="assetInfo">
                        <tr>
                            <td><c:out value="${assetInfo.asset.packageName}" /></td>
                            <td>
                                <ul class="list-unstyled">
                                    <c:forEach items="${assetInfo.versions}" var="version">
                                        <li style="margin: 6px 0px;">
                                            <span class="badge"><c:out value="${version.version}" /></span>

                                            <div class="btn-group">
                                                <a class="btn btn-xs btn-success" href="/asset/${assetInfo.user.username}/${assetInfo.asset.packageName}/${version.version}/">view</a>
                                                <a class="btn btn-xs btn-primary" href="/asset/${assetInfo.user.username}/${assetInfo.asset.packageName}/${version.version}/edit/">edit</a>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>

            </table>

        </c:if>

        <c:if test="${empty assets}">
            You have not uploaded any assets.
        </c:if>

    </div>
</div>

<%@ include file="../overall_footer.jsp" %>