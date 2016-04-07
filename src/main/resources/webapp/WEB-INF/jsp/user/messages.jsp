<%@ include file="../overall_header.jsp" %>

<div class="row">
    <div class="col-sm-12">
        <h3>Private Messages</h3>
        <hr>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">

        <table class="table table-condensed table-striped">

            <tbody>

                <c:forEach items="${messages}" var="message">
                    <tr>
                        <td>
                            <a href="/messages/${message.id}/">
                                <c:out value="${message.title}" />
                            </a>
                        </td>
                        <td>from ${message.author.username}</td>
                    </tr>
                </c:forEach>

            </tbody>

        </table>

    </div>
</div>

<%@ include file="../overall_footer.jsp" %>