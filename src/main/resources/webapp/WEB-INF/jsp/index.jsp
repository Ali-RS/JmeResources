<%@ include file="overall_header.jsp" %>

<div class="row">
    <div class="col-sm-12">

        <c:if test="${not empty assets}">
            <table class="table table-condensed">

                <thead>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Versions</th>
                    <th>Description</th>
                </tr>
                </thead>

                <c:forEach items="${assets}" var="assetInfo">

                    <tr>
                        <td>
                            <div style="background: #000; max-width: 200px;">
                                <img class="center-block index-thumbnail" src="${assetInfo.versions[fn:length(assetInfo.versions) - 1].showcaseImage}" />
                            </div>
                        </td>

                        <td>
                            <a title="View the latest version" href="/asset/${assetInfo.user.username}/${assetInfo.asset.packageName}/${assetInfo.versions[fn:length(assetInfo.versions) - 1].version}/"><c:out value="${assetInfo.asset.packageName}" /></a>
                        </td>

                        </td>
                        <td>


                            <ul class="list-unstyled">
                                <c:forEach items="${assetInfo.versions}" var="version">
                                    <li>
                                        <a title="View version ${version.version}" href="/asset/${assetInfo.user.username}/${assetInfo.asset.packageName}/${version.version}/">
                                            <c:out value="${version.version}" />
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>

                        </td>
                        <td>
                            <c:out value="${assetInfo.versions[fn:length(assetInfo.versions) - 1].description}" />
                            <div class="well well-sm" style="margin-top: 10px;">
                                Uploaded by ${assetInfo.user.username}
                                on <fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm" value="${assetInfo.versions[fn:length(assetInfo.versions) - 1].dateUploaded}" />

                            </div>
                        </td>
                    </tr>

                </c:forEach>

            </table>

        </c:if>

        <c:if test="${empty assets}">
            There are no assets to display at this time.
        </c:if>

    </div>
</div>

<%@ include file="overall_footer.jsp" %>