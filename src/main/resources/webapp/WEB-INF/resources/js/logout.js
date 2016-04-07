/* @author jayfella */

function logout() {
    $.ajax({
        type: "POST",
        url: "/login/logout/",
        success: function() {
            window.location.reload(true);
        }
    });
}
