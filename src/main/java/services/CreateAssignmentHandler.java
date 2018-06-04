package services;

public class CreateAssignmentHandler {
//    @POST
//    @Path("createassignment")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createAssignment(String json) {
//        Response redirection = redirection(true);
//        if (redirection != null) {
//            return redirection;
//        }
//        if (request.getSession().getAttribute("isInstructor") == null
//                || request.getSession().getAttribute("isInstructor").equals(0)) {
//            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
//        }
//        JsonObject params = new JsonParser().parse(json).getAsJsonObject();
//        String description = params.get("description").getAsString();
//        String course_id = params.get("course_id").getAsString();
//        String title = params.get("title").getAsString();
//
//        try {
//            Statement stmt = MySQLConnection.connect().createStatement();
//            String sqlQuery =
//                    "SELECT * FROM user " +
//                            "WHERE email='" + request.getSession().getAttribute("username") + "';";
//            ResultSet rs = stmt.executeQuery(sqlQuery);
//            if (rs.next()) {
//                int id = rs.getInt("id");
//                sqlQuery = "INSERT INTO assignment(id, title, desc, course_id) " +
//                        "values(null, \""+title+"\", \""+description+"\", "+course_id+");";
//                stmt.executeUpdate(sqlQuery);
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return Response.ok().build();
//    }
}
