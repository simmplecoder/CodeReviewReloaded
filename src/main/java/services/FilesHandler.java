package services;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.mongodb.util.JSON;
import commented_code.Comment;
import commented_code.CommentedCode;
import commented_code.FileTemp;
import commented_code.Submission;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import representations.FilesRequestFormat;
import representations.SubmissionsRequestFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Path("")
public class FilesHandler {
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext context;

    private MongoClient mongoClient = null;


    public FilesHandler() {
        mongoClient = new MongoClient();
        checking();
    }

    @POST
    @Path("files")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssignments(String json) {
        Response redirection = new RedirectionHandler(request).redirection(true);
        if (redirection != null) {
            return redirection;
        }

        FilesRequestFormat rf = new Gson().fromJson(json, FilesRequestFormat.class);

        // a new try.s
        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> coll = db.getCollection("commented_code");

        System.out.println("request submission id is " + rf.submission_id);

        Bson bsonFilter = Filters.eq("submissionId", rf.submission_id);
        FindIterable<Document> findIt = coll.find(bsonFilter);
        MongoCursor<Document> cursor = findIt.iterator();

        List<CommentedCode> files = new ArrayList<>();
        while (cursor.hasNext()) {
            Document temp = cursor.next();
            CommentedCode file = new Gson().fromJson(temp.toJson(), commented_code.CommentedCode.class);
//            System.out.println("submission id of file " + file.getSubmissionId());
            file.setId(temp.get("_id").toString());
            files.add(file);
        }

        return Response.ok(new Gson().toJson(files), MediaType.APPLICATION_JSON_TYPE).build();
    }

    void checking() {
        MongoDatabase db = mongoClient.getDatabase("CodeReviewTool");
        MongoCollection<Document> coll = db.getCollection("commented_code");

        CommentedCode code = new CommentedCode("#include <iostream>\n" +
                "using namespace std;\n" +
                "\n" +
                "template <typename T>\n" +
                "struct Node {\n" +
                "    T data;\n" +
                "    struct Node* link;\n" +
                "    \n" +
                "    Node<T>(T data) : data(data), link(NULL) {}\n" +
                "};\n" +
                "\n" +
                "template <typename T>\n" +
                "class Stack {\n" +
                "private:\n" +
                "    Node<T> *head;\n" +
                "    int size;\n" +
                "public:\n" +
                "    Stack<T>() : head(NULL), size(0) {}\n" +
                "    \n" +
                "    void push(T data) {\n" +
                "        Node<T> *node = new Node<T>(data);\n" +
                "        node -> link = head;\n" +
                "        head = node;\n" +
                "        size = size + 1;\n" +
                "    }\n" +
                "    \n" +
                "    T pop() {\n" +
                "        // Saving the data of the top element.\n" +
                "        T data = head -> data;\n" +
                "        // Saving the next element.\n" +
                "        Node<T> *link = head -> link;\n" +
                "        // Freeing memory of the top element.\n" +
                "        delete head;\n" +
                "        // Shifting to the next element.\n" +
                "        head = link;\n" +
                "        \n" +
                "        // Decreasing the size of the stack.\n" +
                "        size = size - 1;\n" +
                "        \n" +
                "        return data;\n" +
                "    }\n" +
                "    \n" +
                "    const bool isEmpty() {\n" +
                "        return size == 0;\n" +
                "    }\n" +
                "    \n" +
                "    const int getSize() {\n" +
                "        return size;\n" +
                "    }\n" +
                "    \n" +
                "    ~Stack() {\n" +
                "        Node<T> *temp = NULL;\n" +
                "        while (head) {\n" +
                "            temp = head -> link;\n" +
                "            delete head;\n" +
                "            head = temp;\n" +
                "        }\n" +
                "        delete temp;\n" +
                "        cout << \"The stack is completely deleted!\" << endl;\n" +
                "    }\n" +
                "};\n" +
                "\n" +
                "template <typename T>\n" +
                "class Queue {\n" +
                "private:\n" +
                "    int size;\n" +
                "    Stack<T> *stackFirstOnTop;\n" +
                "    Stack<T> *stackLastOnTop;\n" +
                "public:\n" +
                "    Queue() : size(0), stackFirstOnTop(new Stack<T>()), stackLastOnTop(new Stack<T>()) {}\n" +
                "    \n" +
                "    void insert(T data) {\n" +
                "        stackLastOnTop -> push(data);\n" +
                "        size = size + 1;\n" +
                "    }\n" +
                "    \n" +
                "    T enqueue() {\n" +
                "        while (!stackLastOnTop -> isEmpty()) {\n" +
                "            stackFirstOnTop -> push(stackLastOnTop -> pop());\n" +
                "        }\n" +
                "        size = size - 1;\n" +
                "        return stackFirstOnTop -> pop();\n" +
                "    }\n" +
                "    \n" +
                "    const int getSize() {\n" +
                "        return size;\n" +
                "    }\n" +
                "};\n" +
                "\n" +
                "void foo() {\n" +
                "    Queue<int> q;\n" +
                "    \n" +
                "    int N = 100000;\n" +
                "    for (int i = 0; i < N; i++) {\n" +
                "        q.insert(i);\n" +
                "    }\n" +
                "    \n" +
                "    for (int i = 0; i < N; i++) {\n" +
                "        cout << q.enqueue() << \" \";\n" +
                "    }\n" +
                "    \n" +
                "    cout << endl;\n" +
                "}\n" +
                "\n" +
                "int main(int argc, const char * argv[]) {\n" +
                "\n" +
                "    foo();\n" +
                "    \n" +
                "    return 0;\n" +
                "}\n");

        Comment comment1 = new Comment("cool job!", "ymademikhanov", 1, 10);
        Comment comment3 = new Comment("I really appreciate it =)", "ymademikhanov", 7, 10);
        Comment comment2 = new Comment("how to check whether it agnostic?", "ymademikhanov", 40, 50);

        code.addComment(comment1);
        code.addComment(comment2);

        code.setSubmissionId("5b1828958282383fd7ce81db");
        code.setName("queue.cpp");

        Document d = Document.parse(new Gson().toJson(code));

        coll.insertOne(d);


    }
}
