## Request Urls to Database Handler

The user can navigate in Code Review Tool from home page up to specific submission file. User is assummed to be authorized, otherwise to resources should be provided.

### `Student`:
#### The items that can be access in navigation by student:
- Access to list of registered courses by student.
- Access to list of assignments of a specific courses.
- Access to list of previous submissions (submission may contain several files).
- Access to list of files of a specific submission.
 
#### Urls to above use cases:
- `/CodeReviewTool/services/courses/{"email" : "student@nu.edu.kz"}` -> should return list of courses as jsonArray
- `/CodeReviewTool/services/assignments/{"email" : "student@nu.edu.kz", "course_id" : "course_id_10"}` -> should return list of assignments as jsonArray
- `/CodeReviewTool/services/submissions/{"email" : "student@nu.edu.kz", "assignment" : "assignment_id_24"}` -> should return list of previous submissions as jsonArray
- `/CodeReviewTool/services/files/{"email" : "student@nu.edu.kz", "submission_id" : "submission100100"}` -> should return list of files of a submission as jsonArray

*** {} - are params sent as JSON.

### `Instructor`:
#### The items that can be access in navigation of instructor:
- Access to list of created courses.
- Access to list of assignments of a course.
- Access to list of student submissions.
- Access to list of files of a specific student submission.
 
#### Urls to above use cases:
- `/CodeReviewTool/services/courses/{"email" : "student@nu.edu.kz"}` -> -> should return list of courses as jsonArray
- `/CodeReviewTool/services/assignments/{"email" : "student@nu.edu.kz", "course_id" : "course_id_10"}` -> should return list of assignments as jsonArray
- `/CodeReviewTool/services/submissions/{"email" : "student@nu.edu.kz", "assignment" : "assignment_id_24"}` -> should return list of all submissions for an assingment as jsonArray
- `/CodeReviewTool/services/files/{"email" : "student@nu.edu.kz", "submission_id" : "submission100100"}` -> should return list of files of a submission as jsonArray

*** {} - are params sent as JSON.
