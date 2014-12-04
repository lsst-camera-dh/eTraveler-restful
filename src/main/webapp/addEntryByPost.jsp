<%-- 
    Document   : addEntry
    Created on : Nov 26, 2014, 4:56:50 PM
    Author     : jrb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Entry</title>
  </head>
  <body>
    <form>
      <label for="name">Name: </label>
      <input type="text" id="name" name="name"/> <br />   
       <input type="submit" value="submitName" />
    </form>
     <form method="post" action="http://localhost:8080/etraveler_restful/rest/example1/${param.name}"  >
   
      <label for="description">Description: </label>
      <input type="text" id="description" name="description"/> <br />
      <label for="value" >Value:</label>
      <input type="text" id="value" name="value"/>
      <input type="submit" value="submit" />
      <input type="reset" value="clear" />
    </form>
  </body>
</html>
