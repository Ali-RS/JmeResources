<%@ include file="../overall_header.jsp" %>

<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-abbr.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-container.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-deflist.min.js"></script>
<!--<script type="text/javascript" src="/theme/lib/markdown-it-6.0.0/markdown-it-emoji.min.js"></script>-->
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-footnote.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-ins.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-mark.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-sub.min.js"></script>
<script type="text/javascript" src="/lib/markdown-it-6.0.0/markdown-it-sup.min.js"></script>

<script type="text/javascript" src="https://twemoji.maxcdn.com/twemoji.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/highlight.js/8.4.0/highlight.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/highlight.js/8.4.0/styles/hybrid.min.css">

<script type="text/javascript" src="/js/markdown-it-init.js"></script>

<script type="text/javascript">

    function viewShowcaseImage(image) {

        var data = "<img src='" + image + "' alt='showcase image' style='max-height: 100%; max-width: 100%;' />";

        bootbox.alert({
            size: "large",
            title: "Showcase Image",
            message: data
        });
    }

     function viewReadme(readme) {

        var resultInline = markdown.render(readme);

        bootbox.alert({
            size: "large",
            title: "Asset Readme",
            message: resultInline
        })

    }

    function approvePotentialAsset(id) {

        $.ajax({
            method: "POST",
            url: "/api/potentialasset/approve/",
            data: JSON.stringify({ assetId: id }),
            success: function(data, status) {

            },
            error: function(xhr, status, error) {

            }
        });
    }


    function deletePotentialAsset(id, reason) {

        $.ajax({
            method: "POST",
            url: "/api/potentialasset/delete/",
            data: JSON.stringify({ assetId: id, reason: reason }),
            success: function(data, status) {

            },
            error: function(xhr, status, error) {

            }
        });
    }

    $(document).ready(function(){

        $(".paReadme").each(function() {

            var resultInline = markdown.render(this.innerHTML);
            this.innerHTML = resultInline;
        })


        $(".reject-pa-form").each(function() {

            var $this = $(this);

            $($this).submit(function(event) {

                event.preventDefault();

                var fData = new FormData(event.target);

                $.ajax({
                    type: "POST",
                    url: "/api/potentialasset/reject/",
                    data: fData,
                    contentType: false,
                    processData: false,
                    success: function(data, status) {
                        location.reload(true);
                    },
                    error: function(xhr, status, error) {
                        bootbox.alert(error);
                    }
                });

            })

        });



    });

</script>

<div class="row">
    <div class="col-sm-12">
        <h2>Potential Assets</h2>
        These assets are awaiting approval. They are not available or visible to the public until they
        are approved by a member of staff.

        <hr>
    </div>
</div>

<c:if test="${empty potentialAssets}">
    <div class="row">
        <div class="col-sm-12">
            There are no assets pending approval at this time.
        </div>
    </div>
</c:if>

<c:forEach items="${potentialAssets}" var="pa">
    <div class="row">
        <div class="col-sm-8">
            <h3><a href="#" data-toggle="collapse" data-target="#pa${pa.potentialAsset.id}"><c:out value="${pa.potentialAsset.packageName}" /></a></h3>

            <div id="pa${pa.potentialAsset.id}" class="collapse">
                <span>Uploaded by <em><c:out value="${pa.user.username}" /></em> on <em><c:out value="${pa.potentialAsset.dateUploaded}" /></em></span>

                <p style="margin-top: 10px;">
                    <button class="btn btn-primary" data-toggle="modal" data-target="#structureModal${pa.potentialAsset.id}"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;Structure</button>
                    <button class="btn btn-primary" onclick="viewShowcaseImage('<c:out value='${pa.potentialAsset.showcaseImage}' />')"><span class="glyphicon glyphicon-picture" aria-hidden="true"></span>&nbsp;Showcase Image</button>
                </p>

                <div id="structureModal${pa.potentialAsset.id}" class="modal fade" role="dialog">
                    <div class="modal-dialog modal-lg">

                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Asset Structure</h4>
                            </div>
                            <div class="modal-body codefont">

                                <c:forEach items="${pa.jarEntries}" var="dir">
                                    <c:out value="${dir}" />
                                </c:forEach>

                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
                            </div>
                        </div>

                    </div>
                </div>

                <p>
                    <h5>Description</h5>
                    <hr>
                    <c:out value="${pa.potentialAsset.versionDescription}" />
                    <hr>
                </p>

                <p>
                    <h5>Readme</h5>
                    <div class="paReadme">
                        <c:out value="${pa.potentialAsset.packageReadme}" />
                    </div>
                </p>

            </div>
        </div>

        <div class="col-sm-4">
            <br>
            <button class="btn btn-success" onclick="approvePotentialAsset(${pa.potentialAsset.id})"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Approve</button>
            <button class="btn btn-warning" data-toggle="modal" data-target="#rejectModal${pa.potentialAsset.id}"><span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span> Reject</button>
            <button class="btn btn-danger" onclick="deletetPotentialAsset(${pa.potentialAsset.id})"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</button>
            <br><br>

            <div id="rejectModal${pa.potentialAsset.id}" class="modal fade" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">Reject <c:out value="${pa.potentialAsset.packageName}" /></h4>
                        </div>
                        <form class="reject-pa-form">
                            <div class="modal-body">
                                <p>
                                    Please provide a reason below. This information will be given to the user.
                                </p>

                                <input type="hidden" name="potentialAssetId" value="${pa.potentialAsset.id}" />
                                <textarea name="reason" class="form-control" style="height: 250px;"></textarea>

                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-warning">Reject Potential Asset</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>

    </div>
</c:forEach>

<%@ include file="../overall_footer.jsp" %>