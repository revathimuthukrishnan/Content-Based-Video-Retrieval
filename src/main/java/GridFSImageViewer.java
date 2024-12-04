import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import org.bson.types.ObjectId;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GridFSImageViewer extends JFrame {

    private JLabel imageLabel;

    public GridFSImageViewer() {
        setTitle("GridFS Image Viewer");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);
    }

    private void displayImage(BufferedImage img) {
        if (img != null) {
            ImageIcon icon = new ImageIcon(img);
            imageLabel.setIcon(icon);
        } else {
            JOptionPane.showMessageDialog(this, "Image not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BufferedImage getImageFromGridFS(ObjectId imageId) {
        BufferedImage img = null;

        String connectionString = "mongodb+srv://lrms:8483@cluster0.xhhpnqo.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        String dbName = "IMVA";
        String bucketName = "fs";  // GridFS bucket name

        try {
            ConnectionString connString = new ConnectionString(connectionString);
            try (com.mongodb.client.MongoClient mongoClient = MongoClients.create(connString)) {
                GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(dbName), bucketName);

                GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                gridFSBucket.downloadToStream(String.valueOf(imageId), outputStream, downloadOptions);

                byte[] imageBytes = outputStream.toByteArray();
                InputStream is = new ByteArrayInputStream(imageBytes);
                img = ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GridFSImageViewer viewer = new GridFSImageViewer();
            viewer.setVisible(true);

            // Replace with the ObjectId of the image you want to display
            ObjectId imageId = new ObjectId("668b938bc23392c7b98d8db5");
            BufferedImage img = viewer.getImageFromGridFS(imageId);
            viewer.displayImage(img);
        });
    }
}
