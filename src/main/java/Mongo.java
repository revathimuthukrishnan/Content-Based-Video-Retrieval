import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

public class Mongo {

        // Method to retrieve image data from MongoDB based on imageId
        public byte[] retrieveImageById(ObjectId imageId) {
            // Replace with your MongoDB connection details
            String connectionString = "mongodb+srv://lrms:8483@cluster0.xhhpnqo.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
            String databaseName = "IMVA"; // Your MongoDB database name
            String bucketName = "fs"; // GridFS bucket name in MongoDB

            try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                MongoDatabase database = mongoClient.getDatabase(databaseName);
                GridFSBucket gridFSBucket = GridFSBuckets.create(database, bucketName);

                // Find the GridFS file by its ObjectId (imageId)
                GridFSFile gridFSFile = gridFSBucket.find(eq("_id", imageId)).first();
                if (gridFSFile != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    gridFSBucket.downloadToStream(imageId, outputStream);
                    return outputStream.toByteArray();
                } else {
                    System.out.println("Image not found in MongoDB for imageId: " + imageId);
                }
            }
            return null;
        }

        // Example usage
        public static void main(String[] args) {
            Mongo retriever = new Mongo();
            ObjectId hardcodedImageId = new ObjectId("668b938bc23392c7b98d8db5"); // Replace with your imageId
            byte[] imageData = retriever.retrieveImageById(hardcodedImageId);

            if (imageData != null) {
                // Convert image data to Base64 for display in web applications
                String base64Image = javax.xml.bind.DatatypeConverter.printBase64Binary(imageData);
                System.out.println("data:image/jpeg;base64," + base64Image);
            } else {
                System.out.println("Failed to retrieve image data.");
            }
        }
}


