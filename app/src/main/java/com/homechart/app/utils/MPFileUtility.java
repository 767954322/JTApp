package com.homechart.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * @author   kubern .
 * @version  v1.0 .
 * @date       2016-6-13 .
 * @file          MPFileUtility.java .
 * @brief        .
 */
public class MPFileUtility
{

    final private static String CACHEFOLDER = "mpstore";
    final private static String TAG = "FileUtility";

    public static File getCacheRootDirectoryHandle(Context context)
    {
        return (context.getDir(CACHEFOLDER, Context.MODE_PRIVATE));
    }

    @Nullable
    public static String getCacheRootDirectory(final Context context)
    {

        File file = context.getDir(CACHEFOLDER, Context.MODE_PRIVATE);

        if (file != null)
            return (file.getAbsolutePath());

        return null;
    }

    //currently not checking directory or file path
    public static String getFileNameFromFilePath(final String fileFullPath)
    {

        File file = new File(fileFullPath);

        if (file != null)
            return file.getName();

        return null;
    }

    //currently not checking directory or file path
    public static String getDirecoryPathFromFilePath(final String fileFullPath)
    {

        File file = new File(fileFullPath);

        if (file != null)
            return file.getParent();

        return null;
    }

    public static String getUniqueFileName()
    {

        return UUID.randomUUID().toString();

    }

    public static String getUniqueFileNameWithExtension(final String extension)
    {

        return (UUID.randomUUID().toString() + "." + extension);
    }

    // TODO: some files may not get deleted so we may need to throw exception in that case

    public static boolean clearCacheContent(final Context context)
    {

        File rootCacheDir = getCacheRootDirectoryHandle(context);
        Boolean bResult = false;
        // Directories must be empty before they will be deleted.
        for (File file : rootCacheDir.listFiles())
            bResult = file.delete();

        //return(rootCacheDir.delete());
        return bResult;
    }

    //this will just give file handle of file which may not be present
    //file name can be null
    @Nullable
    public static File getFileFromName(final Context context, final String fileName)
    {

        File rootDir = getCacheRootDirectoryHandle(context);

        if (rootDir != null)
        {

            File file = new File(rootDir, (fileName != null ? fileName : getUniqueFileName()));
            return file;
        }

        return null;
    }

    //this is similar to getFileFromName but it also taken extra argument as extension
    //file name can be null
    @Nullable
    public static File getFileFromName(final Context context, final String fileName, final String ext)
    {

        File rootDir = getCacheRootDirectoryHandle(context);

        if (rootDir != null)
        {

            String tmpFileName = ((fileName != null) ? fileName : getUniqueFileName());

            if (ext != null)
                tmpFileName += "." + ext;

            return (getFileFromName(context, tmpFileName));
        }

        return null;
    }

    //this will return object if file is present
    //file name can be null
    @Nullable
    public static File getFileFromPath(final Context context, final String filePath)
    {

        File rootDir = getCacheRootDirectoryHandle(context);

        if (rootDir != null)
        {

            File file = new File(rootDir, getFileNameFromFilePath(filePath));

            if (file != null && file.exists())
                return file;
        }

        return null;
    }

    public static boolean removeFile(final String filePath)
    {

        File file = new File(filePath);

        if (file.exists())
            return (file.delete());

        return false;
    }

    public static boolean removeFile(final File file)
    {

        if (file.exists())
            return (file.delete());

        return false;
    }

    public boolean isDirectoryExist(final String filePath)
    {

        File file = new File(filePath);

        return file.exists() && file.isDirectory();

    }

    public boolean isDirectoryExist(final File file)
    {

        return file.exists() && file.isDirectory();

    }


    @Nullable
    public static String[] getAllFilesFromCache(final Context context)
    {

        File rootDir = getCacheRootDirectoryHandle(context);

        if (rootDir != null)
        {
            return (rootDir.list());
        }

        return null;
    }

    @Nullable
    public static File[] getAllFileHandlesFromCache(final Context context)
    {

        File rootDir = getCacheRootDirectoryHandle(context);

        if (rootDir != null)
        {
            return (rootDir.listFiles());
        }

        return null;
    }

    public static boolean saveImage(final Context context, final Bitmap bmp, final String fileName, final boolean bPNGCompression)
    {
        File rootDir = getCacheRootDirectoryHandle(context);
        boolean success = false;

        if (rootDir != null)
        {

            File file = new File(rootDir, fileName);

            if (file != null)
            {

                try
                {

                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = new FileOutputStream(file);

                    if (bPNGCompression)
                    {
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    }
                    else
                    {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    }

                    fileOutputStream.close();

                    success = true;
                }
                catch (IOException e)
                {

                    Log.d(TAG, e.getMessage());
                }
                catch (Exception e)
                {
                    Log.d(TAG, e.getMessage());
                }
            }

        }

        return success;
    }


    public static void copyFile(File sourceFile, File destFile) throws IOException
    {
        if (!destFile.exists())
            destFile.createNewFile();

        FileChannel source = null;
        FileChannel destination = null;

        try
        {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally
        {
            if (source != null)
                source.close();

            if (destination != null)
                destination.close();
        }
    }
}
