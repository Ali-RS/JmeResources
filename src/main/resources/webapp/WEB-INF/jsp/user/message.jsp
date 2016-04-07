<%@ include file="../overall_header.jsp" %>

<script type="text/javascript">

    $(document).ready(function() {

        $("#replyButton").click(function() {

            $("#replyMessage").prop("disabled", true);

            $.ajax({
                type: "POST",
                url: "/api/messages/reply/${message.id}/",
                data: JSON.stringify({ message: $("#replyMessage").val() }),
                contentType: "application/json",
                success: function(data, status) {
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    bootbox.alert(error);
                },
                complete: function() {
                    $("#replyMessage").prop("disabled", false);
                }
            })

        });

    });

</script>

<div class="row">
    <div class="col-sm-12">
        <h4><c:out value="${message.title}" /> <small>by <c:out value="${message.author.username}" /></small></h4>
        <p>
            <c:out value="${message.message}" />
        </p>
    </div>
</div>

<div class="row">

    <div class="col-sm-12">
        <ul id="message-reply">
            <c:forEach items="${message.replies}" var="reply">
                <li>
                    <small>by <c:out value="${reply.author.username}" /></small>
                    <br/>
                    <c:out value="${reply.message}" />
                </li>
            </c:forEach>
        </ul>
    </div>

</div>

<div class="row">
    <div class="col-sm-12">
        <hr>
    </div>
</div>

<div class="row" style="background-color: #282828;">
    <div class="col-sm-12">

        <h4>Reply</h4>

        <div class="form-group">
            <textarea id="replyMessage" style="height: 150px;" class="form-control"></textarea>
        </div>

        <div class="form-group">
            <button type="button" id="replyButton" class="btn btn-success"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span> Reply</button>
        </div>

    </div>
</div>

<%@ include file="../overall_footer.jsp" %>