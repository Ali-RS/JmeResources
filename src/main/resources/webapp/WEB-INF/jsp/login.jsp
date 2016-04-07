<%@ include file="overall_header.jsp" %>

<c:if test="${loggedIn eq true}">

    <div class="row">
        <div class="col-sm-12">
            You are already logged in.
        </div>
    </div>

</c:if>

<c:if test="${loggedIn eq false}">

    <script type="text/javascript">

        $(document).ready(function() {

            $("#login-form").submit(function(event) {

                event.preventDefault();
                $("#login-form :input").prop("disabled", true);

                $.ajax({
                    type: "POST",
                    url: "/login/",
                    data: JSON.stringify({ user: $("#user").val(), password: $("#password").val() }),
                    contentType: 'application/json',
                    success: function(data, status) {
                        window.location.href = "/";
                    },
                    error: function(xhr, status, error) {
                        bootbox.alert("Invalid username or password specified.");
                        $("#login-form :input").prop("disabled", false);
                    }
                });

            });

        });

    </script>

    <div class="row">
        <div class="col-sm-offset-2 col-sm-5">
            Please log in using your credentials from <a href="http://hub.jmonkeyengine.org">hub.jmonkeyengine.org</a>.
            <br><br>
        </div>
    </div>

    <div class="row">

        <form class="form-horizontal" id="login-form" method="post">

            <div class="form-group">
                <label for="user" class="col-sm-2 control-label">Username / Email</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control" name="user" id="user" placeholder="username / email address">
                </div>
            </div>

            <div class="form-group">
                <label for="password" class="col-sm-2 control-label">Password</label>
                <div class="col-sm-5">
                    <input type="password" class="form-control" name="password" id="password" placeholder="password">
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-success">Log in</button>
                </div>
            </div>

        </form>
    </div>

</c:if>

<%@ include file="overall_footer.jsp" %>