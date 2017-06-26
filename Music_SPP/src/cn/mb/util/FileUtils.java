package cn.mb.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import cn.mb.http.VDLog;

public class FileUtils {

	/** 对象序列化 */
	public static void ObjectSerialization(File file, Object object)
			throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				fileOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		objectOutputStream.close();
		objectOutputStream = null;
	}

	/** 对象反序列化 */
	public static Object ObjectDeserialization(File file)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInpuStream = new ObjectInputStream(
				fileInputStream);
		Object object = objectInpuStream.readObject();
		objectInpuStream.close();
		objectInpuStream = null;
		return object;
	}

	/** 复制文件 */
	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			File pf = destFile.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	
	public static boolean copyAssetsToSdCard(Context context, String name,
			String path) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = context.getAssets().open(name);

			File file = new File(path);
			File pf = file.getParentFile();
			if (!pf.exists()) {
				pf.mkdirs();
			}
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/** 删除文件夹 */
	public static void deleteAllFilesOfDir(File path, boolean isDeleteSelf) {
		if (path == null || !path.exists()) {
			return;
		}
		// 如果是文件就直接删除了
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i], isDeleteSelf);
		}
		if (isDeleteSelf) {
			path.delete();
		}
	}

	/** 创建 .nomeadia */
	public static void createNoMediaFile(File parentFile) {
		try {
			// if (!parentFile.exists()) {
			// parentFile.mkdirs();
			// }
			File file = new File(parentFile, ".nomedia");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压 zip
	 * 
	 * @param file
	 *            需要解压的文件
	 */
	public static boolean unpackZip(File file) {
		InputStream is = null;
		;
		ZipInputStream zis = null;
		try {
			is = new FileInputStream(file);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];

			File parFile = file.getParentFile();
			String filename;
			while ((ze = zis.getNextEntry()) != null) {
				// zapis do souboru
				filename = ze.getName();
				// filename = filename.substring(filename.lastIndexOf("/") + 1);
				System.out.println(filename);

				// Need to create directories if not exists, or
				// it will generate an Exception...
				File fmd = new File(parFile, filename);
				if (ze.isDirectory()) {
					fmd.mkdirs();
					continue;
				}

				FileOutputStream fout = new FileOutputStream(fmd);

				// cteni zipu a zapis
				int count;
				while ((count = zis.read(buffer)) != -1) {
					fout.write(buffer, 0, count);
				}
				zis.closeEntry();
				fout.close();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (zis != null) {
					zis.close();
					zis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 把字符串存储到txt文件当中
	 * @param savePath    保存路径
	 * @param name          存储文件名
	 * @param content      字符串
	 * @param append      是否追加
	 * @return
	 */
	public static boolean createFileByContent(String savePath, String name, String content, boolean append){
		try{
			FileOutputStream fOut = null;
			File dir = new File(savePath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File(savePath + File.separator + name);
			fOut = new FileOutputStream(file, append);
			if(content == null){
				content = "";
			}
			fOut.write(content.getBytes());
			fOut.flush();
			fOut.close();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	 public final static String FILE_EXTENSION_SEPARATOR = ".";

	    /**
	     * read file
	     * 
	     * @param filePath
	     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	     * @return if file not exist, return null, else return content of file
	     * @throws RuntimeException if an error occurs while operator BufferedReader
	     */
	    public static StringBuilder readFile(String filePath, String charsetName) {
	        File file = new File(filePath);
	        StringBuilder fileContent = new StringBuilder("");
	        if (file == null || !file.isFile()) {
	            return null;
	        }

	        BufferedReader reader = null;
	        try {
	            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
	            reader = new BufferedReader(is);
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                if (!fileContent.toString().equals("")) {
	                    fileContent.append("\r\n");
	                }
	                fileContent.append(line);
	            }
	            reader.close();
	            return fileContent;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * write file
	     * 
	     * @param filePath
	     * @param content
	     * @param append is append, if true, write to the end of file, else clear content of file and write into it
	     * @return return false if content is empty, true otherwise
	     * @throws RuntimeException if an error occurs while operator FileWriter
	     */
	    public static boolean writeFile(String filePath, String content, boolean append) {
	        if (StringUtils.isEmpty(content)) {
	            return false;
	        }

	        FileWriter fileWriter = null;
	        try {
	            makeDirs(filePath);
	            fileWriter = new FileWriter(filePath, append);
	            fileWriter.write(content);
	            fileWriter.close();
	            return true;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (fileWriter != null) {
	                try {
	                    fileWriter.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * write file
	     * 
	     * @param filePath
	     * @param contentList
	     * @param append is append, if true, write to the end of file, else clear content of file and write into it
	     * @return return false if contentList is empty, true otherwise
	     * @throws RuntimeException if an error occurs while operator FileWriter
	     */
	    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
	        if (ListUtils.isEmpty(contentList)) {
	            return false;
	        }

	        FileWriter fileWriter = null;
	        try {
	            makeDirs(filePath);
	            fileWriter = new FileWriter(filePath, append);
	            int i = 0;
	            for (String line : contentList) {
	                if (i++ > 0) {
	                    fileWriter.write("\r\n");
	                }
	                fileWriter.write(line);
	            }
	            fileWriter.close();
	            return true;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (fileWriter != null) {
	                try {
	                    fileWriter.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * write file, the string will be written to the begin of the file
	     * 
	     * @param filePath
	     * @param content
	     * @return
	     */
	    public static boolean writeFile(String filePath, String content) {
	        return writeFile(filePath, content, false);
	    }

	    /**
	     * write file, the string list will be written to the begin of the file
	     * 
	     * @param filePath
	     * @param contentList
	     * @return
	     */
	    public static boolean writeFile(String filePath, List<String> contentList) {
	        return writeFile(filePath, contentList, false);
	    }

	    /**
	     * write file, the bytes will be written to the begin of the file
	     * 
	     * @param filePath
	     * @param stream
	     * @return
	     * @see {@link #writeFile(String, InputStream, boolean)}
	     */
	    public static boolean writeFile(String filePath, InputStream stream) {
	        return writeFile(filePath, stream, false);
	    }

	    /**
	     * write file
	     * 
	     * @param file the file to be opened for writing.
	     * @param stream the input stream
	     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
	     * @return return true
	     * @throws RuntimeException if an error occurs while operator FileOutputStream
	     */
	    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
	        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
	    }

	    /**
	     * write file, the bytes will be written to the begin of the file
	     * 
	     * @param file
	     * @param stream
	     * @return
	     * @see {@link #writeFile(File, InputStream, boolean)}
	     */
	    public static boolean writeFile(File file, InputStream stream) {
	        return writeFile(file, stream, false);
	    }

	    /**
	     * write file
	     * 
	     * @param file the file to be opened for writing.
	     * @param stream the input stream
	     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
	     * @return return true
	     * @throws RuntimeException if an error occurs while operator FileOutputStream
	     */
	    public static boolean writeFile(File file, InputStream stream, boolean append) {
	        OutputStream o = null;
	        try {
	            makeDirs(file.getAbsolutePath());
	            o = new FileOutputStream(file, append);
	            byte data[] = new byte[1024];
	            int length = -1;
	            while ((length = stream.read(data)) != -1) {
	                o.write(data, 0, length);
	            }
	            o.flush();
	            return true;
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException("FileNotFoundException occurred. ", e);
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (o != null) {
	                try {
	                    o.close();
	                    stream.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * copy file
	     * 
	     * @param sourceFilePath
	     * @param destFilePath
	     * @return
	     * @throws RuntimeException if an error occurs while operator FileOutputStream
	     */
	    public static boolean copyFile(String sourceFilePath, String destFilePath) {
	        InputStream inputStream = null;
	        try {
	            inputStream = new FileInputStream(sourceFilePath);
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException("FileNotFoundException occurred. ", e);
	        }
	        return writeFile(destFilePath, inputStream);
	    }

	    /**
	     * read file to string list, a element of list is a line
	     * 
	     * @param filePath
	     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
	     * @return if file not exist, return null, else return content of file
	     * @throws RuntimeException if an error occurs while operator BufferedReader
	     */
	    public static List<String> readFileToList(String filePath, String charsetName) {
	        File file = new File(filePath);
	        List<String> fileContent = new ArrayList<String>();
	        if (file == null || !file.isFile()) {
	            return null;
	        }

	        BufferedReader reader = null;
	        try {
	            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
	            reader = new BufferedReader(is);
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                fileContent.add(line);
	            }
	            reader.close();
	            return fileContent;
	        } catch (IOException e) {
	            throw new RuntimeException("IOException occurred. ", e);
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    throw new RuntimeException("IOException occurred. ", e);
	                }
	            }
	        }
	    }

	    /**
	     * get file name from path, not include suffix
	     * 
	     * <pre>
	     *      getFileNameWithoutExtension(null)               =   null
	     *      getFileNameWithoutExtension("")                 =   ""
	     *      getFileNameWithoutExtension("   ")              =   "   "
	     *      getFileNameWithoutExtension("abc")              =   "abc"
	     *      getFileNameWithoutExtension("a.mp3")            =   "a"
	     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
	     *      getFileNameWithoutExtension("c:\\")              =   ""
	     *      getFileNameWithoutExtension("c:\\a")             =   "a"
	     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
	     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
	     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
	     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
	     * </pre>
	     * 
	     * @param filePath
	     * @return file name from path, not include suffix
	     * @see
	     */
	    public static String getFileNameWithoutExtension(String filePath) {
	        if (StringUtils.isEmpty(filePath)) {
	            return filePath;
	        }

	        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
	        int filePosi = filePath.lastIndexOf(File.separator);
	        if (filePosi == -1) {
	            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
	        }
	        if (extenPosi == -1) {
	            return filePath.substring(filePosi + 1);
	        }
	        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
	    }

	    /**
	     * get file name from path, include suffix
	     * 
	     * <pre>
	     *      getFileName(null)               =   null
	     *      getFileName("")                 =   ""
	     *      getFileName("   ")              =   "   "
	     *      getFileName("a.mp3")            =   "a.mp3"
	     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	     *      getFileName("abc")              =   "abc"
	     *      getFileName("c:\\")              =   ""
	     *      getFileName("c:\\a")             =   "a"
	     *      getFileName("c:\\a.b")           =   "a.b"
	     *      getFileName("c:a.txt\\a")        =   "a"
	     *      getFileName("/home/admin")      =   "admin"
	     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	     * </pre>
	     * 
	     * @param filePath
	     * @return file name from path, include suffix
	     */
	    public static String getFileName(String filePath) {
	        if (StringUtils.isEmpty(filePath)) {
	            return filePath;
	        }

	        int filePosi = filePath.lastIndexOf(File.separator);
	        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	    }

	    /**
	     * get folder name from path
	     * 
	     * <pre>
	     *      getFolderName(null)               =   null
	     *      getFolderName("")                 =   ""
	     *      getFolderName("   ")              =   ""
	     *      getFolderName("a.mp3")            =   ""
	     *      getFolderName("a.b.rmvb")         =   ""
	     *      getFolderName("abc")              =   ""
	     *      getFolderName("c:\\")              =   "c:"
	     *      getFolderName("c:\\a")             =   "c:"
	     *      getFolderName("c:\\a.b")           =   "c:"
	     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
	     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
	     *      getFolderName("/home/admin")      =   "/home"
	     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
	     * </pre>
	     * 
	     * @param filePath
	     * @return
	     */
	    public static String getFolderName(String filePath) {

	        if (StringUtils.isEmpty(filePath)) {
	            return filePath;
	        }

	        int filePosi = filePath.lastIndexOf(File.separator);
	        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	    }

	    /**
	     * get suffix of file from path
	     * 
	     * <pre>
	     *      getFileExtension(null)               =   ""
	     *      getFileExtension("")                 =   ""
	     *      getFileExtension("   ")              =   "   "
	     *      getFileExtension("a.mp3")            =   "mp3"
	     *      getFileExtension("a.b.rmvb")         =   "rmvb"
	     *      getFileExtension("abc")              =   ""
	     *      getFileExtension("c:\\")              =   ""
	     *      getFileExtension("c:\\a")             =   ""
	     *      getFileExtension("c:\\a.b")           =   "b"
	     *      getFileExtension("c:a.txt\\a")        =   ""
	     *      getFileExtension("/home/admin")      =   ""
	     *      getFileExtension("/home/admin/a.txt/b")  =   ""
	     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
	     * </pre>
	     * 
	     * @param filePath
	     * @return
	     */
	    public static String getFileExtension(String filePath) {
	        if (StringUtils.isBlank(filePath)) {
	            return filePath;
	        }

	        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
	        int filePosi = filePath.lastIndexOf(File.separator);
	        if (extenPosi == -1) {
	            return "";
	        }
	        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	    }

	    /**
	     * Creates the directory named by the trailing filename of this file, including the complete directory path required
	     * to create this directory. <br/>
	     * <br/>
	     * <ul>
	     * <strong>Attentions:</strong>
	     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
	     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
	     * </ul>
	     * 
	     * @param filePath
	     * @return true if the necessary directories have been created or the target directory already exists, false one of
	     *         the directories can not be created.
	     *         <ul>
	     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
	     *         <li>if target directory already exists, return true</li>
	     *         <li>return {@link java.io.File#makeFolder}</li>
	     *         </ul>
	     */
	    public static boolean makeDirs(String filePath) {
	        String folderName = getFolderName(filePath);
	        if (StringUtils.isEmpty(folderName)) {
	            return false;
	        }

	        File folder = new File(folderName);
	        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	    }

	    /**
	     * @param filePath
	     * @return
	     * @see #makeDirs(String)
	     */
	    public static boolean makeFolders(String filePath) {
	        return makeDirs(filePath);
	    }

	    /**
	     * Indicates if this file represents a file on the underlying file system.
	     * 
	     * @param filePath
	     * @return
	     */
	    public static boolean isFileExist(String filePath) {
	        if (StringUtils.isBlank(filePath)) {
	            return false;
	        }

	        File file = new File(filePath);
	        return (file.exists() && file.isFile());
	    }

	    /**
	     * Indicates if this file represents a directory on the underlying file system.
	     * 
	     * @param directoryPath
	     * @return
	     */
	    public static boolean isFolderExist(String directoryPath) {
	        if (StringUtils.isBlank(directoryPath)) {
	            return false;
	        }

	        File dire = new File(directoryPath);
	        return (dire.exists() && dire.isDirectory());
	    }

	    /**
	     * delete file or directory
	     * <ul>
	     * <li>if path is null or empty, return true</li>
	     * <li>if path not exist, return true</li>
	     * <li>if path exist, delete recursion. return true</li>
	     * <ul>
	     * 
	     * @param path
	     * @return
	     */
	    public static boolean deleteFile(String path) {
	        if (StringUtils.isBlank(path)) {
	            return true;
	        }

	        File file = new File(path);
	        if (!file.exists()) {
	            return true;
	        }
	        if (file.isFile()) {
	            return file.delete();
	        }
	        if (!file.isDirectory()) {
	            return false;
	        }
	        for (File f : file.listFiles()) {
	            if (f.isFile()) {
	                f.delete();
	            } else if (f.isDirectory()) {
	                deleteFile(f.getAbsolutePath());
	            }
	        }
	        return file.delete();
	    }

	    /**
	     * get file size
	     * <ul>
	     * <li>if path is null or empty, return -1</li>
	     * <li>if path exist and it is a file, return file size, else return -1</li>
	     * <ul>
	     * 
	     * @param path
	     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
	     */
	    public static long getFileSize(String path) {
	        if (StringUtils.isBlank(path)) {
	            return -1;
	        }

	        File file = new File(path);
	        return (file.exists() && file.isFile() ? file.length() : -1);
	    }
	    public static String getPath(String subPath) {
			String resPath = "";
			if (isExternalStorage()) {
				resPath = Environment.getExternalStorageDirectory().getAbsolutePath() + subPath;
				
				File file = new File(resPath);
				if (!file.exists()) {
					file.mkdirs();
				}
			}
			return resPath;
		}
	    /**
		 * SD卡的状态
		 * 
		 * @return
		 */
		private static boolean isExternalStorage() {
			String status = Environment.getExternalStorageState();// SDK状态

			if (status.equals(Environment.MEDIA_MOUNTED)) {// SD卡正常挂载
				return true;
			} else {
				return false;
			}
		}

		
		public static final String[] VIDEO_EXTENSIONS = { "264", "3g2", "3gp",
				"3gp2", "3gpp", "3gpp2", "3mm", "3p2", "60d", "aep", "ajp", "amv",
				"amx", "arf", "asf", "asx", "avb", "avd", "avi", "avs", "avs",
				"axm", "bdm", "bdmv", "bik", "bix", "bmk", "box", "bs4", "bsf",
				"byu", "camre", "clpi", "cpi", "cvc", "d2v", "d3v", "dav", "dce",
				"dck", "ddat", "dif", "dir", "divx", "dlx", "dmb", "dmsm", "dmss",
				"dnc", "dpg", "dream", "dsy", "dv", "dv-avi", "dv4", "dvdmedia",
				"dvr-ms", "dvx", "dxr", "dzm", "dzp", "dzt", "evo", "eye", "f4p",
				"f4v", "fbr", "fbr", "fbz", "fcp", "flc", "flh", "fli", "flv",
				"flx", "gl", "grasp", "gts", "gvi", "gvp", "hdmov", "hkm", "ifo",
				"imovi", "imovi", "iva", "ivf", "ivr", "ivs", "izz", "izzy", "jts",
				"lsf", "lsx", "m15", "m1pg", "m1v", "m21", "m21", "m2a", "m2p",
				"m2t", "m2ts", "m2v", "m4e", "m4u", "m4v", "m75", "meta", "mgv",
				"mj2", "mjp", "mjpg", "mkv", "mmv", "mnv", "mod", "modd", "moff",
				"moi", "moov", "mov", "movie", "mp21", "mp21", "mp2v", "mp4",
				"mp4v", "mpe", "mpeg", "mpeg4", "mpf", "mpg", "mpg2", "mpgin",
				"mpl", "mpls", "mpv", "mpv2", "mqv", "msdvd", "msh", "mswmm",
				"mts", "mtv", "mvb", "mvc", "mvd", "mve", "mvp", "mxf", "mys",
				"ncor", "nsv", "nvc", "ogm", "ogv", "ogx", "osp", "par", "pds",
				"pgi", "piv", "playlist", "pmf", "prel", "pro", "prproj", "psh",
				"pva", "pvr", "pxv", "qt", "qtch", "qtl", "qtm", "qtz",
				"rcproject", "rdb", "rec", "rm", "rmd", "rmp", "rmvb", "roq", "rp",
				"rts", "rts", "rum", "rv", "sbk", "sbt", "scm", "scm", "scn",
				"sec", "seq", "sfvidcap", "smil", "smk", "sml", "smv", "spl",
				"ssm", "str", "stx", "svi", "swf", "swi", "swt", "tda3mt", "tivo",
				"tix", "tod", "tp", "tp0", "tpd", "tpr", "trp", "ts", "tvs", "vc1",
				"vcr", "vcv", "vdo", "vdr", "veg", "vem", "vf", "vfw", "vfz",
				"vgz", "vid", "viewlet", "viv", "vivo", "vlab", "vob", "vp3",
				"vp6", "vp7", "vpj", "vro", "vsp", "w32", "wcp", "webm", "wm",
				"wmd", "wmmp", "wmv", "wmx", "wp3", "wpl", "wtv", "wvx", "xfl",
				"xvid", "yuv", "zm1", "zm2", "zm3", "zmv" };
		// http://www.fileinfo.com/filetypes/audio , "spx" , "mid" , "sf"
		public static final String[] AUDIO_EXTENSIONS = { "4mp", "669", "6cm",
				"8cm", "8med", "8svx", "a2m", "aa", "aa3", "aac", "aax", "abc",
				"abm", "ac3", "acd", "acd-bak", "acd-zip", "acm", "act", "adg",
				"afc", "agm", "ahx", "aif", "aifc", "aiff", "ais", "akp", "al",
				"alaw", "all", "amf", "amr", "ams", "ams", "aob", "ape", "apf",
				"apl", "ase", "at3", "atrac", "au", "aud", "aup", "avr", "awb",
				"band", "bap", "bdd", "box", "bun", "bwf", "c01", "caf", "cda",
				"cdda", "cdr", "cel", "cfa", "cidb", "cmf", "copy", "cpr", "cpt",
				"csh", "cwp", "d00", "d01", "dcf", "dcm", "dct", "ddt", "dewf",
				"df2", "dfc", "dig", "dig", "dls", "dm", "dmf", "dmsa", "dmse",
				"drg", "dsf", "dsm", "dsp", "dss", "dtm", "dts", "dtshd", "dvf",
				"dwd", "ear", "efa", "efe", "efk", "efq", "efs", "efv", "emd",
				"emp", "emx", "esps", "f2r", "f32", "f3r", "f4a", "f64", "far",
				"fff", "flac", "flp", "fls", "frg", "fsm", "fzb", "fzf", "fzv",
				"g721", "g723", "g726", "gig", "gp5", "gpk", "gsm", "gsm", "h0",
				"hdp", "hma", "hsb", "ics", "iff", "imf", "imp", "ins", "ins",
				"it", "iti", "its", "jam", "k25", "k26", "kar", "kin", "kit",
				"kmp", "koz", "koz", "kpl", "krz", "ksc", "ksf", "kt2", "kt3",
				"ktp", "l", "la", "lqt", "lso", "lvp", "lwv", "m1a", "m3u", "m4a",
				"m4b", "m4p", "m4r", "ma1", "mdl", "med", "mgv", "midi", "miniusf",
				"mka", "mlp", "mmf", "mmm", "mmp", "mo3", "mod", "mp1", "mp2",
				"mp3", "mpa", "mpc", "mpga", "mpu", "mp_", "mscx", "mscz", "msv",
				"mt2", "mt9", "mte", "mti", "mtm", "mtp", "mts", "mus", "mws",
				"mxl", "mzp", "nap", "nki", "nra", "nrt", "nsa", "nsf", "nst",
				"ntn", "nvf", "nwc", "odm", "oga", "ogg", "okt", "oma", "omf",
				"omg", "omx", "ots", "ove", "ovw", "pac", "pat", "pbf", "pca",
				"pcast", "pcg", "pcm", "peak", "phy", "pk", "pla", "pls", "pna",
				"ppc", "ppcx", "prg", "prg", "psf", "psm", "ptf", "ptm", "pts",
				"pvc", "qcp", "r", "r1m", "ra", "ram", "raw", "rax", "rbs", "rcy",
				"rex", "rfl", "rmf", "rmi", "rmj", "rmm", "rmx", "rng", "rns",
				"rol", "rsn", "rso", "rti", "rtm", "rts", "rvx", "rx2", "s3i",
				"s3m", "s3z", "saf", "sam", "sb", "sbg", "sbi", "sbk", "sc2", "sd",
				"sd", "sd2", "sd2f", "sdat", "sdii", "sds", "sdt", "sdx", "seg",
				"seq", "ses", "sf2", "sfk", "sfl", "shn", "sib", "sid", "sid",
				"smf", "smp", "snd", "snd", "snd", "sng", "sng", "sou", "sppack",
				"sprg", "sseq", "sseq", "ssnd", "stm", "stx", "sty", "svx", "sw",
				"swa", "syh", "syw", "syx", "td0", "tfmx", "thx", "toc", "tsp",
				"txw", "u", "ub", "ulaw", "ult", "ulw", "uni", "usf", "usflib",
				"uw", "uwf", "vag", "val", "vc3", "vmd", "vmf", "vmf", "voc",
				"voi", "vox", "vpm", "vqf", "vrf", "vyf", "w01", "wav", "wav",
				"wave", "wax", "wfb", "wfd", "wfp", "wma", "wow", "wpk", "wproj",
				"wrk", "wus", "wut", "wv", "wvc", "wve", "wwu", "xa", "xa", "xfs",
				"xi", "xm", "xmf", "xmi", "xmz", "xp", "xrns", "xsb", "xspf", "xt",
				"xwb", "ym", "zvd", "zvr" };

		private static final HashSet<String> mHashVideo;
		private static final HashSet<String> mHashAudio;
		private static final double KB = 1024.0;
		private static final double MB = KB * KB;
		private static final double GB = KB * KB * KB;

		static {
			mHashVideo = new HashSet<String>(Arrays.asList(VIDEO_EXTENSIONS));
			mHashAudio = new HashSet<String>(Arrays.asList(AUDIO_EXTENSIONS));
		}

		/** 是否是音频或者视频 */
		public static boolean isVideoOrAudio(File f) {
			final String ext = getFileExtension(f);
			return mHashVideo.contains(ext) || mHashAudio.contains(ext);
		}

		public static boolean isVideoOrAudio(String f) {
			final String ext = getUrlExtension(f);
			return mHashVideo.contains(ext) || mHashAudio.contains(ext);
		}

		public static boolean isVideo(File f) {
			final String ext = getFileExtension(f);
			return mHashVideo.contains(ext);
		}

		/** 获取文件后缀 */
		public static String getFileExtension(File f) {
			if (f != null) {
				String filename = f.getName();
				int i = filename.lastIndexOf('.');
				if (i > 0 && i < filename.length() - 1) {
					return filename.substring(i + 1).toLowerCase();
				}
			}
			return null;
		}

		public static String getUrlFileName(String url) {
			int slashIndex = url.lastIndexOf('/');
			int dotIndex = url.lastIndexOf('.');
			String filenameWithoutExtension;
			if (dotIndex == -1) {
				filenameWithoutExtension = url.substring(slashIndex + 1);
			} else {
				filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
			}
			return filenameWithoutExtension;
		}

		public static String getUrlExtension(String url) {
			if (!StringUtils.isEmpty(url)) {
				int i = url.lastIndexOf('.');
				if (i > 0 && i < url.length() - 1) {
					return url.substring(i + 1).toLowerCase();
				}
			}
			return "";
		}

		public static String getFileNameNoEx(String filename) {
			if ((filename != null) && (filename.length() > 0)) {
				int dot = filename.lastIndexOf('.');
				if ((dot > -1) && (dot < (filename.length()))) {
					return filename.substring(0, dot);
				}
			}
			return filename;
		}

		public static String showFileSize(long size) {
			String fileSize;
			if (size < KB)
				fileSize = size + "B";
			else if (size < MB)
				fileSize = String.format("%.1f", size / KB) + "KB";
			else if (size < GB)
				fileSize = String.format("%.1f", size / MB) + "MB";
			else
				fileSize = String.format("%.1f", size / GB) + "GB";

			return fileSize;
		}

		/** 显示SD卡剩余空间 */
		public static String showFileAvailable() {
			String result = "";
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				StatFs sf = new StatFs(Environment.getExternalStorageDirectory()
						.getPath());
				long blockSize = sf.getBlockSize();
				long blockCount = sf.getBlockCount();
				long availCount = sf.getAvailableBlocks();
				return showFileSize(availCount * blockSize) + " / "
						+ showFileSize(blockSize * blockCount);
			}
			return result;
		}

		/** 如果不存在就创建 */
		public static boolean createIfNoExists(String path) {
			File file = new File(path);
			boolean mk = false;
			if (!file.exists()) {
				mk = file.mkdirs();
			}
			return mk;
		}

		private static HashMap<String, String> mMimeType = new HashMap<String, String>();
		static {
			mMimeType.put("M1V", "video/mpeg");
			mMimeType.put("MP2", "video/mpeg");
			mMimeType.put("MPE", "video/mpeg");
			mMimeType.put("MPG", "video/mpeg");
			mMimeType.put("MPEG", "video/mpeg");
			mMimeType.put("MP4", "video/mp4");
			mMimeType.put("M4V", "video/mp4");
			mMimeType.put("3GP", "video/3gpp");
			mMimeType.put("3GPP", "video/3gpp");
			mMimeType.put("3G2", "video/3gpp2");
			mMimeType.put("3GPP2", "video/3gpp2");
			mMimeType.put("MKV", "video/x-matroska");
			mMimeType.put("WEBM", "video/x-matroska");
			mMimeType.put("MTS", "video/mp2ts");
			mMimeType.put("TS", "video/mp2ts");
			mMimeType.put("TP", "video/mp2ts");
			mMimeType.put("WMV", "video/x-ms-wmv");
			mMimeType.put("ASF", "video/x-ms-asf");
			mMimeType.put("ASX", "video/x-ms-asf");
			mMimeType.put("FLV", "video/x-flv");
			mMimeType.put("MOV", "video/quicktime");
			mMimeType.put("QT", "video/quicktime");
			mMimeType.put("RM", "video/x-pn-realvideo");
			mMimeType.put("RMVB", "video/x-pn-realvideo");
			mMimeType.put("VOB", "video/dvd");
			mMimeType.put("DAT", "video/dvd");
			mMimeType.put("AVI", "video/x-divx");
			mMimeType.put("OGV", "video/ogg");
			mMimeType.put("OGG", "video/ogg");
			mMimeType.put("VIV", "video/vnd.vivo");
			mMimeType.put("VIVO", "video/vnd.vivo");
			mMimeType.put("WTV", "video/wtv");
			mMimeType.put("AVS", "video/avs-video");
			mMimeType.put("SWF", "video/x-shockwave-flash");
			mMimeType.put("YUV", "video/x-raw-yuv");
		}

		/** 获取MIME */
		public static String getMimeType(String path) {
			int lastDot = path.lastIndexOf(".");
			if (lastDot < 0)
				return null;

			return mMimeType.get(path.substring(lastDot + 1).toUpperCase());
		}

		/** 多个SD卡时 取外置SD卡 */
		public static String getExternalStorageDirectory() {
			// 参考文章
			// http://blog.csdn.net/bbmiku/article/details/7937745
			Map<String, String> map = System.getenv();
			String[] values = new String[map.values().size()];
			map.values().toArray(values);
			String path = values[values.length - 1];
			VDLog.e("nmbb", "FileUtils.getExternalStorageDirectory : " + path);
			if (path.startsWith("/mnt/")
					&& !Environment.getExternalStorageDirectory().getAbsolutePath()
							.equals(path))
				return path;
			else
				return null;
		}

		public static String getCanonical(File f) {
			if (f == null)
				return null;

			try {
				return f.getCanonicalPath();
			} catch (IOException e) {
				return f.getAbsolutePath();
			}
		}
		
		public static boolean sdAvailable() {
			return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment
					.getExternalStorageState())
					|| Environment.MEDIA_MOUNTED.equals(Environment
							.getExternalStorageState());
		}
		
		public static String getVideoLocalPath(String videoUrl){
			String videoName=videoUrl.substring(videoUrl.lastIndexOf("/")+1);
			String localpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoCache/" + videoName;
			
			return localpath;
		}
		
		/**
		 * 计算SD卡的剩余空间
		 * 
		 * @return 返回-1，说明没有安装sd卡
		 */
		public static long getFreeDiskSpace() {
			String status = Environment.getExternalStorageState();
			long freeSpace = 0;
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				try {
					File path = Environment.getExternalStorageDirectory();
					StatFs stat = new StatFs(path.getPath());
					long blockSize = stat.getBlockSize();
					long availableBlocks = stat.getAvailableBlocks();
					freeSpace = availableBlocks * blockSize / (1024 * 1024);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return -1;
			}
			return (freeSpace);
		}
}
