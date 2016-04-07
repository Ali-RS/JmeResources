<%@ include file="../overall_header.jsp" %>

<div class="row">
    <div class="col-sm-12">
        
        <h4>General Configuration</h4>
        <hr>
        
        
        
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        
        <h3>Administrators</h3>
        <hr>
        
        <ul>
           <c:forEach items="${admins}" var="administrator">
               <li>${administrator.username}</li>
           </c:forEach>
        </ul>
        
        <form class="form" method="post" enctype="multipart/form-data">
            
            <div class="form-group">
                
                <div class="input-group">
                    <input type="text" class="form-control" name="adminUsername">
                    
                    <span class="input-group-btn">
                        <button type="submit" formaction="/admin/admins/add/" class="btn btn-success">Add</button>
                        <button type="submit" formaction="/admin/admins/remove/" class="btn btn-danger">Remove</button>
                    </span>
                </div>

            </div>
            
        </form>
    </div>
</div>

<%@ include file="../overall_footer.jsp" %>