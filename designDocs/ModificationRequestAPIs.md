# Modification Requests to Database

## All actions are about to modify the database.


#### An instructor wants to:
- Create an assignment folder.
- Select lines of some code and leave a comment.

#### A student wants to:
- Make a submission by uploading one or more files.


## URLs.

#### An instructor URLs:
- `/CodeReviewTool/services/createassignment/{"title" : "Implement Hash-Table" }`. POST method returns an updated list of assignments. We expect that an id of newer assignment with always greater the id of an older one.
- `/CodeReviewTool/services/comment/{"file_id" : "42", "start" : 50, "end" : 57, "comment" : "You forgot to check corner case"}`. Should return OK message. The author of comment should be extracted from HTTP SESSION.

#### A student wants to:
- `/CodeReviewTool/services/upload/{"assignment_id" : 007, "files" : [{"filename" : "a.cpp", "content" : "#include"}, {"filename" : "a.h", "content" : "#include"}] } `. So submission upload is attached to specific assignment id and will contain an array of JSON object which are file object with name and content.
