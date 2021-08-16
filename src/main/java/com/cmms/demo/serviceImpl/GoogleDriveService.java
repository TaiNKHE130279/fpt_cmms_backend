package com.cmms.demo.serviceImpl;


import com.cmms.demo.utils.GoogleDriveUtils;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GoogleDriveService {

    static Drive driveService;

    static {
        try {
            driveService = GoogleDriveUtils.getDriveService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleSubFolders(String googleFolderIdParent) {
        List<File> list = new ArrayList<>();
        try {
            String pageToken = null;

            String query = null;
            if (googleFolderIdParent == null) {
                query = " mimeType = 'application/vnd.google-apps.folder' " //
                        + " and 'root' in parents";
            } else {
                query = " mimeType = 'application/vnd.google-apps.folder' " //
                        + " and '" + googleFolderIdParent + "' in parents";
            }
            do {
                FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                        // Fields will be assigned values: id, name, createdTime
                        .setFields("nextPageToken, files(id, name, createdTime)")//
                        .setPageToken(pageToken).execute();
                for (File file : result.getFiles()) {
                    list.add(file);
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleSubFolderByName(String googleFolderIdParent, String subFolderName)
            throws IOException {

        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
        if (googleFolderIdParent == null) {
            query = " name = '" + subFolderName + "' " //
                    + " and mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " name = '" + subFolderName + "' " //
                    + " and mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    public String createGoogleFolder(String folderName){
        File fileMetadata = new File();
        String mimeType = "application/vnd.google-apps.folder";
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType(mimeType);

        File file = null;
        try {
            file = driveService.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Folder ID: " + file.getId());

        return file.getId();
    }

    public void getListFile() throws IOException {
        FileList result = driveService.files().list()
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        System.out.println("hello2");
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }
    public String uploadFile(String folderId,String img_path,String file_type) throws IOException {

        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File(img_path);
        FileContent mediaContent = new FileContent(file_type, filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + file.getId());
        return file.getId();
    }
    public String uploadFile2(String folderId, java.io.File file, String file_type){
        File file1 = new File();
        file1.setName(file.getName());
        file1.setParents(Collections.singletonList(folderId));
        FileContent content = new FileContent(file_type,file);
        File fileX = null;
        try {
            fileX = driveService.files().create(file1, content)
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File ID: " + fileX.getId());
        return fileX.getId();
    }
    public String createSubFile(String folderParentId,String folderName){
        File fileMetadata = new File();
        String mimeType = "application/vnd.google-apps.folder";
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType(mimeType);
        fileMetadata.setParents(Collections.singletonList(folderParentId));
        File file = null;
        try {
            file = driveService.files().create(fileMetadata)
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Folder ID: " + file.getId());
        return file.getId();
    }

    public void deleteFile(String fileId) {
        try {
            driveService.files().delete(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    public static void main(String[] args) throws IOException {

        GoogleDriveService driveServiceMain = new GoogleDriveService();
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        System.out.println(format.format(new Date(System.currentTimeMillis())));
        // driveServiceMain.uploadFile("1A5eDPXwYOsKmlDUcIKf3GN8lSsAyUpyU","E:\\Image\\4ca99c708e04bbc73a2d40b429e541e1nguoi-anh-em-cho-xin-cai-dia-chi-dau-cat-moi.jpg","image/jpeg");
        driveServiceMain.createSubFile("1A5eDPXwYOsKmlDUcIKf3GN8lSsAyUpyU","XYZ");
    }

}
