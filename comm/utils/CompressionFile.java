package com.siweidg.comm.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;


public class CompressionFile
{
  private static final Logger logger = Logger.getLogger(CompressionFile.class);

  public static void unRarFile(String srcRarPath, String dstDirectoryPath)
  {
    File dstDiretory = new File(dstDirectoryPath);
    if (!dstDiretory.exists()) {
      dstDiretory.mkdirs();
    }
    System.out.println("srcRarPath::::" + srcRarPath);
    Archive a = null;
    try {
      a = new Archive(new File(srcRarPath));
      if (a != null) {
        a.getMainHeader().print();
        FileHeader fh = a.nextFileHeader();
        while (fh != null) {
          if (fh.isDirectory())
          {
            String compressFileName;
            if (existZH(fh.getFileNameW().trim()))
              compressFileName = fh.getFileNameW().trim();
            else {
              compressFileName = fh.getFileNameString().trim();
            }

            File fol = new File(dstDirectoryPath + File.separator + 
              compressFileName);
            fol.mkdirs();
          }
          else
          {
            String compressFileName;
            if (existZH(fh.getFileNameW().trim()))
              compressFileName = fh.getFileNameW().trim();
            else {
              compressFileName = fh.getFileNameString().trim();
            }
            File out = new File(dstDirectoryPath + File.separator + 
              compressFileName);
            try
            {
              if (!out.exists()) {
                if (!out.getParentFile().exists()) {
                  out.getParentFile().mkdirs();
                }
                out.createNewFile();
              }
              FileOutputStream os = new FileOutputStream(out);
              a.extractFile(fh, os);
              os.close();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          fh = a.nextFileHeader();
        }
        a.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void unZipFile(String oldfile, String newpath)
  {
    System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding"));
    long startTime = System.currentTimeMillis();
    try
    {
      ZipInputStream Zin = new ZipInputStream(new FileInputStream(oldfile));
      BufferedInputStream Bin = new BufferedInputStream(Zin);

      String Parent = newpath;
      File Fout = null;
      try
      {
        System.out.println("2" + Zin.getNextEntry());
        ZipEntry entry;
        while (((entry = Zin.getNextEntry()) != null) && (!entry.isDirectory()))
        {
          Fout = new File(Parent, entry.getName());
          if (!Fout.exists()) {
            new File(Fout.getParent()).mkdirs();
          }

          FileOutputStream out = new FileOutputStream(Fout);
          BufferedOutputStream Bout = new BufferedOutputStream(out);
          int b;
          while ((b = Bin.read()) != -1)
          {
            Bout.write(b);
          }

          Bout.close();
          out.close();
          System.out.println("1");
          System.out.println(Fout + "解压成功");
        }
        Bin.close();
        Zin.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    long endTime = System.currentTimeMillis();
    System.out.println("耗费时间： " + (endTime - startTime) + " ms");
  }

  public static boolean existZH(String str) {
    String regEx = "[\\u4e00-\\u9fa5]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(str);
    if (m.find()) {
      return true;
    }
    return false;
  }

  public static void unFileZip(String oldfile, String newpath)
  {
    long startTime = System.currentTimeMillis();
    Project p = new Project();
    Expand e = new Expand();
    e.setProject(p);
    e.setSrc(new File(oldfile));
    e.setOverwrite(false);
    e.setDest(new File(newpath));
    e.setEncoding("gbk");
    e.execute();
    long endTime = System.currentTimeMillis();
    System.out.println("耗费时间： " + (endTime - startTime) + " ms");
  }

  public static void unZip(String zipPath, String dest)
  {
    try
    {
      ZipFile zFile = new ZipFile(zipPath);
      String sysEncoding = System.getProperty("sun.jnu.encoding");
      zFile.setFileNameCharset(sysEncoding);
      if (!zFile.isValidZipFile()) {
        throw new ZipException("压缩文件不合法,可能被损坏.");
      }
      File destDir = new File(dest);
      if ((destDir.isDirectory()) && (!destDir.exists())) {
        destDir.mkdir();
      }
      zFile.extractAll(dest);
      logger.info("解压文件结束,文件目录 : " + destDir.getPath());
      System.out.println("解压文件结束");
    } catch (ZipException e) {
      e.printStackTrace();
      logger.error("解压zip包失败", e);
    }
  }

  public static void main(String[] args) {
    unZipFile("C:\\Users\\lamber\\Desktop\\中国范围0-6新疆范围7-13新疆9城市14-18透明rpf.zip", "E:\\新建文件夹");
  }


}